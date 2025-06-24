package pe.edu.vallegrande.vgmsusersauthentication.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsusersauthentication.application.service.UserService;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.model.*;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCreateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCompleteUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserWithWaterBoxesResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.exception.CustomException;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository.AuthRepository;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;
     private final AuthRepository authRepository;
     private final PasswordEncoder passwordEncoder;

     @Override
     public Mono<UserResponse> createUser(UserCreateRequest request) {
          log.info("Creating user with email: {}", request.getEmail());

          return validateUserDoesNotExist(request)
                    .then(generateUserCode(request.getOrganizationId()))
                    .flatMap(userCode -> {
                         User user = buildUserFromRequest(request, userCode);

                         return userRepository.save(user)
                                   .flatMap(savedUser -> {
                                        AuthCredential authCredential = buildAuthCredentialFromRequest(request,
                                                  savedUser.getId());
                                        return authRepository.save(authCredential)
                                                  .then(Mono.just(savedUser));
                                   })
                                   .flatMap(this::mapToUserResponse);
                    })
                    .doOnSuccess(response -> log.info("User created successfully with ID: {}", response.getId()))
                    .doOnError(error -> log.error("Error creating user: {}", error.getMessage()));
     }

     @Override
     public Mono<UserResponse> updateUserPartially(String userId, UserUpdateRequest request) {
          log.info("Updating user partially with ID: {}", userId);

          return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(user -> {
                         updateUserFromPartialRequest(user, request);
                         user.setUpdatedAt(LocalDateTime.now());
                         return userRepository.save(user);
                    }).flatMap(this::mapToUserResponse)
                    .doOnSuccess(response -> log.info("User updated successfully with ID: {}", response.getId()))
                    .doOnError(error -> log.error("Error updating user: {}", error.getMessage()));
     }

     @Override
     public Mono<UserResponse> updateUserCompletely(String userId, UserCompleteUpdateRequest request) {
          log.info("Updating user completely with ID: {}", userId);

          return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(existingUser -> {
                         User updatedUser = buildUserFromCompleteUpdateRequest(request, existingUser.getUserCode());
                         updatedUser.setId(existingUser.getId());
                         updatedUser.setCreatedAt(existingUser.getCreatedAt());
                         updatedUser.setUpdatedAt(LocalDateTime.now());

                         return userRepository.save(updatedUser)
                                   .flatMap(savedUser -> updateUserRoles(userId, request.getRoles())
                                             .thenReturn(savedUser));
                    })
                    .flatMap(this::mapToUserResponse)
                    .doOnSuccess(response -> log.info("User updated completely with ID: {}", response.getId()))
                    .doOnError(error -> log.error("Error updating user completely: {}", error.getMessage()));
     }

     @Override
     public Mono<Void> deleteUserLogically(String userId) {
          log.info("Deleting user logically with ID: {}", userId);

          return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(user -> {
                         user.setStatus(StatusUsers.INACTIVE);
                         user.setUpdatedAt(LocalDateTime.now());
                         return userRepository.save(user);
                    })
                    .flatMap(user -> authRepository.findByUserId(userId)
                              .flatMap(auth -> {
                                   auth.setStatus(StatusUsers.INACTIVE);
                                   return authRepository.save(auth);
                              }))
                    .then()
                    .doOnSuccess(v -> log.info("User deleted logically with ID: {}", userId))
                    .doOnError(error -> log.error("Error deleting user logically: {}", error.getMessage()));
     }

     @Override
     public Mono<UserResponse> restoreUserLogically(String userId) {
          log.info("Restoring user logically with ID: {}", userId);

          return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(user -> {
                         user.setStatus(StatusUsers.ACTIVE);
                         user.setUpdatedAt(LocalDateTime.now());
                         return userRepository.save(user);
                    })
                    .flatMap(user -> authRepository.findByUserId(userId)
                              .flatMap(auth -> {
                                   auth.setStatus(StatusUsers.ACTIVE);
                                   return authRepository.save(auth);
                              })
                              .then(Mono.just(user)))
                    .flatMap(this::mapToUserResponse)
                    .doOnSuccess(response -> log.info("User restored logically with ID: {}", response.getId()))
                    .doOnError(error -> log.error("Error restoring user logically: {}", error.getMessage()));
     }

     @Override
     public Flux<UserResponse> getAllUsers() {
          log.info("Getting all users");
          return userRepository.findAll()
                    .flatMap(this::mapToUserResponse)
                    .doOnComplete(() -> log.info("Retrieved all users"));
     }

     @Override
     public Mono<UserResponse> getUserById(String userId) {
          log.info("Getting user by ID: {}", userId);
          return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Mono<UserResponse> getUserByEmail(String email) {
          log.info("Getting user by email: {}", email);
          return userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado con el email proporcionado",
                              "USER_NOT_FOUND", 404)))
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Mono<UserResponse> getUserByDocumentNumber(String documentNumber) {
          log.info("Getting user by document number: {}", documentNumber);
          return userRepository.findByDocumentNumber(documentNumber)
                    .switchIfEmpty(Mono.error(new CustomException(
                              "Usuario no encontrado con el documento proporcionado", "USER_NOT_FOUND", 404)))
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Flux<UserResponse> getUsersByRole(RolesUsers role) {
          log.info("Getting users by role: {}", role);
          return authRepository.findByRolesContaining(role)
                    .flatMap(auth -> userRepository.findById(auth.getUserId()))
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Flux<UserResponse> getUsersByStatus(StatusUsers status) {
          log.info("Getting users by status: {}", status);
          return userRepository.findByStatus(status)
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Flux<UserResponse> getUsersByOrganization(String organizationId) {
          log.info("Getting users by organization: {}", organizationId);
          return userRepository.findByOrganizationId(organizationId)
                    .flatMap(this::mapToUserResponse);
     }

     @Override
     public Mono<Boolean> existsByEmail(String email) {
          return userRepository.existsByEmail(email);
     }

     @Override
     public Mono<Boolean> existsByDocumentNumber(String documentNumber) {
          return userRepository.existsByDocumentNumber(documentNumber);
     }

     @Override
     public Mono<Boolean> existsByUserCode(String userCode) {
          return userRepository.existsByUserCode(userCode);
     }

     private Mono<Void> validateUserDoesNotExist(UserCreateRequest request) {
          return existsByEmail(request.getEmail())
                    .flatMap(emailExists -> {
                         if (emailExists) {
                              return Mono.error(new CustomException("El email ya está registrado",
                                        "EMAIL_ALREADY_EXISTS", 409));
                         }
                         return existsByDocumentNumber(request.getDocumentNumber());
                    })
                    .flatMap(documentExists -> {
                         if (documentExists) {
                              return Mono.error(new CustomException("El documento ya está registrado",
                                        "DOCUMENT_ALREADY_EXISTS", 409));
                         }
                         return authRepository.existsByUsername(request.getUsername());
                    })
                    .flatMap(usernameExists -> {
                         if (usernameExists) {
                              return Mono.error(new CustomException("El nombre de usuario ya existe",
                                        "USERNAME_ALREADY_EXISTS", 409));
                         }
                         return Mono.empty();
                    });
     }

     private Mono<String> generateUserCode(String organizationId) {
          return userRepository.count()
                    .map(count -> {
                         long nextNumber = count + 1;
                         return String.format("USR-%05d", nextNumber);
                    })
                    .flatMap(userCode -> existsByUserCode(userCode)
                              .flatMap(exists -> {
                                   if (exists) {
                                        String timestampCode = "USR-" + System.currentTimeMillis();
                                        return Mono.just(timestampCode);
                                   }
                                   return Mono.just(userCode);
                              }));
     }

     private User buildUserFromRequest(UserCreateRequest request, String userCode) {
          User user = new User();
          user.setUserCode(userCode);
          user.setOrganizationId(request.getOrganizationId());

          PersonalInfo personalInfo = new PersonalInfo();
          personalInfo.setDocumentType(request.getDocumentType());
          personalInfo.setDocumentNumber(request.getDocumentNumber());
          personalInfo.setFirstName(request.getFirstName());
          personalInfo.setLastName(request.getLastName());
          personalInfo.setFullName(request.getFirstName() + " " + request.getLastName());
          user.setPersonalInfo(personalInfo);

          Contact contact = new Contact();
          contact.setEmail(request.getEmail());
          contact.setPhone(request.getPhone());
          AddressUsers address = new AddressUsers();
          address.setStreetAddress(request.getStreetAddress());
          address.setStreetId(request.getStreetId());
          address.setZoneId(request.getZoneId());
          contact.setAddress(address);

          user.setContact(contact);
          user.setWaterBoxes(new ArrayList<>());
          user.setStatus(StatusUsers.ACTIVE);
          user.setRegistrationDate(LocalDateTime.now());
          user.setCreatedAt(LocalDateTime.now());
          user.setUpdatedAt(LocalDateTime.now());

          return user;
     }

     private AuthCredential buildAuthCredentialFromRequest(UserCreateRequest request, String userId) {
          AuthCredential authCredential = new AuthCredential();
          authCredential.setUserId(userId);
          authCredential.setUsername(request.getUsername());
          authCredential.setPasswordHash(passwordEncoder.encode(request.getPassword()));
          authCredential.setRoles(request.getRoles());
          authCredential.setStatus(StatusUsers.ACTIVE);
          authCredential.setCreatedAt(LocalDateTime.now());

          return authCredential;
     }

     private void updateUserFromPartialRequest(User user, UserUpdateRequest request) {
          if (request.getDocumentType().isPresent()) {
               user.getPersonalInfo().setDocumentType(request.getDocumentType().get());
          }
          if (request.getDocumentNumber().isPresent()) {
               user.getPersonalInfo().setDocumentNumber(request.getDocumentNumber().get());
          }
          if (request.getFirstName().isPresent()) {
               user.getPersonalInfo().setFirstName(request.getFirstName().get());
               updateFullName(user);
          }
          if (request.getLastName().isPresent()) {
               user.getPersonalInfo().setLastName(request.getLastName().get());
               updateFullName(user);
          }

          if (request.getEmail().isPresent()) {
               user.getContact().setEmail(request.getEmail().get());
          }
          if (request.getPhone().isPresent()) {
               user.getContact().setPhone(request.getPhone().get());
          }
          if (request.getStreetAddress().isPresent()) {
               user.getContact().getAddress().setStreetAddress(request.getStreetAddress().get());
          }
          if (request.getStreetId().isPresent()) {
               user.getContact().getAddress().setStreetId(request.getStreetId().get());
          }
          if (request.getZoneId().isPresent()) {
               user.getContact().getAddress().setZoneId(request.getZoneId().get());
          }
     }

     private void updateFullName(User user) {
          String firstName = user.getPersonalInfo().getFirstName();
          String lastName = user.getPersonalInfo().getLastName();
          String fullName = firstName + " " + lastName;
          user.getPersonalInfo().setFullName(fullName);
     }

     private Mono<UserResponse> mapToUserResponse(User user) {
          log.debug("Mapping user to response: userId={}", user.getId());

          return authRepository.findByUserId(user.getId())
                    .doOnNext(auth -> log.debug("Found AuthCredential for user {}: username={}, roles={}",
                              user.getId(), auth.getUsername(), auth.getRoles()))
                    .map(auth -> {
                         UserResponse response = createUserResponseFromUser(user);
                         response.setRoles(auth.getRoles() != null ? auth.getRoles() : new ArrayList<>());
                         response.setUsername(auth.getUsername() != null ? auth.getUsername() : "N/A");
                         return response;
                    })
                    .switchIfEmpty(Mono.fromCallable(() -> {
                         log.warn("No AuthCredential found for user {}, using defaults", user.getId());
                         UserResponse response = createUserResponseFromUser(user);
                         response.setRoles(new ArrayList<>());
                         response.setUsername("N/A");
                         return response;
                    }));
     }

     private UserResponse createUserResponseFromUser(User user) {
          UserResponse response = new UserResponse();
          response.setId(user.getId());
          response.setUserCode(user.getUserCode());
          response.setOrganizationId(user.getOrganizationId());

          if (user.getPersonalInfo() != null) {
               response.setDocumentType(user.getPersonalInfo().getDocumentType());
               response.setDocumentNumber(user.getPersonalInfo().getDocumentNumber());
               response.setFirstName(user.getPersonalInfo().getFirstName());
               response.setLastName(user.getPersonalInfo().getLastName());
               response.setFullName(user.getPersonalInfo().getFullName());
          }

          if (user.getContact() != null) {
               response.setEmail(user.getContact().getEmail());
               response.setPhone(user.getContact().getPhone());

               if (user.getContact().getAddress() != null) {
                    response.setStreetAddress(user.getContact().getAddress().getStreetAddress());
                    response.setStreetId(user.getContact().getAddress().getStreetId());
                    response.setZoneId(user.getContact().getAddress().getZoneId());
               }
          }

          response.setStatus(user.getStatus());
          response.setRegistrationDate(user.getRegistrationDate());
          response.setLastLogin(user.getLastLogin());
          response.setCreatedAt(user.getCreatedAt());
          response.setUpdatedAt(user.getUpdatedAt());

          return response;
     }

     @Override
     public Flux<UserWithWaterBoxesResponse> getAllUsersWithWaterBoxes() {
          log.info("Obteniendo todos los usuarios con información de waterboxes");

          return userRepository.findAll()
                    .flatMap(this::mapToUserWithWaterBoxesResponse);
     }

     @Override
     public Mono<UserWithWaterBoxesResponse> getUserWithWaterBoxesById(String userId) {
          log.info("Obteniendo usuario con waterboxes por ID: {}", userId);

          return userRepository.findById(userId)
                    .flatMap(this::mapToUserWithWaterBoxesResponse)
                    .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con ID: " + userId)));
     }

     private Mono<UserWithWaterBoxesResponse> mapToUserWithWaterBoxesResponse(User user) {
          return authRepository.findByUserId(user.getId())
                    .map(auth -> {
                         UserWithWaterBoxesResponse response = new UserWithWaterBoxesResponse();

                         response.setId(user.getId());
                         response.setUserCode(user.getUserCode());
                         response.setOrganizationId(user.getOrganizationId());

                         if (user.getPersonalInfo() != null) {
                              response.setDocumentType(user.getPersonalInfo().getDocumentType());
                              response.setDocumentNumber(user.getPersonalInfo().getDocumentNumber());
                              response.setFirstName(user.getPersonalInfo().getFirstName());
                              response.setLastName(user.getPersonalInfo().getLastName());
                              response.setFullName(user.getPersonalInfo().getFullName());
                         }

                         if (user.getContact() != null) {
                              response.setEmail(user.getContact().getEmail());
                              response.setPhone(user.getContact().getPhone());

                              if (user.getContact().getAddress() != null) {
                                   response.setStreetAddress(user.getContact().getAddress().getStreetAddress());
                                   response.setStreetId(user.getContact().getAddress().getStreetId());
                                   response.setZoneId(user.getContact().getAddress().getZoneId());
                              }
                         }

                         response.setStatus(user.getStatus());
                         response.setRegistrationDate(user.getRegistrationDate());
                         response.setLastLogin(user.getLastLogin());
                         response.setCreatedAt(user.getCreatedAt());
                         response.setUpdatedAt(user.getUpdatedAt());

                         response.setRoles(auth.getRoles());
                         response.setUsername(auth.getUsername());

                         response.setWaterBoxes(generateMockWaterBoxes(user.getOrganizationId(), user.getId()));

                         return response;
                    })
                    .switchIfEmpty(Mono.fromCallable(() -> {
                         UserWithWaterBoxesResponse response = new UserWithWaterBoxesResponse();

                         response.setId(user.getId());
                         response.setUserCode(user.getUserCode());
                         response.setOrganizationId(user.getOrganizationId());

                         if (user.getPersonalInfo() != null) {
                              response.setDocumentType(user.getPersonalInfo().getDocumentType());
                              response.setDocumentNumber(user.getPersonalInfo().getDocumentNumber());
                              response.setFirstName(user.getPersonalInfo().getFirstName());
                              response.setLastName(user.getPersonalInfo().getLastName());
                              response.setFullName(user.getPersonalInfo().getFullName());
                         }

                         if (user.getContact() != null) {
                              response.setEmail(user.getContact().getEmail());
                              response.setPhone(user.getContact().getPhone());

                              if (user.getContact().getAddress() != null) {
                                   response.setStreetAddress(user.getContact().getAddress().getStreetAddress());
                                   response.setStreetId(user.getContact().getAddress().getStreetId());
                                   response.setZoneId(user.getContact().getAddress().getZoneId());
                              }
                         }

                         response.setStatus(user.getStatus());
                         response.setRegistrationDate(user.getRegistrationDate());
                         response.setLastLogin(user.getLastLogin());
                         response.setCreatedAt(user.getCreatedAt());
                         response.setUpdatedAt(user.getUpdatedAt());

                         response.setRoles(new ArrayList<>());
                         response.setUsername("N/A");

                         response.setWaterBoxes(generateMockWaterBoxes(user.getOrganizationId(), user.getId()));

                         return response;
                    }));
     }

     private List<UserWithWaterBoxesResponse.WaterBoxInfo> generateMockWaterBoxes(String organizationId,
               String userId) {
          List<UserWithWaterBoxesResponse.WaterBoxInfo> waterBoxes = new ArrayList<>();

          int waterBoxCount = (userId.hashCode() % 3) + 1;

          for (int i = 1; i <= waterBoxCount; i++) {
               UserWithWaterBoxesResponse.WaterBoxInfo waterBox = new UserWithWaterBoxesResponse.WaterBoxInfo();

               waterBox.setWaterBoxId("WB-" + organizationId + "-" + userId.substring(userId.length() - 4) + "-"
                         + String.format("%02d", i));
               waterBox.setWaterBoxCode("CAJA-" + String.format("%05d", (userId.hashCode() + i) % 99999));
               waterBox.setWaterBoxName("Caja N°" + i);
               waterBox.setLocation("Zona " + organizationId + " - Sector " + i);
               waterBox.setAssignmentStatus(
                         pe.edu.vallegrande.vgmsusersauthentication.domain.enums.AssignmentStatus.ACTIVE);
               waterBox.setMonthlyFee(new java.math.BigDecimal("20.00"));
               waterBox.setAssignmentDate(LocalDateTime.now().minusMonths(i * 2));

               waterBoxes.add(waterBox);
          }
          return waterBoxes;
     }

     private User buildUserFromCompleteUpdateRequest(UserCompleteUpdateRequest request, String userCode) {
          User user = new User();
          user.setUserCode(userCode);
          user.setOrganizationId(request.getOrganizationId());

          PersonalInfo personalInfo = new PersonalInfo();
          personalInfo.setDocumentType(request.getDocumentType());
          personalInfo.setDocumentNumber(request.getDocumentNumber());
          personalInfo.setFirstName(request.getFirstName());
          personalInfo.setLastName(request.getLastName());
          personalInfo.setFullName(request.getFirstName() + " " + request.getLastName());
          user.setPersonalInfo(personalInfo);

          Contact contact = new Contact();
          contact.setEmail(request.getEmail());
          contact.setPhone(request.getPhone());
          AddressUsers address = new AddressUsers();
          address.setStreetAddress(request.getStreetAddress());
          address.setStreetId(request.getStreetId());
          address.setZoneId(request.getZoneId());
          contact.setAddress(address);

          user.setContact(contact);
          user.setWaterBoxes(new ArrayList<>());
          user.setStatus(StatusUsers.ACTIVE);
          user.setRegistrationDate(LocalDateTime.now());
          user.setCreatedAt(LocalDateTime.now());
          user.setUpdatedAt(LocalDateTime.now());

          return user;
     }

     private Mono<Void> updateUserRoles(String userId, List<RolesUsers> roles) {
          return authRepository.findByUserId(userId)
                    .flatMap(authCredential -> {
                         authCredential.setRoles(roles);
                         return authRepository.save(authCredential);
                    })
                    .then();
     }
}

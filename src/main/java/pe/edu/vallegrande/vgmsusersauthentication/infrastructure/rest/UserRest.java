package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsusersauthentication.application.service.UserService;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCreateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCompleteUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserWithWaterBoxesResponse;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRest {

     private final UserService userService;

     @PostMapping
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> createUser(@Valid @RequestBody UserCreateRequest request) {
          log.info("Creando nuevo usuario con email: {}", request.getEmail());

          return userService.createUser(request)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                              .body(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error creando usuario: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_CREATION_ERROR",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @GetMapping("/all")
     public Mono<ResponseEntity<ResponseDto<List<UserResponse>>>> getAllUsers() {
          log.info("Obteniendo todos los usuarios");

          return userService.getAllUsers()
                    .collectList()
                    .map(userList -> ResponseEntity.ok(new ResponseDto<>(true, userList)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuarios: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_RETRIEVAL_ERROR",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }

     @GetMapping("/{userId}")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> getUserById(@PathVariable String userId) {
          log.info("Getting user by ID: {}", userId);

          return userService.getUserById(userId)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error getting user by ID: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/email/{email}")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> getUserByEmail(@PathVariable String email) {
          log.info("Getting user by email: {}", email);

          return userService.getUserByEmail(email)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error getting user by email: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/document/{documentNumber}")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> getUserByDocumentNumber(
               @PathVariable String documentNumber) {
          log.info("Getting user by document number: {}", documentNumber);

          return userService.getUserByDocumentNumber(documentNumber)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error getting user by document: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/role/{role}")
     public Mono<ResponseEntity<ResponseDto<List<UserResponse>>>> getUsersByRole(@PathVariable RolesUsers role) {
          log.info("Getting users by role: {}", role);

          return userService.getUsersByRole(role)
                    .collectList()
                    .map(userList -> ResponseEntity.ok(new ResponseDto<>(true, userList)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuarios por rol: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_RETRIEVAL_ERROR",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }

     @GetMapping("/status/{status}")
     public Mono<ResponseEntity<ResponseDto<List<UserResponse>>>> getUsersByStatus(@PathVariable StatusUsers status) {
          log.info("Getting users by status: {}", status);

          return userService.getUsersByStatus(status)
                    .collectList()
                    .map(userList -> ResponseEntity.ok(new ResponseDto<>(true, userList)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuarios por estado: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_RETRIEVAL_ERROR",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }

     @GetMapping("/organization/{organizationId}")
     public Mono<ResponseEntity<ResponseDto<List<UserResponse>>>> getUsersByOrganization(
               @PathVariable String organizationId) {
          log.info("Getting users by organization: {}", organizationId);

          return userService.getUsersByOrganization(organizationId)
                    .collectList()
                    .map(userList -> ResponseEntity.ok(new ResponseDto<>(true, userList)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuarios por organización: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_RETRIEVAL_ERROR",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }

     @PutMapping("/{userId}")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> updateUserCompletely(
               @PathVariable String userId,
               @Valid @RequestBody UserCompleteUpdateRequest request) {
          log.info("Updating user completely with ID: {}", userId);

          return userService.updateUserCompletely(userId, request)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error updating user completely: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_UPDATE_ERROR",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @PatchMapping("/{userId}")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> updateUserPartially(
               @PathVariable String userId,
               @Valid @RequestBody UserUpdateRequest request) {
          log.info("Updating user partially with ID: {}", userId);

          return userService.updateUserPartially(userId, request)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error updating user partially: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_UPDATE_ERROR",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @DeleteMapping("/{userId}")
     public Mono<ResponseEntity<ResponseDto<String>>> deleteUser(@PathVariable String userId) {
          log.info("Eliminando usuario lógicamente con ID: {}", userId);

          return userService.deleteUserLogically(userId)
                    .then(Mono.just(ResponseEntity.ok(ResponseDto.success("Usuario eliminado correctamente"))))
                    .onErrorResume(error -> {
                         log.error("Error eliminando usuario: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<String>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_DELETE_ERROR",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @PostMapping("/{userId}/restore")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> restoreUser(@PathVariable String userId) {
          log.info("Restoring user logically with ID: {}", userId);

          return userService.restoreUserLogically(userId)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error restoring user: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_RESTORE_ERROR",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @GetMapping("/exists/email/{email}")
     public Mono<ResponseEntity<ResponseDto<Boolean>>> checkEmailExists(@PathVariable String email) {
          log.info("Checking if email exists: {}", email);

          return userService.existsByEmail(email)
                    .map(exists -> ResponseEntity.ok(new ResponseDto<>(true, exists)));
     }

     @GetMapping("/with-waterboxes")
     public Mono<ResponseEntity<ResponseDto<List<UserWithWaterBoxesResponse>>>> getAllUsersWithWaterBoxes() {
          log.info("Obteniendo todos los usuarios con información de water boxes");

          return userService.getAllUsersWithWaterBoxes()
                    .collectList()
                    .map(users -> ResponseEntity.ok(new ResponseDto<>(true, users)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuarios con water boxes: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<List<UserWithWaterBoxesResponse>>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       "Error interno del servidor: " + error.getMessage(),
                                                       "USERS_WITH_WATERBOXES_ERROR",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }

     @GetMapping("/{userId}/with-waterboxes")
     public Mono<ResponseEntity<ResponseDto<UserWithWaterBoxesResponse>>> getUserWithWaterBoxesById(
               @PathVariable String userId) {
          log.info("Obteniendo usuario con ID: {} con información de water boxes", userId);

          return userService.getUserWithWaterBoxesById(userId)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error obteniendo usuario con water boxes: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserWithWaterBoxesResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_WITH_WATERBOXES_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/exists/document/{documentNumber}")
     public Mono<ResponseEntity<ResponseDto<Boolean>>> checkDocumentExists(@PathVariable String documentNumber) {
          log.info("Checking if document exists: {}", documentNumber);

          return userService.existsByDocumentNumber(documentNumber)
                    .map(exists -> ResponseEntity.ok(new ResponseDto<>(true, exists)));
     }

     @GetMapping("/my-profile")
     public Mono<ResponseEntity<ResponseDto<UserResponse>>> getMyProfile(@RequestHeader("X-User-Id") String userId) {
          log.info("Getting profile for authenticated user: {}", userId);

          return userService.getUserById(userId)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error getting user profile: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_PROFILE_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/my-profile/with-waterboxes")
     public Mono<ResponseEntity<ResponseDto<UserWithWaterBoxesResponse>>> getMyProfileWithWaterBoxes(
               @RequestHeader("X-User-Id") String userId) {
          log.info("Getting profile with water boxes for authenticated user: {}", userId);

          return userService.getUserWithWaterBoxesById(userId)
                    .map(user -> ResponseEntity.ok(new ResponseDto<>(true, user)))
                    .onErrorResume(error -> {
                         log.error("Error getting user profile with water boxes: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<UserWithWaterBoxesResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_PROFILE_WITH_WATERBOXES_NOT_FOUND",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }
}

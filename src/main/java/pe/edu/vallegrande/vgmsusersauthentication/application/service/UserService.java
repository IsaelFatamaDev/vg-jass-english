package pe.edu.vallegrande.vgmsusersauthentication.application.service;

import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCreateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.UserCompleteUpdateRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.UserWithWaterBoxesResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

     Mono<UserResponse> createUser(UserCreateRequest request);

     Mono<UserResponse> updateUserPartially(String userId, UserUpdateRequest request);

     Mono<UserResponse> updateUserCompletely(String userId, UserCompleteUpdateRequest request);

     Mono<Void> deleteUserLogically(String userId);

     Mono<UserResponse> restoreUserLogically(String userId);

     Flux<UserResponse> getAllUsers();

     Mono<UserResponse> getUserById(String userId);

     Mono<UserResponse> getUserByEmail(String email);

     Mono<UserResponse> getUserByDocumentNumber(String documentNumber);

     Flux<UserResponse> getUsersByRole(RolesUsers role);

     Flux<UserResponse> getUsersByStatus(StatusUsers status);

     Flux<UserResponse> getUsersByOrganization(String organizationId);

     Flux<UserWithWaterBoxesResponse> getAllUsersWithWaterBoxes();

     Mono<UserWithWaterBoxesResponse> getUserWithWaterBoxesById(String userId);

     Mono<Boolean> existsByEmail(String email);

     Mono<Boolean> existsByDocumentNumber(String documentNumber);

     Mono<Boolean> existsByUserCode(String userCode);
}

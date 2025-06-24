package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.model.AuthCredential;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthRepository extends ReactiveMongoRepository<AuthCredential, String> {

     Mono<AuthCredential> findByUserId(String userId);

     Mono<AuthCredential> findByUsername(String username);

     Mono<AuthCredential> findByUsernameAndStatus(String username, StatusUsers status);

     Flux<AuthCredential> findByRolesContaining(RolesUsers role);

     Mono<Boolean> existsByUsername(String username);

     Mono<Void> deleteByUserId(String userId);
}

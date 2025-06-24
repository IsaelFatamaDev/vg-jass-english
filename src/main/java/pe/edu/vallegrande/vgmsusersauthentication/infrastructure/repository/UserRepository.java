package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

     @Query("{'contact.email': ?0}")
     Mono<User> findByEmail(String email);

     @Query("{'personalInfo.documentNumber': ?0}")
     Mono<User> findByDocumentNumber(String documentNumber);

     Mono<User> findByUserCode(String userCode);

     Flux<User> findByOrganizationId(String organizationId);

     Flux<User> findByStatus(StatusUsers status);

     Flux<User> findByOrganizationIdAndStatus(String organizationId, StatusUsers status);

     @Query(value = "{'contact.email': ?0}", exists = true)
     Mono<Boolean> existsByEmail(String email);

     @Query(value = "{'personalInfo.documentNumber': ?0}", exists = true)
     Mono<Boolean> existsByDocumentNumber(String documentNumber);

     Mono<Boolean> existsByUserCode(String userCode);
}

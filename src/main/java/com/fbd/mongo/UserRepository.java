package com.fbd.mongo;

import com.fbd.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "fbd_users", path = "fbd_users")
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserNameAndPassword(String userName, String password);
    Boolean existsByUserName(String userName);
}

package com.fbd.mongo;

import com.fbd.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_users", path = "fbd_users")
public interface MongoUserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findAllByGender(String gender);
}

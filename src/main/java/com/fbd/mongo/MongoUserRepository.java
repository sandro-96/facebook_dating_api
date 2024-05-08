package com.fbd.mongo;

import com.fbd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_users", path = "fbd_users")
public interface MongoUserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    @Query("{ 'gender' : ?0 , 'id' : { $nin : ?1 } }")
    Page<User> findAllByGenderAndIdNotIn(String gender, List<String> notIncludeUsers, Pageable pageable);

    @Query("{ 'id' : { $nin : ?0 } }")
    Page<User> findAllByIdNotIn(List<String> notIncludeUsers, Pageable pageable);
}
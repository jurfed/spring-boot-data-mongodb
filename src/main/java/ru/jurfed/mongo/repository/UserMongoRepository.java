package ru.jurfed.mongo.repository;


import ru.jurfed.mongo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMongoRepository extends MongoRepository<User, Integer> {

    User findByName(String name);

//    @Query("select u from User u where u.name =:inputName")
    @Query("{ 'name': {$regex: ?0}}")
    List<User> customFind(@Param("inputName") String name);

/*    @Query("select u from users u")
    List<User> customFind2();*/
}

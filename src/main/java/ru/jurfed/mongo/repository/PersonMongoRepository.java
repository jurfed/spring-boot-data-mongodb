package ru.jurfed.mongo.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.jurfed.mongo.domain.Email;
import ru.jurfed.mongo.domain.Person;
import ru.jurfed.mongo.domain.User;

import java.util.List;

public interface PersonMongoRepository extends MongoRepository<Person, Integer> {

    Person findByName(String name);


}

package ru.jurfed.mongo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.query.Update;
import ru.jurfed.mongo.domain.Address;
import ru.jurfed.mongo.domain.Email;
import ru.jurfed.mongo.domain.Person;
import ru.jurfed.mongo.domain.User;
import ru.jurfed.mongo.repository.PersonMongoRepository;
import ru.jurfed.mongo.repository.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@EnableMongoRepositories(basePackages = "ru.jurfed.mongo")//write it in the Configuration class
@SpringBootApplication
public class MongoApplication {

    @Autowired
    private UserMongoRepository userMongoRepository;

    @Autowired
    private PersonMongoRepository personMongoRepository;

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }
    private static final Log log = LogFactory.getLog(MongoApplication.class);

    @Autowired
    MongoOperations mongoOperations;

    @PostConstruct
    public void init() {
        User user = new User("create bd", 23);

        this.userMongoRepository.deleteAll();

        User user1 = new User("Bob", 38);
        User user2 = new User("Alice", 23);

        this.userMongoRepository.save(user1);
        this.userMongoRepository.save(user2);

        System.err.println("--------------------mongorep");
        List<User> users = userMongoRepository.findAll();
        users.forEach(System.err::println);

        System.err.println("--------------------query");
        List<User> users2 = userMongoRepository.customFind("Alice");
        users2.forEach(System.err::println);

        System.err.println("--------------------mango operations");
        mongoOperations.save(new User("Mihail", 56));
        mongoOperations.findAll(User.class).forEach(System.err::println);

        System.err.println("--------------------mango criteria");
        User user3 = mongoOperations.findOne(query(where("age").is(56)), User.class);
        System.err.println(user3);

        List<User> user4 = mongoOperations.find(query(where("age").is(23)), User.class);
        user4.forEach(System.err::println);

        System.err.println("\n------------------------------------------Persons------------------------------------------");
        this.personMongoRepository.deleteAll();

        Person person = new Person("Bob", "Ivanov");
        Address address = new Address("Krasnodarskiy kray 34");
        person.setAddress(address);
        Email email1 = new Email("mail1@yandex.ru");
        Email email2 = new Email("mail2@yandex.ru");
        Email email3 = new Email("mail@inbox.ru");
        person.setEmails(Arrays.asList(email1, email2, email3));
        this.personMongoRepository.save(person);

        Person person2 = new Person("Alice", "Silezneva");
        Address address2 = new Address("St Petersburg 34");
        person2.setAddress(address2);
        Email email11 = new Email("mail1@google.com");
        Email email12 = new Email("mail2@google.com");
        Email email13 = new Email("mail@inbox.ru");
        person2.setEmails(Arrays.asList(email11, email12, email13));
        this.personMongoRepository.save(person2);

        List<Person> persons = this.personMongoRepository.findAll();
        persons.forEach(System.err::println);

        log.info("\n----------------------------change emails");
        Person findPerson = this.personMongoRepository.findByName("Alice");
        findPerson.getEmails().forEach(email -> {
            email.setEmail(email.getEmail() + "_new");
        });
        this.personMongoRepository.save(findPerson);
        log.info(this.personMongoRepository.findByName("Alice"));


        log.info("\n-------------------------mongoOperations");
        mongoOperations.updateFirst(query(where("name").is("Alice")), Update.update("surname","Bubkina"), Person.class);
        log.info(mongoOperations.findOne(query(where("name").is("Alice")),Person.class));
    }

}

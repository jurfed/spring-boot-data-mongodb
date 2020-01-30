package ru.jurfed.mongo;

import ru.jurfed.mongo.domain.User;
import ru.jurfed.mongo.repository.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@EnableMongoRepositories(basePackages = "ru.jurfed.mongo")//write it in the Configuration class
@SpringBootApplication
public class MongoApplication {

    @Autowired
    private UserMongoRepository userMongoRepository;

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);

    }


/*	@Bean
	public MongoOperations keyValueTemplate(){
		return new KeyValueTemplate(keyValueAdapter());
	}

	@Bean
	public KeyValueAdapter keyValueAdapter(){
		return new MongoOperations(WeakHashMap.class);
	}*/


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
    }

}
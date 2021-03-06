package com.testcricket.cricketdata.People;

import com.testcricket.cricketdata.People.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

    Person findByIdentifier(String identifier);

}

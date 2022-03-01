package com.testcricket.cricketdata.People;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
    boolean existsByName(String name);
    Team findByName(String name);
}

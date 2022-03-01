package com.testcricket.cricketdata.People;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfficialRepository extends MongoRepository<Official, String> {
    Official findByIdentifier(String identifier);
}

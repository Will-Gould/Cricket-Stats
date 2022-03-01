package com.testcricket.cricketdata.People;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, String> {

    Player findPlayerByIdentifier(String identifier);

}

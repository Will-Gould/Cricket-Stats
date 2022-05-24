package com.testcricket.cricketdata.Team;

import com.testcricket.cricketdata.Team.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends MongoRepository<Team, String> {
    boolean existsByName(String name);
    Team findByName(String name);
    Team findTeamById(String id);
}

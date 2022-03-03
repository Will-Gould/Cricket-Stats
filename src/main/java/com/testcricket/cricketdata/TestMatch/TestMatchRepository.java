package com.testcricket.cricketdata.TestMatch;

import com.testcricket.cricketdata.TestMatch.TestMatch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TestMatchRepository extends MongoRepository<TestMatch, String> {

    @Query("{ 'teams.players': ?0 }")
    public List<TestMatch> fetchFeaturedTestMatches(String id);

}

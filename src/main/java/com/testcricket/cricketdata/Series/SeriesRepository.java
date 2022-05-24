package com.testcricket.cricketdata.Series;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

public interface SeriesRepository extends MongoRepository<Series, String> {

    Series findByNameAndSeason(String name, String season);
    boolean existsByNameAndSeason(String name, String season);

    @Query("{ name: ?0, season:  ?1, push: {matches: ?2}}")
    void addMatchesByNameAndSeason(
            String name,
            String season,
            String matchId
    );

}

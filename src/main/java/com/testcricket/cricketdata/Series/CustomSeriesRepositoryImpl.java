package com.testcricket.cricketdata.Series;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CustomSeriesRepositoryImpl implements CustomSeriesRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void addMatchesByNameAndSeason(String name, String season, String matchId) {
        Query q = new Query();
        q.addCriteria(Criteria.where("name").is(name).and("season").is(season));
        Update u = new Update();
        u.push("matches", matchId);

        UpdateResult result = mongoTemplate.updateFirst(q, u, Series.class);

    }
}

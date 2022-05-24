package com.testcricket.cricketdata.Series;

public interface CustomSeriesRepository {

    void addMatchesByNameAndSeason(
            String name,
            String season,
            String matchId
    );

}

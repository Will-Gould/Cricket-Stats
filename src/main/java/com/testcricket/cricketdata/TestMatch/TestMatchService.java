package com.testcricket.cricketdata.TestMatch;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TestMatchService {

    private final TestMatchRepository testMatchRepository;

    public List<TestMatch> getAllTestMatches(){
        return testMatchRepository.findAll();
    }

    public Optional<TestMatch> getMatchById(String matchId){
        return testMatchRepository.findById(matchId);
    }

}

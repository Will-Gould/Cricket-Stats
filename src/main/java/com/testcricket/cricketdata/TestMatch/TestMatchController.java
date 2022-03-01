package com.testcricket.cricketdata.TestMatch;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/testmatches")
@AllArgsConstructor
public class TestMatchController {

    private final TestMatchService testMatchService;

    @GetMapping
    public List<TestMatch> fetchAllTestMatches(){
        return testMatchService.getAllTestMatches();
    }

    @GetMapping(path = "{matchId}")
    public Optional<TestMatch> fetchTestMatchById(@PathVariable String matchId){
        return testMatchService.getMatchById(matchId);
    }

}

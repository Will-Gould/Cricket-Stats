package com.testcricket.cricketdata.TestMatch;

import com.testcricket.cricketdata.Util.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("api/testmatches")
@AllArgsConstructor
public class TestMatchController {

    private final TestMatchService testMatchService;

    @GetMapping
    public ResponseEntity<Response> fetchAllTestMatches(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("Test Matches", testMatchService.getAllTestMatches()))
                        .message("Test matches retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
        //return testMatchService.getAllTestMatches();
    }

    @GetMapping(path = "{matchId}")
    public Optional<TestMatch> fetchTestMatchById(@PathVariable String matchId){
        return testMatchService.getMatchById(matchId);
    }

}

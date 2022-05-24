package com.testcricket.cricketdata.admin;

import com.testcricket.cricketdata.TestMatch.TestMatchService;
import com.testcricket.cricketdata.filestore.FileStore;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {

    private final TestMatchService testMatchService;
    private final FileStore fileStore;

    @GetMapping("update_tests")
    public void updateTestMatches(){
        List<String> bucketFiles = fileStore.listFiles();
        List<String> newTests = new ArrayList<>();

        for(String s : bucketFiles){
            String[] tokens = s.split("[.]");
            String fileId = tokens[0];
            if(!testMatchService.existsByFileId(fileId)){
                newTests.add(s);
            }
        }

        fileStore.addNewTestMatches(newTests);

    }

}

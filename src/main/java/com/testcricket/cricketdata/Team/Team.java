package com.testcricket.cricketdata.Team;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
@Data
public class Team {

    @Id
    private String id;
    private String name;

    public Team(String name) {
        this.name = name;
    }

}

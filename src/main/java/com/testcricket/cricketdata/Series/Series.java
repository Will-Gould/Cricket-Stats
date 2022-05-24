package com.testcricket.cricketdata.Series;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document
@Data
@NoArgsConstructor
public class Series {

    @Id
    private String id;
    private String name;
    private String season;
    private List<String> matches;

    public Series(String name, String season){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.season = season;
        this.matches = new ArrayList<>();
    }

    public Series(String name, String season, List<String> matches){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.season = season;
        this.matches = matches;
    }

}

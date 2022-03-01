package com.testcricket.cricketdata.People;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
public class Person {

    @Id
    private String id;
    private String identifier;
    private String uniqueName;
    private String name;
    private String cricInfoKey;

    public Person(String identifier, String uniqueName, String name) {
        this.id = UUID.randomUUID().toString();
        this.identifier = identifier;
        this.uniqueName = uniqueName;
        this.name = name;
    }
}

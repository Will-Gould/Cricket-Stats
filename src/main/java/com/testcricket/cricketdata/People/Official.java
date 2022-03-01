package com.testcricket.cricketdata.People;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Official extends Person{
    public Official(String identifier, String uniqueName, String name) {
        super(identifier, uniqueName, name);
    }
}

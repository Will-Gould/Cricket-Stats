package com.testcricket.cricketdata.TestMatch.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Wicket {

    private String outgoingBatter;
    private List<String> fielders;
    private Dismissal type;

    public Wicket(String outgoingBatter, List<String> fielders, Dismissal type) {
        this.outgoingBatter = outgoingBatter;
        this.fielders = fielders;
        this.type = type;
    }

    public Wicket(String outgoingBatter, Dismissal type) {
        this.outgoingBatter = outgoingBatter;
        this.type = type;
    }
}

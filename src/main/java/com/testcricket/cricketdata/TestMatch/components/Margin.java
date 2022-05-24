package com.testcricket.cricketdata.TestMatch.components;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Margin {

    private MarginType type;
    private int margin;
    private int innings;

    public Margin() {
        this.type = MarginType.RUNS;
        this.margin = 0;
        this.innings = 0;
    }

    public Margin(MarginType type, int margin){
        this.type = type;
        this.margin = margin;
    }

    public Margin(MarginType type, int margin, int innings) {
        this.type = type;
        this.margin = margin;
        this.innings = innings;
    }
}

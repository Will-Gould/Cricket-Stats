package com.testcricket.cricketdata.TestMatch;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Over {

    private int overNumber;
    private List<Delivery> deliveries;

    public Over(int overNumber, List<Delivery> deliveries) {
        this.overNumber = overNumber;
        this.deliveries = deliveries;
    }
}

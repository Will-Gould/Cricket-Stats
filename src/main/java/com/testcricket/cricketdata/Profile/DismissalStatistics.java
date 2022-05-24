package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.TestMatch.components.Dismissal;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DismissalStatistics {

    private HashMap<Dismissal, Integer> dismissalCount;
    private HashMap<String, Integer> mostDismissedBy;

    public DismissalStatistics(HashMap<Dismissal, Integer> dismissalCount, HashMap<String, Integer> mostDismissedBy){
        this.dismissalCount = dismissalCount;
        this.mostDismissedBy = mostDismissedBy;
    }

}

package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.People.Player;
import com.testcricket.cricketdata.People.PlayerRepository;
import com.testcricket.cricketdata.TestMatch.Dismissal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

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

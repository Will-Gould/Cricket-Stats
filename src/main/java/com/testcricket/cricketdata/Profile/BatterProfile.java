package com.testcricket.cricketdata.Profile;

import com.testcricket.cricketdata.People.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatterProfile extends PlayerProfile{

    private VersusBowlerStatistics versusBowlerStatistics;
    private DismissalStatistics dismissalStatistics;

    public BatterProfile(Player player, Role roleFocus, List<BattingInnings> battingInnings, VersusBowlerStatistics versusBowlerStatistics, DismissalStatistics dismissalStatistics) {
        super(player, roleFocus, battingInnings);
        this.versusBowlerStatistics = versusBowlerStatistics;
        this.dismissalStatistics = dismissalStatistics;
    }

}

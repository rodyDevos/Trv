
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class PlayerStats {

    private List<PlayerStatsDataObject> playerstats = new ArrayList<PlayerStatsDataObject>();

    public List<PlayerStatsDataObject> getPlayerStatistics() {
        return playerstats;
    }

    @Override
    public String toString() {

        return "getPlayerStatistics: " + playerstats;

    }

}

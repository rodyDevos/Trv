
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class GameStatistics {

    private List<GameStatisticsDataObject> game_players = new ArrayList<GameStatisticsDataObject>();

    public List<GameStatisticsDataObject> getGameStatistics() {
        return game_players;
    }

    @Override
    public String toString() {

        return "getGameStatistics: " + game_players;

    }

}

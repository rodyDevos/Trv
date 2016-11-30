
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class GamesDetail {

    private List<GamesDataObject> games = new ArrayList<GamesDataObject>();

    public List<GamesDataObject> getGames() {
        return games;
    }

    @Override
    public String toString() {
        return "" + games;
    }

}

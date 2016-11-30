
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class GetPlayers {

    private List<PlayersDataObject> favorites = new ArrayList<PlayersDataObject>();
    private List<PlayersDataObject> blocked = new ArrayList<PlayersDataObject>();
    private List<PlayersDataObject> random = new ArrayList<PlayersDataObject>();

    public List<PlayersDataObject> getFavorites() {
        return favorites;
    }

    public List<PlayersDataObject> getBlocked() {
        return blocked;
    }

    public List<PlayersDataObject> getRandom() {
        return random;
    }

    @Override
    public String toString() {

        if (!favorites.isEmpty())
            return "fav: " + favorites;
        else if (!blocked.isEmpty())
            return "blocked: " + blocked;
        else if (!random.isEmpty())
            return "random: " + random;

        return null;
    }

}

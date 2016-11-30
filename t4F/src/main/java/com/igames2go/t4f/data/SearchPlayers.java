
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class SearchPlayers {

    private List<PlayersDataObject> search = new ArrayList<PlayersDataObject>();

    public List<PlayersDataObject> getSearchPlayers() {
        return search;
    }

    @Override
    public String toString() {

        return "Search: " + search;

    }

}


package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    private List<MessagesDataObject> gamemessages = new ArrayList<MessagesDataObject>();

    public List<MessagesDataObject> getGameMessages() {
        return gamemessages;
    }

    @Override
    public String toString() {

        return "getGameMessages: " + gamemessages;

    }

}

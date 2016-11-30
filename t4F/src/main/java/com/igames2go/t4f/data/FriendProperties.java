
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class FriendProperties {

    private List<FriendsDataObject> getFriendProperties = new ArrayList<FriendsDataObject>();

    public List<FriendsDataObject> getFriendProperties() {
        return getFriendProperties;
    }

    @Override
    public String toString() {
        return "" + getFriendProperties;
    }

}

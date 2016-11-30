
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class FacebookFriendsList {

    private List<FacebookFriendsDataObject> data = new ArrayList<FacebookFriendsDataObject>();

    // private FacebookFriendsNext paging = new FacebookFriendsNext();

    public List<FacebookFriendsDataObject> getFacebookFriends() {
        return data;
    }
    public void setFacebookFriends(List<FacebookFriendsDataObject> data) {
        this.data = data;
    }
    /*
     * public FacebookFriendsNext getPagingUrls() { return paging; }
     */

    @Override
    public String toString() {
        return "\n" + data;
    }

}

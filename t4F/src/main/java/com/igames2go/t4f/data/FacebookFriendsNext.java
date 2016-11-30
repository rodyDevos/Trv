
package com.igames2go.t4f.data;

public class FacebookFriendsNext {
    private String next;
    private String previous;

    public String getNextPageUrl() {
        return next;
    }

    public String getPreviousPageUrl() {
        return previous;
    }

    @Override
    public String toString() {
        return "\nnext url:- " + next + "\nprevious url:-  " + previous;
    }
}


package com.igames2go.t4f.data;

import android.util.Log;

public class PlayersDataObject {

    private String pla_fb_id;
    private String pla_fb_name;
    private String pla_id;
    private String plaapp_id;
    private String favorite;
    private String blocked;
    private String pla_location;
    private String award_badge_image;

    public String getPla_fb_id() {
        return pla_fb_id;
    }

    public String getPla_fb_name() {
        return pla_fb_name;
    }

    public String getPlaapp_id() {
        return plaapp_id;
    }

    public String getPla_id() {
        return pla_id;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getBlocked() {
        return blocked;
    }

    public String getPla_location() {
        return pla_location;
    }

    public String getAward_badge_image() {
        return award_badge_image;
    }

    @Override
    public String toString() {
        return "GetPlayersDataObject: \npla_fb_id: " + pla_fb_id
                + "\npla_fb_name: " + pla_fb_name + "\npla_id: " + pla_id
                + "\nplaapp_id: " + plaapp_id + "\nfavorite: " + favorite
                + "\nblocked: " + blocked + "\npla_location: " + pla_location
                + "\naward_badge_image: " + award_badge_image;
    }

    public FriendsDataObject creatFriendDataObject() {
        FriendsDataObject obj = new FriendsDataObject(pla_fb_id, pla_fb_name,
                pla_id, plaapp_id, favorite, blocked, award_badge_image);
        Log.i("PlayerDataObject", obj.toString());
        return obj;
    }

}

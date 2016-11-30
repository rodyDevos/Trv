
package com.igames2go.t4f.data;

import android.util.Log;

public class FacebookFriendsDataObject {

    String name;
    String uid;
    String pla_id;
    String plaapp_id;
    String favorite;
    String blocked;
    String award_badge_image;
    public boolean isUpdated = false;

    public void setId(String uid){
    	this.uid = uid;
    }
    public String getId() {
        return uid;
    }

    public void setName(String name){
    	this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getPla_id() {
        return pla_id;
    }

    public String getPlaapp_id() {
        return plaapp_id;
    }

    public String getFavorite() {
        return favorite+"";
    }

    public String getBlocked() {
        return blocked;
    }

    public String getAward_badge_image() {
        return award_badge_image;
    }

    public void setPla_id(String pla_id) {
        this.pla_id = pla_id;
    }

    public void setPlaapp_id(String plaapp_id) {
        this.plaapp_id = plaapp_id;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public void setAward_badge_image(String award_badge_image) {
        this.award_badge_image = award_badge_image;
    }

    @Override
    public String toString() {
        return "\nid: " + uid + "   ::   name: " + name + " fav:"+favorite + " blocked"+blocked+" award:"+award_badge_image;
    }

    public FriendsDataObject creatFriendDataObject() {
        FriendsDataObject obj = new FriendsDataObject(uid, name,
                pla_id, plaapp_id, favorite, blocked, award_badge_image);
        Log.i("FacebookFriendsDataObject", obj.toString());
        return obj;
    }
}

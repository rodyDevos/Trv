
package com.igames2go.t4f.data;


public class FriendsDataObject {

    private String pla_fb_id;
    private String pla_fb_name;
    private String pla_id;
    private String plaapp_id;
    private String favorite;
    private String blocked;
    private String award_badge_image;
  
    public FriendsDataObject(String pla_fb_id, String pla_fb_name,
            String pla_id, String plaapp_id, String favorite, String blocked,
            String award_badge_image) {
        super();
        this.pla_fb_id = pla_fb_id;
        this.pla_fb_name = pla_fb_name;
        this.pla_id = pla_id;
        this.plaapp_id = plaapp_id;
        this.favorite = favorite;
        this.blocked = blocked;
        this.award_badge_image = award_badge_image;
    }

   
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

    public String getAward_badge_image() {
        return award_badge_image;
    }

    @Override
    public String toString() {
        return "GetFriendPropertiesDataObject: \npla_fb_id: " + pla_fb_id
                + "\npla_fb_name: " + pla_fb_name + "\npla_id: " + pla_id
                + "\nplaapp_id: " + plaapp_id + "\nfavorite: " + favorite
                + "\nblocked: " + blocked + "\naward_badge_image: "
                + award_badge_image;
    }

}

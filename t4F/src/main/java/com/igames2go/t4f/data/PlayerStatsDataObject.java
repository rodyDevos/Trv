
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsDataObject {

    private String plaid;
    private String fbid;
    private String fbname;
    private int gameswon;
    private int gamesplayed;
    private String tier;
    private String level;
    private String award_badge_image;
    private int level_points_so_far;
    private int level_points_to_go;
    private String ranking;
    
    private List<PlayerStats_CategoriesData> categories = new ArrayList<PlayerStats_CategoriesData>();

    public String getPlaid() {
        return plaid;
    }

    public String getFbid() {
        return fbid;
    }

    public String getFbname() {
        return fbname;
    }

    public int getGameswon() {
        return gameswon;
    }

    public int getGamesplayed() {
        return gamesplayed;
    }

    public String getTier() {
        return tier;
    }

    public String getLevel() {
        return level;
    }

    public String getRanking() {
    	return ranking;
    }
    public String getAward_badge_image() {
        return award_badge_image;
    }

    public int getLevel_points_so_far() {
        return level_points_so_far;
    }

    public int getLevel_points_to_go() {
        return level_points_to_go;
    }

    public List<PlayerStats_CategoriesData> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "PlayerStatsDataObject: \nplaid: " + plaid + "\nfbid: " + fbid
                + "\nfbname: " + fbname + "\ngameswon: " + gameswon
                + "\ngamesplayed: " + gamesplayed + "\ntier: " + tier
                + "\nlevel: " + level + "\naward_badge_image: "
                + award_badge_image + "\nlevel_points_so_far: "
                + level_points_so_far + "\nlevel_points_to_go: "
                + level_points_to_go + "\ncategories: " + categories.toString();
    }

}

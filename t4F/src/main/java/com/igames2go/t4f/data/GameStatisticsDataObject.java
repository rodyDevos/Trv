
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class GameStatisticsDataObject {

    private String plaid;
    private String fbid;
    private String fbname;
    private String shortname;
    private String gamplaright;
    private String gamplaasked;
    private String gamplaid;
    private String status;
    private String turnorder;
    private String lifelines;
    private String playdatetime;
    private String place;
    private String gampla_point_tot;
    private String chat_number;
    private String award_badge_image;
    private List<GameStatistics_CategoriesData> categories = new ArrayList<GameStatistics_CategoriesData>();

    public String getPlaid() {
        return plaid;
    }

    public String getFbid() {
        return fbid;
    }

    public String getFbname() {
        return fbname;
    }

    public String getShortname() {
        return shortname;
    }

    public String getGamplaright() {
        return gamplaright;
    }

    public String getGamplaasked() {
        return gamplaasked;
    }

    public String getGamplaid() {
        return gamplaid;
    }

    public String getStatus() {
        return status;
    }

    public String getTurnorder() {
        return turnorder;
    }

    public String getLifelines() {
        return lifelines;
    }

    public String getPlaydatetime() {
        return playdatetime;
    }

    public String getPlace() {
        return place;
    }

    public String getGampla_point_tot() {
        return gampla_point_tot;
    }

    public String getChat_number() {
        return chat_number;
    }

    public String getAward_badge_image() {
        return award_badge_image;
    }

    public List<GameStatistics_CategoriesData> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "GameStatisticsDataObject: \nplaid: " + plaid + "\nfbid: "
                + fbid + "\nfbname: " + fbname + "\nshortname: " + shortname
                + "\ngamplaright: " + gamplaright + "\ngamplaasked: "
                + gamplaasked + "\ngamplaid: " + gamplaid + "\nstatus: "
                + status + "\nturnorder: " + turnorder + "\nlifelines: "
                + lifelines + "\nplaydatetime: " + playdatetime + "\nplace: "
                + place + "\ngampla_point_tot: " + gampla_point_tot
                + "\nchat_number: " + chat_number + "\naward_badge_image: "
                + award_badge_image + "\ncategories: " + categories.toString();
    }

}

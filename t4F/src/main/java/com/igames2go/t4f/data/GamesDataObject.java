
package com.igames2go.t4f.data;

import java.io.Serializable;

public class GamesDataObject implements Serializable {

    private String gam_id;
    private String pla_fb_id;
    private String pla_fb_name;
    private String gampla_status;
    private String gampla_play_datetime;
    private String gampla_lifelines;
    private String gampla_turn_order;
    private String gampla_id;
    private String place;
    private String gam_status;
    private String gam_info1;
    private String gam_info2;
    private String gampla_point_tot;
    private String award_badge_image;
    private String plaid;
    public String getGam_id() {
        return gam_id;
    }

    public String getPla_fb_id() {
        return pla_fb_id;
    }

    public String getPla_fb_name() {
        return pla_fb_name;
    }

    public String getGampla_status() {
        return gampla_status;
    }

    public void setGampla_status(String pla_status) {
        this.gampla_status = pla_status;
    }

    public String getGampla_play_datetime() {
        return gampla_play_datetime;
    }

    public String getGampla_lifelines() {
        return gampla_lifelines;
    }

    public String getGampla_turn_order() {
        return gampla_turn_order;
    }

    public String getGampla_id() {
        return gampla_id;
    }

    public String getPlace() {
        return place;
    }

    public String getGam_status() {
        return gam_status;
    }

    public String getGam_info1() {
        return gam_info1;
    }

    public String getGam_info2() {
        return gam_info2;
    }

    public String getGampla_point_tot() {
        return gampla_point_tot;
    }

    public String getAward_badge_image() {
        return award_badge_image;
    }

    @Override
    public String toString() {
        return "GamesDataObject: \ngam_id: " + gam_id + "\npla_fb_id: "
                + pla_fb_id + "\npla_fb_name: " + pla_fb_name
                + "\ngampla_status: " + gampla_status
                + "\n gampla_play_datetime: " + gampla_play_datetime
                + "\ngampla_lifelines: " + gampla_lifelines
                + "\ngampla_turn_order: " + gampla_turn_order + "\ngampla_id: "
                + gampla_id + "\nplace: " + place + "\ngam_status: "
                + gam_status + "\ngam_info1: " + gam_info1 + "\ngam_info2: "
                + gam_info2 + "\ngampla_point_tot: " + gampla_point_tot
                + "\naward_badge_image: " + award_badge_image
                + "\nplaid: " + plaid;
    }

	public String getPlaid() {
		return plaid;
	}

	public void setPlaid(String plaid) {
		this.plaid = plaid;
	}

}

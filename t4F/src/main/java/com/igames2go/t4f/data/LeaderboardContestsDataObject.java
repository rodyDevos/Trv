
package com.igames2go.t4f.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardContestsDataObject implements Serializable {

	private String isDefault;
    private String contestid;
    private String contest_name;
    private String contest_image;
    private String viewable;
    private String contest_beg_date;
    private String contest_end_date;
    private String contest_beg_datetime;
    private String contest_end_datetime;
    private String contest_period;
    private String entered;
    private String contest_fee;
    private String contest_sku;
    private String awards;
    private String contestants;
    private String allow_fb_post;
    private String contest_url;
    private String contest_desc;
    private String attribute1;
    private String attribute2;
    
    @Override
    public String toString() {
        return "GetLeaderboardContestsDataObject: \ndefault: " + isDefault
                + "\ncontestid: " + contestid + "\ncontest_name: " + contest_name
                + "\ncontest_image: " + contest_image + "\nviewable: " + viewable
                + "\ncontest_beg_date: " + contest_beg_date + "\ncontest_end_date: " + contest_end_date
                + "\ncontest_beg_datetime: " + contest_beg_datetime + "\ncontest_end_datetime: " + contest_end_datetime
                + "\ncontest_period: " + contest_period + "\nentered: " + entered
                + "\ncontest_fee: " + contest_fee + "\ncontest_sku: " + contest_sku
                + "\nawards: " + awards + "\ncontestants: " + contestants
                + "\nallow_fb_post: " + allow_fb_post + "\ncontest_url: " + contest_url
                + "\ncontest_desc: " + contest_desc + "\nattribute1: " + attribute1
                + "\nattribute2: " + attribute2;
    }

    public void parseJSONObject(JSONObject obj){
    	 
    	try {
    		
			isDefault = obj.getString("default");
			contestid = obj.getString("contestid");
		    contest_name = obj.getString("contest_name");
		    contest_image = obj.getString("contest_image");
		    viewable = obj.getString("viewable");
		    contest_beg_date = obj.getString("contest_beg_date");
		    contest_end_date = obj.getString("contest_end_date");
		    contest_beg_datetime = obj.getString("contest_beg_datetime");
		    contest_end_datetime = obj.getString("contest_end_datetime");
		    contest_period = obj.getString("contest_period");
		    entered = obj.getString("entered");
		    contest_fee = obj.getString("contest_fee");
		    contest_sku = obj.getString("contest_sku");
		    awards = obj.getString("awards");
		    contestants = obj.getString("contestants");
		    allow_fb_post = obj.getString("allow_fb_post");
		    contest_url = obj.getString("contest_url");
		    contest_desc = obj.getString("contest_desc");
		    attribute1 = obj.getString("attribute1");
		    attribute2 = obj.getString("attribute2");
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public String getContestId() {
		return contestid;
	}

	public void setContestId(String contestid) {
		this.contestid = contestid;
	}

	public String getContestName() {
		return contest_name;
	}

	public void setContestName(String contest_name) {
		this.contest_name = contest_name;
	}

	public String getContestImage() {
		return contest_image;
	}

	public void setContestImage(String contest_image) {
		this.contest_image = contest_image;
	}

	public String getViewable() {
		return viewable;
	}

	public void setViewable(String viewable) {
		this.viewable = viewable;
	}

	public String getContestBegDate() {
		return contest_beg_date;
	}

	public void setContestBegDate(String contest_beg_date) {
		this.contest_beg_date = contest_beg_date;
	}

	public String getContestEndDate() {
		return contest_end_date;
	}

	public void setContestEndDate(String contest_end_date) {
		this.contest_end_date = contest_end_date;
	}

	public String getContestPeriod() {
		return contest_period;
	}

	public void setContestPeriod(String contest_period) {
		this.contest_period = contest_period;
	}

	public String getEntered() {
		return entered;
	}

	public void setEntered(String entered) {
		this.entered = entered;
	}

	public String getContestFee() {
		return contest_fee;
	}

	public void setContestFee(String contest_fee) {
		this.contest_fee = contest_fee;
	}

	public String getContestSku() {
		return contest_sku;
	}

	public void setContestSku(String contest_sku) {
		this.contest_sku = contest_sku;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public String getContestants() {
		return contestants;
	}

	public void setContestants(String contestants) {
		this.contestants = contestants;
	}

	public String getAllowFBPost() {
		return allow_fb_post;
	}

	public void setAllowFBPost(String allow_fb_post) {
		this.allow_fb_post = allow_fb_post;
	}

	public String getContestUrl() {
		return contest_url;
	}

	public void setContestUrl(String contest_url) {
		this.contest_url = contest_url;
	}

	public String getContestDesc() {
		return contest_desc;
	}

	public void setContestDesc(String contest_desc) {
		this.contest_desc = contest_desc;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getDefault() {
		return isDefault;
	}

	public void setDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getContestBegDateTime() {
		return contest_beg_datetime;
	}

	public void setContestBegDateTime(String contest_beg_datetime) {
		this.contest_beg_datetime = contest_beg_datetime;
	}

	public String getContestEndDateTime() {
		return contest_end_datetime;
	}

	public void setContestEndDateTime(String contest_end_datetime) {
		this.contest_end_datetime = contest_end_datetime;
	}

}

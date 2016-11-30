
package com.igames2go.t4f.data;

import org.json.JSONException;
import org.json.JSONObject;


public class LeaderboardPlayersDataObject {

	private String contestid;
    private String plaid;
    private String rank;
    private String player_name;
    private String player_image;
    private String points;
    private String award_badge_image;
    private String attribute1;
    private String attribute2;
    
    
    @Override
    public String toString() {
        return "GetLeaderboardObject: \ncontestid: " + contestid
                + "\nplaid: " + plaid + "\nrank: " + rank
                + "\nplayer_name: " + player_name + "\nplayer_image: " + player_image
                + "\npoints: " + points + "\naward_badge_image: " + award_badge_image
                + "\nattribute1: " + attribute1 + "\nattribute2: " + attribute2;
    }

    public void parseJSONObject(JSONObject obj){
   	 
    	try {
    		
    		contestid = obj.getString("contestid");
    		plaid = obj.getString("plaid");
    		rank = obj.getString("rank");
    		player_name = obj.getString("player_name");
    		player_image = obj.getString("player_image");
    		points = obj.getString("points");
    		award_badge_image = obj.getString("award_badge_image");
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

	public String getPlaId() {
		return plaid;
	}

	public void setPlaId(String plaid) {
		this.plaid = plaid;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPlayerName() {
		return player_name;
	}

	public void setPlayerName(String player_name) {
		this.player_name = player_name;
	}

	public String getPlayerImage() {
		return player_image;
	}

	public void setPlayerImage(String player_image) {
		this.player_image = player_image;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
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

}

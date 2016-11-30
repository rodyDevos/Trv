
package com.igames2go.t4f.data;

import java.io.Serializable;

public class EndOfTurnDataObject implements Serializable {

    private String idindicator;
    private String plaid_next;
    private String fbid_next;
    private String fbname_next;
    private String message1;
    private String message2;
    private String didyouknow_title;
    private String chat_number;
    private String image1;
    public String flag1;
    public String flag2;
    public String flag3;
    public String flag4;
    public String banner_image;
    public String banner_url;

    public String getIdindicator() {
        return idindicator;
    }

    public String getPlaid_next() {
        return plaid_next;
    }

    public String getFbid_next() {
        return fbid_next;
    }

    public String getFbname_next() {
        return fbname_next;
    }

    public String getMessage1() {
        return message1;
    }

    public String getMessage2() {
        return message2;
    }

    public String getDidyouknow_title() {
        return didyouknow_title;
    }

    public String getChat_number() {
        return chat_number;
    }

    public String getImage1() {
        return image1;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public String getBanner_url() {
        return banner_url;
    }

    @Override
    public String toString() {
        return "EndOfTurnDataObject: \nidindicator: " + idindicator
                + "\nplaid_next: " + plaid_next + "\nfbid_next: " + fbid_next
                + "\nfbname_next: " + fbname_next + "\nmessage1: " + message1
                + "\nmessage2: " + message2 + "\ndidyouknow_title: "
                + didyouknow_title + "\nchat_number: " + chat_number
                + "\nimage1: " + image1
                + "\nbanner_image: " + banner_image
                + "\nbanner_url: " + banner_url;
    }

}

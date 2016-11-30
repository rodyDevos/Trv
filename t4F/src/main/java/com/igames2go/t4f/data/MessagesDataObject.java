
package com.igames2go.t4f.data;

public class MessagesDataObject {

    private String gampla_id;
    private String pla_fb_id;
    private String pla_fb_name;
    private String mes_type;
    private String mes_add_datetime;
    private String mes_text;

    public String getGampla_id() {
        return gampla_id;
    }

    public String getPla_fb_id() {
        return pla_fb_id;
    }

    public String getPla_fb_name() {
        return pla_fb_name;
    }

    public String getMes_type() {
        return mes_type;
    }

    public String getMes_add_datetime() {
        return mes_add_datetime;
    }

    public String getMes_text() {
        return mes_text;
    }

    @Override
    public String toString() {
        return "MessagesDataObject: \ngampla_id: " + gampla_id
                + "\npla_fb_id: " + pla_fb_id + "\npla_fb_name: " + pla_fb_name
                + "\nmes_type: " + mes_type + "\nmes_add_datetime: "
                + mes_add_datetime + "\nmes_text: " + mes_text;
    }

}

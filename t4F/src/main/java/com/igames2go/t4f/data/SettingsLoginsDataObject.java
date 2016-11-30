
package com.igames2go.t4f.data;

public class SettingsLoginsDataObject {

    private String pla_fb_id;
    private String pla_fb_name;
    private String pla_id;
    private String plaapp_id;
    private String device_notify;
    private boolean isChanged = false;

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public String getPla_fb_id() {
        return pla_fb_id;
    }

    public String getPla_fb_name() {
        return pla_fb_name;
    }

    public String getPla_id() {
        return pla_id;
    }

    public String getPlaapp_id() {
        return plaapp_id;
    }

    public String getDevice_notify() {
        return device_notify;
    }

    public void setDevice_notify(String device_notify) {
        this.device_notify = device_notify;
    }

    @Override
    public String toString() {
        return "SettingsLoginsDataObject: \npla_fb_id: "
                + pla_fb_id + "\npla_fb_name: " + pla_fb_name
                + "\npla_id: " + pla_id + "\nplaapp_id: "
                + plaapp_id + "\ndevice_notify: " + device_notify;
    }

}

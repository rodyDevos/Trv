
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class SettingsNotifications {

    private List<SettingsNotificationsDataObject> settingsnotification = new ArrayList<SettingsNotificationsDataObject>();

    public List<SettingsNotificationsDataObject> getSettingsNotification() {
        return settingsnotification;
    }

    @Override
    public String toString() {
        return "getSettingsNotification: " + settingsnotification;
    }

}

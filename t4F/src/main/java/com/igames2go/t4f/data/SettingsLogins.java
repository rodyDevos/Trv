
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class SettingsLogins {

    private List<SettingsLoginsDataObject> settingslogins = new ArrayList<SettingsLoginsDataObject>();

    public List<SettingsLoginsDataObject> getSettingsLogins() {
        return settingslogins;
    }

    @Override
    public String toString() {
        return "getSettingsLogins: " + settingslogins;
    }

}

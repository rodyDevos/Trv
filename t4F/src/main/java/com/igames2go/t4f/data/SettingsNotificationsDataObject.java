
package com.igames2go.t4f.data;

public class SettingsNotificationsDataObject {

    private String notification;
    private String game_start;
    private String your_turn;
    private String anyones_turn;
    private String chat;

    public String getNotification() {
        return notification;
    }

    public String getGame_start() {
        return game_start;
    }

    public String getYour_turn() {
        return your_turn;
    }

    public String getAnyones_turn() {
        return anyones_turn;
    }

    public String getChat() {
        return chat;
    }

    @Override
    public String toString() {
        return "SettingsNotificationsDataObject: \nnotification: "
                + notification + "\ngame_start: " + game_start
                + "\nyour_turn: " + your_turn + "\nanyones_turn: "
                + anyones_turn + "\nchat: " + chat;
    }

}

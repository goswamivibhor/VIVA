package com.govibs.viva.storage.bean;

import java.io.Serializable;

/**
 * Notification POJO
 * Created by Vibhor on 3/2/16.
 */
public class NotificationBean implements Serializable {

    private int notificationID;
    private String notificationText;
    private String notificationPackage;
    private String notificationApp;
    private int notificationCount;

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationPackage() {
        return notificationPackage;
    }

    public void setNotificationPackage(String notificationPackage) {
        this.notificationPackage = notificationPackage;
    }

    public String getNotificationApp() {
        return notificationApp;
    }

    public void setNotificationApp(String notificationApp) {
        this.notificationApp = notificationApp;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }
}

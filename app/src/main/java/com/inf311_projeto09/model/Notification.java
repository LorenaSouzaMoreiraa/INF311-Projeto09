package com.inf311_projeto09.model;

import com.inf311_projeto09.api.RubeusApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {
    private Date notificationTime;
    private boolean read;
    private String title;
    private String subtitle;

    public Notification(final Date notificationTime, final boolean read, final String title,
                        final String subtitle) {
        this.notificationTime = notificationTime;
        this.read = read;
        this.title = title;
        this.subtitle = subtitle;
    }

    public Notification(final String notification) {
        final String[] parts = notification.split(";");

        this.notificationTime = RubeusApi.parseIsoDate(parts[0]);
        this.read = parts[1].equals("1");
        this.title = parts[2];
        this.subtitle = parts[3];
    }

    public Date getNotificationTime() {
        return this.notificationTime;
    }

    public void setNotificationTime(final Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public boolean getRead() {
        return this.read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public Notification readNotification() {
        return new Notification(this.notificationTime, true, this.title, this.subtitle);
    }

    public String toRubeusNotification() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("pt", "BR"));
        final String dateStr = dateFormat.format(this.notificationTime);
        final String readStr = this.read ? "1" : "0";
        return dateStr + ";" + readStr + ";" + this.title + ";" + this.subtitle;
    }
}

package com.example.dinehero;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {
    private String date;
    private String title;
    private String type;
    private String recurrence;

    public Event(String date, String title, String type, String recurrence) {
        this.date = date;
        this.title = title;
        this.type = type;
        this.recurrence = recurrence;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.example.dinehero;

public class Event {
    private String date;
    private String title;
    private String type;
    private String recurrence;
    private boolean isHeader;

    private boolean finished;

    // Constructor for regular event
    public Event(String date, String title, String type, String recurrence) {
        this.date = date;
        this.title = title;
        this.type = type;
        this.recurrence = recurrence;
        this.isHeader = false;
        this.finished = false;
    }

    // Constructor for date headers
    public Event(String date, boolean isHeader) {
        this.date = date;
        this.isHeader = isHeader;
        this.title = "";
        this.type = "";
        this.recurrence = "";
    }

    // Getters
    public String getDate() { return date; }

    public boolean getFinished() { return finished; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getRecurrence() { return recurrence; }
    public boolean isHeader() { return isHeader; }

    // Setters
    public void setDate(String date) { this.date = date; }
    public void setTitle(String title) {
        if (!isHeader) {
            this.title = title;
        }
    }
    public void setType(String type) {
        if (!isHeader) {
            this.type = type;
        }
    }
    public void setFinished(boolean x){
        finished = x;
    }
    public void setRecurrence(String recurrence) {
        if (!isHeader) {
            this.recurrence = recurrence;
        }
    }
}

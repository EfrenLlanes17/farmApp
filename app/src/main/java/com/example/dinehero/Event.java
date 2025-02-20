package com.example.dinehero;


public class Event {
    public String date;
    public String title;
    public String type;  // Task, Employee Schedule, Harvest, IoT
    public String recurrence;  // None, Daily, Weekly, Monthly, Yearly

    public Event(String date, String title, String type, String recurrence) {
        this.date = date;
        this.title = title;
        this.type = type;
        this.recurrence = recurrence;
    }
}

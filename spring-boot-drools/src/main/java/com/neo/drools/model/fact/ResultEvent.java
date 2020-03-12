package com.neo.drools.model.fact;

import java.util.ArrayList;
import java.util.List;

public class ResultEvent {
    private List<String> events = new ArrayList<String>();

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}

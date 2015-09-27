package com.example.raf0c.myoffice.model;

/**
 * Created by raf0c on 27/09/15.
 */
public class Visits {

    public long id;
    public String office;
    public long entry;
    public long exit;
    public long duration;

    public Visits(Long id, String office, Long entry, Long exit, Long duration) {
        this.duration = duration;
        this.entry = entry;
        this.exit = exit;
        this.office = office;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEntry() {
        return entry;
    }

    public void setEntry(Long entry) {
        this.entry = entry;
    }

    public Long getExit() {
        return exit;
    }

    public void setExit(Long exit) {
        this.exit = exit;
    }


}

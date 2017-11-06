package org.senai.mecatronica.dripper.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 05/11/2017.
 */

public class IrrigationData implements Serializable{

    private Boolean oneTime;
    private String start;
    private Integer duration;
    private List<String> weekDays;

    public IrrigationData(){
        this.weekDays = new ArrayList<String>();
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void addWeekDay(String weekDay) {
        this.weekDays.add(weekDay);
    }
}

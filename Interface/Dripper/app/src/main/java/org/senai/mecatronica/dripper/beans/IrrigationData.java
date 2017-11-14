package org.senai.mecatronica.dripper.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Felipe on 05/11/2017.
 */

public class IrrigationData implements Serializable{

    private Boolean oneTime;
    private String startTime;
    private String startDate;
    private List<Integer> duration;
    private HashMap<String, Boolean> weekDays;

    public IrrigationData(){
        this.weekDays = new HashMap<>();
        this.weekDays.put("Sunday", false);
        this.weekDays.put("Monday", false);
        this.weekDays.put("Tuesday", false);
        this.weekDays.put("Wednesday", false);
        this.weekDays.put("Thursday", false);
        this.weekDays.put("Friday", false);
        this.weekDays.put("Saturday", false);

    }

    public List<Integer> getDuration() {
        return duration;
    }

    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public HashMap<String, Boolean> getWeekDays() {
        return weekDays;
    }

    public void setWeekDay(String key, Boolean value) {
        this.weekDays.put(key, value);
    }

    public void setWeekDays(boolean[] weekdays){

        this.weekDays.put("Sunday", weekdays[0]);
        this.weekDays.put("Monday", weekdays[1]);
        this.weekDays.put("Tuesday", weekdays[2]);
        this.weekDays.put("Wednesday", weekdays[3]);
        this.weekDays.put("Thursday", weekdays[4]);
        this.weekDays.put("Friday", weekdays[5]);
        this.weekDays.put("Saturday", weekdays[6]);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}

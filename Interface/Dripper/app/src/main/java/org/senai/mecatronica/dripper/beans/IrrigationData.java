package org.senai.mecatronica.dripper.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Felipe on 05/11/2017.
 */

public class IrrigationData implements Serializable{

    private Boolean oneTime;
    private String startTime;
    private String startDate;
    private Integer duration;
    private List<String> weekDays;
    private Integer id;

    public IrrigationData(){
        this.weekDays = Collections.emptyList();
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void clear(){
        this.oneTime = false;
        this.startTime = "";
        this.startDate = "";
        this.duration = 0;
        this.weekDays = Collections.emptyList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

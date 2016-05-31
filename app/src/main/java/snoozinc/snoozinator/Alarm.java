package snoozinc.snoozinator;

public class Alarm {

    int alarmId;
    int hour;
    int minute;
    Boolean on;
    Boolean[] weekDays;

    public Alarm(){

    }

    public Alarm(int alarmId, int hour, int minute, Boolean on, Boolean[] weekDays) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.on = on;

        Boolean[] weekDaysTemp = {false, false, false, false, false, false, false};

        if(weekDays.length == 7){
            weekDaysTemp = weekDays;
        }

        this.weekDays = weekDaysTemp;
    }

    public Alarm(int hour, int minute, Boolean on, Boolean[] weekDays) {
        this.hour = hour;
        this.minute = minute;
        this.on = on;

        Boolean[] weekDaysTemp = {false, false, false, false, false, false, false};

        if(weekDays.length == 7){
            weekDaysTemp = weekDays;
        }

        this.weekDays = weekDaysTemp;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Boolean[] getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(Boolean[] weekDays) {
        Boolean[] weekDaysTemp = {false, false, false, false, false, false, false};

        if(weekDays.length == 7){
            weekDaysTemp = weekDays;
        }

        this.weekDays = weekDaysTemp;
    }
}

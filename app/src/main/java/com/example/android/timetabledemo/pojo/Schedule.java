package com.example.android.timetabledemo.pojo;

public class Schedule {
    private String course;
    private String semester;
    private String weekDay;
    private String startTime;
    private String endTime;
    private String roomNo;

    public Schedule(String course, String semester, String weekDay, String startTime, String endTime, String roomNo) {
        this.course = course;
        this.semester = semester;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNo = roomNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}

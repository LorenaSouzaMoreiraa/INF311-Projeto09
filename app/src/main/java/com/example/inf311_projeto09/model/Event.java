package com.example.inf311_projeto09.model;

import java.util.Date;
import java.util.Map;

public class Event {
    private int course;
    private String title;
    private String description;
    private String type;
    private EventVerificationMethod verificationMethod;
    private String checkInCode;
    private String location;
    private Date beginTime;
    private Date endTime;
    private Date checkInEnabled;
    private Date checkOutEnabled;
    private Date checkInTime;
    private Date checkOutTime;
    private EventStage eventStage;

    public Event(final int course, final String title, final String description, final String type,
                 final EventVerificationMethod verificationMethod, final String checkInCode, final String location,
                 final Date beginTime, final Date endTime, final Date checkInEnabled, final Date checkOutEnabled,
                 final Date checkInTime, final Date checkOutTime, final EventStage eventStage) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.type = type;
        this.verificationMethod = verificationMethod;
        this.checkInCode = checkInCode;
        this.location = location;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.checkInEnabled = checkInEnabled;
        this.checkOutEnabled = checkOutEnabled;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.eventStage = eventStage;
    }

    public int getCourse() {
        return this.course;
    }

    public void setCourse(final int course) {
        this.course = course;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public EventVerificationMethod getVerificationMethod() {
        return this.verificationMethod;
    }

    public void setVerificationMethod(final EventVerificationMethod verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public String getCheckInCode() {
        return this.checkInCode;
    }

    public void setCheckInCode(final String checkInCode) {
        this.checkInCode = checkInCode;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(final Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    public Date getCheckInEnabled() {
        return this.checkInEnabled;
    }

    public void setCheckInEnabled(final Date checkInEnabled) {
        this.checkInEnabled = checkInEnabled;
    }

    public Date getCheckOutEnabled() {
        return this.checkOutEnabled;
    }

    public void setCheckOutEnabled(final Date checkOutEnabled) {
        this.checkOutEnabled = checkOutEnabled;
    }

    public Date getCheckInTime() {
        return this.checkInTime;
    }

    public void setCheckInTime(final Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return this.checkOutTime;
    }

    public void setCheckOutTime(final Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public EventStage getEventStage() {
        return this.eventStage;
    }

    public void setEventStage(final EventStage eventStage) {
        this.eventStage = eventStage;
    }

    public enum EventStage {
        CURRENT,
        NEXT,
        ENDED
    }

    public enum EventVerificationMethod {
        QR_CODE,
        VERIFICATION_CODE
    }

    public record RawEventResponse(int curso, String processoNome, String etapaNome,
                                   Map<String, Object> camposPersonalizados) {
    }
}




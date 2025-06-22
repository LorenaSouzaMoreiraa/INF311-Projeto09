package com.example.inf311_projeto09.model;

import java.util.Date;
import java.util.Map;

public class Event {
    private int id;
    private String title;
    private String description;
    private String type;
    private String verificationMethod;
    private String checkInCode;
    private String checkOutCode;
    private int referEventId;
    private Date beginTime;
    private Date endTime;
    private Date checkInEnabled;
    private Date checkOutEnabled;
    private Date checkInTime;
    private Date checkOutTime;
    private EventStage eventStage;

    public Event(final int id, final String title, final String description, final String type,
                 final String verificationMethod, final String checkInCode, final String checkOutCode,
                 final int referEventId, final Date beginTime, final Date endTime,
                 final Date checkInEnabled, final Date checkOutEnabled,
                 final Date checkInTime, final Date checkOutTime,
                 final EventStage eventStage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.verificationMethod = verificationMethod;
        this.checkInCode = checkInCode;
        this.checkOutCode = checkOutCode;
        this.referEventId = referEventId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.checkInEnabled = checkInEnabled;
        this.checkOutEnabled = checkOutEnabled;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.eventStage = eventStage;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
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

    public String getVerificationMethod() {
        return this.verificationMethod;
    }

    public void setVerificationMethod(final String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public String getCheckInCode() {
        return this.checkInCode;
    }

    public void setCheckInCode(final String checkInCode) {
        this.checkInCode = checkInCode;
    }

    public String getCheckOutCode() {
        return this.checkOutCode;
    }

    public void setCheckOutCode(final String checkOutCode) {
        this.checkOutCode = checkOutCode;
    }

    public int getReferEventId() {
        return this.referEventId;
    }

    public void setReferEventId(final int referEventId) {
        this.referEventId = referEventId;
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

    public record RawEventResponse(int id, String processoNome, String etapaNome,
                                   Map<String, Object> camposPersonalizados) {
    }
}




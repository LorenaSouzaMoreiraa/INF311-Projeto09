package com.example.inf311_projeto09.helper;

import com.example.inf311_projeto09.model.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventHelper {

    private EventHelper() {
        //
    }

    private static List<Event> getHappenedEvents(final List<Event> events) {
        final Date now = new Date();
        return events.stream()
                .filter(event -> event.getBeginTime().before(now))
                .collect(Collectors.toList());
    }

    private static List<Event> getHappenedEventsInMonth(final List<Event> events, final int month, final int year) {
        return events.stream()
                .filter(event -> {
                    final Date begin = event.getBeginTime();
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(begin);
                    final int eventMonth = calendar.get(Calendar.MONTH);
                    final int eventYear = calendar.get(Calendar.YEAR);
                    return eventMonth == month && eventYear == year;
                })
                .collect(Collectors.toList());
    }

    public static int numberOfEventsParticipated(final List<Event> events) {
        return getHappenedEvents(events).size();
    }

    public static int numberOfEventsParticipatedInMonth(final List<Event> events, final int month, final int year) {
        return getHappenedEventsInMonth(events, month, year).size();
    }

    public static int numberOfEventsParticipatedInDay(final List<Event> events, final int day, final int month, final int year) {
        return (int) events.stream()
                .filter(event -> {
                    final Date date = event.getBeginTime();
                    final Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    return cal.get(Calendar.DAY_OF_MONTH) == day &&
                            cal.get(Calendar.MONTH) == month &&
                            cal.get(Calendar.YEAR) == year;
                })
                .count();
    }

    public static int numberOfMissedEvents(final List<Event> events) {
        final List<Event> happenedEvents = getHappenedEvents(events);
        return (int) happenedEvents.stream()
                .filter(event -> event.getCheckInTime() == null)
                .count();
    }

    public static int numberOfMissedEventsInMonth(final List<Event> events, final int month, final int year) {
        final List<Event> happenedEvents = getHappenedEventsInMonth(events, month, year);
        return (int) happenedEvents.stream()
                .filter(event -> event.getCheckInTime() == null)
                .count();
    }

    public static int numberOfDelayedEvents(final List<Event> events) {
        final List<Event> happenedEvents = getHappenedEvents(events);

        final long tenMinutesInMillis = 600000L;

        return (int) happenedEvents.stream()
                .filter(event -> event.getCheckInTime() != null)
                .filter(event -> {
                    final long delay = event.getCheckInTime().getTime() - event.getBeginTime().getTime();
                    return delay >= tenMinutesInMillis;
                })
                .count();
    }

    public static int numberOfDelayedEventsInMonth(final List<Event> events, final int month, final int year) {
        final List<Event> happenedEvents = getHappenedEventsInMonth(events, month, year);

        final long tenMinutesInMillis = 600000L;

        return (int) happenedEvents.stream()
                .filter(event -> event.getCheckInTime() != null)
                .filter(event -> {
                    final long delay = event.getCheckInTime().getTime() - event.getBeginTime().getTime();
                    return delay >= tenMinutesInMillis;
                })
                .count();
    }
}

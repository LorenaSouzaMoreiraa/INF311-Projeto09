package com.example.inf311_projeto09.api;

import com.example.inf311_projeto09.model.EventJava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class RubeusApi {

    private static final RubeusApiHelper helper = new RubeusApiHelper();

    private RubeusApi() {
        //
    }

    private static Date parseIsoDate(final Object value) {
        if (value instanceof final String string) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("pt", "BR")).parse(string);
            } catch (final ParseException e) {
                //
            }
        }

        return null;
    }

    public static List<EventJava> listUserEvents(final int userId) throws Exception {
        final Function<List<EventJava.RawEventResponse>, List<EventJava>> toCustomList = events -> events.stream()
                .map(event -> {
                    final Map<String, Object> customFields = event.camposPersonalizados();

                    return new EventJava(event.id(),
                            (String) customFields.get(RubeusFields.UserEvent.TITLE.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.DESCRIPTION.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.TYPE.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.VERIFICATION_TYPE.getIdentifier()),
                            Integer.parseInt((String) Objects.requireNonNull(customFields.get(RubeusFields.UserEvent.REFER_EVENT_ID.getIdentifier()))),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.BEGIN_TIME.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.END_TIME.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_IN_ENABLED.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_OUT_ENABLED.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_IN_TIME.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_OUT_TIME.getIdentifier())));
                })
                .collect(Collectors.toList());

        return helper.executeRequest(helper.listUserEventsCall(userId), toCustomList);
    }
}

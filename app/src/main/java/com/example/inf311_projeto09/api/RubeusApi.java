package com.example.inf311_projeto09.api;

import com.example.inf311_projeto09.model.Event;
import com.example.inf311_projeto09.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static Boolean registerUser(final String name, final String email, final String school, final String password, final String cpf, final User.UserRole type) {
        return helper.executeRequest(helper.registerUserCall(name, email, school, password, cpf, type));
    }

    public static List<String> listSchools() {
        final Function<List<Object>, List<String>> toCustomList = schools -> schools.stream()
                .map(school -> {
                    final Map<String, Object> convertedSchool = (Map<String, Object>) school;
                    return convertedSchool.get("descricao") + " (" + convertedSchool.get("nome") + ")";
                })
                .collect(Collectors.toList());

        return helper.executeRequest(helper.listSchoolsCall(), toCustomList, List::of);
    }

    public static User searchUserByEmail(final String email) {
        final Function<List<User.RawUserResponse>, User> toUser = users -> users.stream()
                .filter(user -> email.equals(((Map<String, Object>) user.emails().get("principal")).get("email")))
                .map(user -> {
                    final List<Object> customFields = user.camposPersonalizados();

                    final String password = customFields.stream()
                            .filter(field -> RubeusFields.UserAccount.PASSWORD.getIdentifier()
                                    .equals(((Map<String, Object>) field).get("coluna")))
                            .map(field -> (String) ((Map<String, Object>) field).get("valor"))
                            .collect(Collectors.toList())
                            .get(0);
                    final String userRole = customFields.stream()
                            .filter(field -> RubeusFields.UserAccount.TYPE.getIdentifier()
                                    .equals(((Map<String, Object>) field).get("coluna")))
                            .map(field -> (String) ((Map<String, Object>) field).get("valor"))
                            .collect(Collectors.toList())
                            .get(0);

                    return new User(Integer.parseInt(user.id()),
                            user.nome(),
                            userRole.equals("User") ? User.UserRole.USER : User.UserRole.ADMIN,
                            email,
                            user.cpf(),
                            user.escolaOrigem(),
                            password);
                })
                .collect(Collectors.toList()).get(0);

        return helper.executeRequest(helper.searchUserByEmailCall(email), toUser, () -> null);
    }

    public static List<Event> listUserEvents(final int userId) {
        final Function<List<Event.RawEventResponse>, List<Event>> toCustomList = events -> events.stream()
                .filter(event -> "Eventos Aluno".equals(event.processoNome()))
                .map(event -> {
                    final Map<String, Object> customFields = event.camposPersonalizados();

                    final Event.EventVerificationMethod verificationMethod = "Código único".equals(customFields.get(RubeusFields.UserEvent.VERIFICATION_TYPE.getIdentifier())) ?
                            Event.EventVerificationMethod.VERIFICATION_CODE :
                            Event.EventVerificationMethod.QR_CODE;

                    final Date beginTime = parseIsoDate(customFields.get(RubeusFields.UserEvent.BEGIN_TIME.getIdentifier()));
                    final Date endTime = parseIsoDate(customFields.get(RubeusFields.UserEvent.END_TIME.getIdentifier()));

                    return new Event(event.id(),
                            (String) customFields.get(RubeusFields.UserEvent.TITLE.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.DESCRIPTION.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.TYPE.getIdentifier()),
                            verificationMethod,
                            (String) customFields.get(RubeusFields.UserEvent.CHECK_IN_CODE.getIdentifier()),
                            (String) customFields.get(RubeusFields.UserEvent.CHECK_OUT_CODE.getIdentifier()),
                            Integer.parseInt((String) Objects.requireNonNull(customFields.get(RubeusFields.UserEvent.REFER_EVENT_ID.getIdentifier()))),
                            beginTime,
                            endTime,
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_IN_ENABLED.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_OUT_ENABLED.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_IN_TIME.getIdentifier())),
                            parseIsoDate(customFields.get(RubeusFields.UserEvent.CHECK_OUT_TIME.getIdentifier())),
                            calculateEventStage(beginTime, endTime));
                })
                .collect(Collectors.toList());

        return helper.executeRequest(helper.listUserEventsCall(userId), toCustomList, List::of);
    }

    public static Boolean checkIn(final int userId, final Event event, final String checkInTime) {
        event.setCheckInTime(parseIsoDate(checkInTime));
        return helper.executeRequest(helper.checkInCall(userId, event.getId(), checkInTime));
    }

    public static Boolean checkOut(final int userId, final Event event, final String checkOutTime) {
        event.setCheckOutTime(parseIsoDate(checkOutTime));
        return helper.executeRequest(helper.checkOutCall(userId, event.getId(), checkOutTime));
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

    private static Event.EventStage calculateEventStage(final Date beginTime, final Date endTime) {
        final Date now = new Date();
        final Date endTimePlus10 = addMinutes(endTime, 10);

        if (now.after(endTimePlus10)) {
            return Event.EventStage.ENDED;
        }

        if (!now.before(beginTime) && !now.after(endTimePlus10)) {
            return Event.EventStage.CURRENT;
        }

        return Event.EventStage.NEXT;
    }

    private static Date addMinutes(final Date date, final int minutes) {
        if (date == null) return null;
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
}

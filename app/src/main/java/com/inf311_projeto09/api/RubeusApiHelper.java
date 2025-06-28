package com.inf311_projeto09.api;

import android.util.Log;

import com.inf311_projeto09.BuildConfig;
import com.inf311_projeto09.model.Event;
import com.inf311_projeto09.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

final class RubeusApiHelper {

    private static final String HTTP_REQUEST = "HTTP_REQUEST";
    private static final String CAMPOS_PERSONALIZADOS = "camposPersonalizados";
    private final RubeusService service;

    public RubeusApiHelper() {
        this.service = RetrofitClient.getInstance().create(RubeusService.class);
    }

    public <T> Boolean executeRequest(final Call<ApiResponse<T>> call) {
        final Callable<Boolean> task = () -> {
            final Response<ApiResponse<T>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && response.body().success()) {
                return true;
            } else {
                throw new HttpException(response);
            }
        };

        final Future<Boolean> future = Executors.newSingleThreadExecutor().submit(task);

        try {
            return future.get();
        } catch (final ExecutionException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof final IOException ioException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(ioException.getMessage()));
            } else if (cause instanceof final HttpException httpException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(httpException.getMessage()));
            } else if (cause instanceof final RuntimeException runtimeException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(runtimeException.getMessage()));
            } else {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(e.getMessage()));
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e(HTTP_REQUEST, Objects.requireNonNull(e.getMessage()));
        }

        return false;
    }

    public <T, R> R executeRequest(final Call<ApiResponse<T>> call, final Function<T, R> transform, final Supplier<R> fallback) {
        final Callable<R> task = () -> {
            final Response<ApiResponse<T>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && response.body().success()) {
                return transform.apply(response.body().dados());
            } else {
                throw new HttpException(response);
            }
        };

        final Future<R> future = Executors.newSingleThreadExecutor().submit(task);

        try {
            return future.get();
        } catch (final ExecutionException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof final IOException ioException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(ioException.getMessage()));
            } else if (cause instanceof final HttpException httpException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(httpException.getMessage()));
            } else if (cause instanceof final RuntimeException runtimeException) {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(runtimeException.getMessage()));
            } else {
                Log.e(HTTP_REQUEST, Objects.requireNonNull(e.getMessage()));
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e(HTTP_REQUEST, Objects.requireNonNull(e.getMessage()));
        }

        return fallback.get();
    }

    private Map<String, Object> defaultBody() {
        final Map<String, Object> body = new HashMap<>();
        body.put("origem", Integer.parseInt(BuildConfig.RUBEUS_API_ORIGIN));
        body.put("token", BuildConfig.RUBEUS_API_TOKEN);
        return body;
    }

    private List<Object> getUserFieldsReturn() {
        return List.of("id",
                "nome",
                "emails",
                "cpf",
                "escolaOrigem",
                "imagem",
                Map.of(
                        "key", CAMPOS_PERSONALIZADOS,
                        "campos", List.of("coluna",
                                "valor"),
                        "filtros", List.of(Map.of(
                                "coluna", List.of(RubeusFields.UserAccount.PASSWORD.getIdentifier(),
                                        RubeusFields.UserAccount.TYPE.getIdentifier(),
                                        RubeusFields.UserAccount.ENABLE_NOTIFICATIONS.getIdentifier(),
                                        RubeusFields.UserAccount.NOTIFICATIONS.getIdentifier())))));
    }

    public Call<ApiResponse<Object>> registerUserCall(final String name, final String email, final String school, final String password, final String cpf, final User.UserRole type) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserAccount.TYPE.getIdentifier(), type.getIdentifier());
        customFields.put(RubeusFields.UserAccount.PASSWORD.getIdentifier(), password);
        customFields.put(RubeusFields.UserAccount.ENABLE_NOTIFICATIONS.getIdentifier(), "1");

        body.put("nome", name);
        body.put("emailPrincipal", email);
        body.put("escolaOrigem", school);
        body.put("cpf", cpf);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.registerUser(body);
    }

    public Call<ApiResponse<Object>> updateUserCall(final int userId, final String name, final String school, final String password, final boolean enableNotifications, final String imageUrl) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserAccount.PASSWORD.getIdentifier(), password);
        customFields.put(RubeusFields.UserAccount.ENABLE_NOTIFICATIONS.getIdentifier(), enableNotifications ? "1" : "0");

        body.put("id", userId);
        body.put("nome", name);
        body.put("escolaOrigem", school);
        body.put("imagemUrl", imageUrl);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.updateUser(body);
    }

    public Call<ApiResponse<Object>> updateUserNotificationsCall(final int userId, final String name, final List<String> notifications) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserAccount.NOTIFICATIONS.getIdentifier(), notifications);

        body.put("id", userId);
        body.put("nome", name);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.updateUser(body);
    }

    public Call<ApiResponse<Object>> deleteUserCall(final int userId) {
        final Map<String, Object> body = this.defaultBody();

        body.put("id", userId);

        return this.service.deleteUser(body);
    }

    public Call<ApiResponse<List<Object>>> listSchoolsCall() {
        return this.service.listSchools(this.defaultBody());
    }

    public Call<ApiResponse<List<User.RawUserResponse>>> searchUserByEmailCall(final String email) {
        final Map<String, Object> body = this.defaultBody();

        body.put("camposRetorno", this.getUserFieldsReturn());
        body.put("email", email);

        return this.service.searchUserByEmail(body);
    }

    public Call<ApiResponse<List<User.RawUserResponse>>> searchUserByCpfCall(final String cpf) {
        final Map<String, Object> body = this.defaultBody();

        body.put("camposRetorno", this.getUserFieldsReturn());
        body.put("cpf", cpf);

        return this.service.searchUserByEmail(body);
    }

    public Call<ApiResponse<Object>> registerOfferCall(final String eventName, final String timestamp, final String eventType, final String school) {
        final Map<String, Object> body = this.defaultBody();

        body.put("nome", eventName);
        body.put("codOferta", timestamp);
        body.put("codCurso", "1");
        body.put("codModalidade", eventType.charAt(0));
        body.put("codUnidade", school.substring(school.lastIndexOf('(') + 1, school.length() - 1));
        body.put("codLocalOferta", List.of("1"));
        body.put("codNivelEnsino", 1);

        return this.service.registerOffer(body);
    }

    public Call<ApiResponse<Object>> searchOfferCall(final String timestamp) {
        final Map<String, Object> body = this.defaultBody();

        body.put("codOferta", timestamp);

        return this.service.searchOffer(body);
    }

    private String formatDate(final Date date) {
        if (date == null) {
            return null;
        }

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("pt", "BR"));
        return formatter.format(date);
    }

    public Call<ApiResponse<Object>> registerEventCall(final User user, final String courseId, final Event event) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserEvent.TITLE.getIdentifier(), event.getTitle());
        customFields.put(RubeusFields.UserEvent.DESCRIPTION.getIdentifier(), event.getDescription());
        customFields.put(RubeusFields.UserEvent.TYPE.getIdentifier(), event.getType());
        customFields.put(RubeusFields.UserEvent.VERIFICATION_TYPE.getIdentifier(), event.getVerificationMethod().getIdentifier());
        customFields.put(RubeusFields.UserEvent.CHECK_IN_CODE.getIdentifier(), event.getCheckInCode());
        customFields.put(RubeusFields.UserEvent.LOCATION.getIdentifier(), event.getLocation());
        customFields.put(RubeusFields.UserEvent.AUTO_CHECK.getIdentifier(), event.getAutoCheck() ? "1" : "0");
        customFields.put(RubeusFields.UserEvent.BEGIN_TIME.getIdentifier(), this.formatDate(event.getBeginTime()));
        customFields.put(RubeusFields.UserEvent.END_TIME.getIdentifier(), this.formatDate(event.getEndTime()));
        customFields.put(RubeusFields.UserEvent.CHECK_IN_ENABLED.getIdentifier(), this.formatDate(event.getCheckInEnabled()));
        customFields.put(RubeusFields.UserEvent.CHECK_OUT_ENABLED.getIdentifier(), this.formatDate(event.getCheckOutEnabled()));
        customFields.put(RubeusFields.UserEvent.CHECK_IN_TIME.getIdentifier(), this.formatDate(event.getCheckInTime()));
        customFields.put(RubeusFields.UserEvent.CHECK_OUT_TIME.getIdentifier(), this.formatDate(event.getCheckOutTime()));
        customFields.put(RubeusFields.UserEvent.PARTICIPANTS.getIdentifier(), event.getParticipants());

        body.put("pessoa", Map.of("id", user.getId()));
        body.put("curso", courseId);
        body.put("processo", "4");
        body.put("etapa", user.getType() == User.UserRole.ADMIN && event.getAutoCheck() ? "21" : "17");
        body.put("camposPersonalizados", customFields);

        return this.service.registerEvent(body);
    }

    public Call<ApiResponse<List<Event.RawEventResponse>>> listUserEventsCall(final int userId) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        final List<String> campos = new ArrayList<>();
        for (final RubeusFields.UserEvent field : RubeusFields.UserEvent.values()) {
            campos.add(field.getIdentifier());
        }

        customFields.put("key", CAMPOS_PERSONALIZADOS);
        customFields.put("campos", campos);

        body.put("id", userId);
        body.put("camposRetorno", List.of("curso", "processoNome", "etapaNome", customFields));

        return this.service.listUserEvents(body);
    }

    public Call<ApiResponse<Object>> updateEventCall(final int userId, final int courseId, final Event event) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserEvent.TITLE.getIdentifier(), event.getTitle());
        customFields.put(RubeusFields.UserEvent.BEGIN_TIME.getIdentifier(), this.formatDate(event.getBeginTime()));
        customFields.put(RubeusFields.UserEvent.END_TIME.getIdentifier(), this.formatDate(event.getEndTime()));
        customFields.put(RubeusFields.UserEvent.TYPE.getIdentifier(), event.getType());
        customFields.put(RubeusFields.UserEvent.DESCRIPTION.getIdentifier(), event.getDescription());
        customFields.put(RubeusFields.UserEvent.VERIFICATION_TYPE.getIdentifier(), event.getVerificationMethod().getIdentifier());
        customFields.put(RubeusFields.UserEvent.AUTO_CHECK.getIdentifier(), event.getAutoCheck() ? "1" : "0");

        body.put("tipo", 121);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.updateEvent(body);
    }

    public Call<ApiResponse<Object>> deleteEventCall(final int userId, final int courseId) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserEvent.ACTIVE.getIdentifier(), "0");

        body.put("tipo", 121);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.deleteEvent(body);
    }

    public Call<ApiResponse<Object>> enableCheckInCall(final int userId, final int courseId, final String checkInTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("tipo", 114);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_IN_ENABLED.getIdentifier(), checkInTime));

        return this.service.enableCheckIn(body);
    }

    public Call<ApiResponse<Object>> enableCheckOutCall(final int userId, final int courseId, final String checkOutTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("tipo", 116);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_OUT_ENABLED.getIdentifier(), checkOutTime));

        return this.service.enableCheckOut(body);
    }

    public Call<ApiResponse<Object>> checkInCall(final int userId, final int courseId, final String checkInTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("tipo", 114);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_IN_TIME.getIdentifier(), checkInTime));

        return this.service.checkIn(body);
    }

    public Call<ApiResponse<Object>> checkOutCall(final int userId, final int courseId, final String checkOutTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("tipo", 116);
        body.put("pessoa", Map.of("id", userId));
        body.put("curso", courseId);
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_OUT_TIME.getIdentifier(), checkOutTime));

        return this.service.checkOut(body);
    }
}

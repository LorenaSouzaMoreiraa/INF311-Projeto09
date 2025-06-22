package com.example.inf311_projeto09.api;

import android.util.Log;

import com.example.inf311_projeto09.BuildConfig;
import com.example.inf311_projeto09.model.Event;
import com.example.inf311_projeto09.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                Map.of(
                        "key", CAMPOS_PERSONALIZADOS,
                        "campos", List.of("coluna",
                                "valor"),
                        "filtros", List.of(Map.of(
                                "coluna", List.of(RubeusFields.UserAccount.PASSWORD.getIdentifier(),
                                        RubeusFields.UserAccount.TYPE.getIdentifier())))));
    }

    public Call<ApiResponse<Object>> registerUserCall(final String name, final String email, final String school, final String password, final String cpf, final User.UserRole type) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        customFields.put(RubeusFields.UserAccount.TYPE.getIdentifier(), type.getIdentifier());
        customFields.put(RubeusFields.UserAccount.PASSWORD.getIdentifier(), password);

        body.put("nome", name);
        body.put("emailPrincipal", email);
        body.put("escolaOrigem", school);
        body.put("cpf", cpf);
        body.put(CAMPOS_PERSONALIZADOS, customFields);

        return this.service.registerUser(body);
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
        body.put("camposRetorno", List.of("id", "processoNome", "etapaNome", customFields));

        return this.service.listUserEvents(body);
    }

    public Call<ApiResponse<Object>> checkInCall(final int userId, final int eventId, final String checkInTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("codigo", eventId);
        body.put("tipo", 114);
        body.put("pessoa", Map.of("id", userId));
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_IN_TIME, checkInTime));

        return this.service.checkIn(body);
    }

    public Call<ApiResponse<Object>> checkOutCall(final int userId, final int eventId, final String checkOutTime) {
        final Map<String, Object> body = this.defaultBody();

        body.put("codigo", eventId);
        body.put("tipo", 114);
        body.put("pessoa", Map.of("id", userId));
        body.put(CAMPOS_PERSONALIZADOS, Map.of(RubeusFields.UserEvent.CHECK_OUT_TIME, checkOutTime));

        return this.service.checkOut(body);
    }
}

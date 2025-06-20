package com.example.inf311_projeto09.api;

import com.example.inf311_projeto09.BuildConfig;
import com.example.inf311_projeto09.model.EventJava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

final class RubeusApiHelper {

    private final RubeusService service;

    public RubeusApiHelper() {
        this.service = RetrofitClient.getInstance().create(RubeusService.class);
    }

    public <T, R> R executeRequest(final Call<ApiResponse<T>> call, final Function<T, R> transform) throws IOException, HttpException, InterruptedException, ExecutionException {
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
                throw ioException;
            } else if (cause instanceof final HttpException httpException) {
                throw httpException;
            } else if (cause instanceof final RuntimeException runtimeException) {
                throw runtimeException;
            } else {
                throw e;
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public Map<String, Object> defaultBody() {
        final Map<String, Object> body = new HashMap<>();
        body.put("origem", Integer.parseInt(BuildConfig.RUBEUS_API_ORIGIN));
        body.put("token", BuildConfig.RUBEUS_API_TOKEN);
        return body;
    }

    public Call<ApiResponse<List<EventJava.RawEventResponse>>> listUserEventsCall(final int userId) {
        final Map<String, Object> body = this.defaultBody();
        final Map<String, Object> customFields = new HashMap<>();

        final List<String> campos = new ArrayList<>();
        for (final RubeusFields.UserEvent field : RubeusFields.UserEvent.values()) {
            campos.add(field.getIdentifier());
        }

        customFields.put("key", "camposPersonalizados");
        customFields.put("campos", campos);

        body.put("id", userId);
        body.put("camposRetorno", List.of("id", customFields));

        return this.service.listUserEvents(body);
    }
}

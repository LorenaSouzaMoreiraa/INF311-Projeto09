package com.example.inf311_projeto09.api;

import com.example.inf311_projeto09.model.EventJava;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface RubeusService {

    @POST("Contato/listarOportunidades")
    Call<ApiResponse<List<EventJava.RawEventResponse>>> listUserEvents(@Body Map<String, Object> body);
}

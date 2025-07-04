package com.inf311_projeto09.api;

import com.inf311_projeto09.model.Event;
import com.inf311_projeto09.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface RubeusService {

    @POST("Contato/cadastro")
    Call<ApiResponse<Object>> registerUser(@Body Map<String, Object> body);

    @POST("Contato/cadastro")
    Call<ApiResponse<Object>> updateUser(@Body Map<String, Object> body);

    @POST("Contato/excluirPessoa")
    Call<ApiResponse<Object>> deleteUser(@Body Map<String, Object> body);

    @POST("Unidade/listarUnidades")
    Call<ApiResponse<List<Object>>> listSchools(@Body Map<String, Object> body);

    @POST("Contato/dadosPessoas")
    Call<ApiResponse<List<User.RawUserResponse>>> searchUserByEmail(@Body Map<String, Object> body);

    @POST("Contato/listarOportunidades")
    Call<ApiResponse<List<Event.RawEventResponse>>> listUserEvents(@Body Map<String, Object> body);

    @POST("Curso/cadastroOferta")
    Call<ApiResponse<Object>> registerOffer(@Body Map<String, Object> body);

    @POST("Curso/listarOfertas")
    Call<ApiResponse<Object>> searchOffer(@Body Map<String, Object> body);

    @POST("Oportunidade/cadastro")
    Call<ApiResponse<Object>> registerEvent(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> updateEvent(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> deleteEvent(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> enableCheckIn(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> enableCheckOut(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> checkIn(@Body Map<String, Object> body);

    @POST("Evento/cadastro")
    Call<ApiResponse<Object>> checkOut(@Body Map<String, Object> body);
}

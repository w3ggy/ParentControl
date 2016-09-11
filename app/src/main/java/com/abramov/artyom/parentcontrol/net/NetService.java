package com.abramov.artyom.parentcontrol.net;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetService {
    String BASE_URL = "http://google.ru";

    @GET("/")
    Call<String> exampleJson();

    @GET("/")
    Call<String> exampleXml();
}

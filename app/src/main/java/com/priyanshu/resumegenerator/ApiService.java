package com.priyanshu.resumegenerator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("resume")
    Call<Resume> getResume(@Query("name") String name);
}

package com.example.goran.mvvm_demo.data.remote;

import com.example.goran.mvvm_demo.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("top-headlines")
    Call<ApiResponse> getArticles(@Query("sources") String sources,
                                  @Query("apiKey") String apiKey);

}
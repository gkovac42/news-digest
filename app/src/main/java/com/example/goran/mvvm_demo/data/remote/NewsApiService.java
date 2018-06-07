package com.example.goran.mvvm_demo.data.remote;

import com.example.goran.mvvm_demo.data.model.ApiResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("top-headlines")
    Single<ApiResponse> getArticles(@Query("sources") String sources,
                                    @Query("apiKey") String apiKey);

}
package com.example.goran.mvvm_demo.data.remote;

import com.example.goran.mvvm_demo.data.model.ArticlesResponse;
import com.example.goran.mvvm_demo.data.model.SourcesResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "4e02fcfa306546ba9556850eb654e723";

    private static final String SORT_BY_POPULARITY = "popularity";
    private static final String SORT_BY_RELEVANCY = "relevancy";
    private static final String SORT_BY_DATE = "publishedAt";

    private static final String LANG_EN = "en";

    private NewsApiService apiService;

    public ApiHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NewsApiService.class);
    }

    public Call<ArticlesResponse> getArticles(String source) {
        return apiService.getArticles(source, API_KEY);
    }

    public Call<ArticlesResponse> searchArticles(String query) {
        return apiService.searchArticles(query, SORT_BY_RELEVANCY, LANG_EN, API_KEY);
    }

    public Call<SourcesResponse> getSources() {
        return apiService.getSources(API_KEY);
    }
}

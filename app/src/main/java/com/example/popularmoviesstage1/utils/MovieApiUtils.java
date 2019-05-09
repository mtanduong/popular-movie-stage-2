package com.example.popularmoviesstage1.utils;

public class MovieApiUtils {
    // TODO - Enter API Key here
    public static final String API_KEY = "e51e568877142d0363099bd88c03ded5";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    static final String POPULAR_PATH = "movie/popular";
    static final String TOP_RATED_PATH = "movie/top_rated";
    static final String VIDEO_PATH = "movie/{id}/videos";
    static final String REVIEW_PATH = "movie/{id}/reviews";

    public static MovieService createService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieService.class);
    }
}

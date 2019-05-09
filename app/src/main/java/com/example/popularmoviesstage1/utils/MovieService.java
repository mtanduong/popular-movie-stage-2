package com.example.popularmoviesstage1.utils;

import com.example.popularmoviesstage1.models.MovieDBObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieService {

    String API_PARAM = "api_key";

    @GET(MovieApiUtils.POPULAR_PATH)
    Call<MovieDBObject> getPopularMovies(@Query(API_PARAM) String api_key);

    @GET(MovieApiUtils.TOP_RATED_PATH)
    Call<MovieDBObject> getTopRatedMovies(@Query(API_PARAM) String api_key);

    @GET(MovieApiUtils.VIDEO_PATH)
    Call<MovieDBObject> getVideoMovies(@Path("id") String movie_id, @Query(API_PARAM) String api_key);

    @GET(MovieApiUtils.REVIEW_PATH)
    Call<MovieDBObject> getReviewMovies(@Path("id") String movie_id, @Query(API_PARAM) String api_key);

}

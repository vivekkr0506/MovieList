package com.example.saveomovie.API

import com.example.saveomovie.models.MovieDetails
import com.example.saveomovie.models.Movies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page : Int):Single<Movies>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int):Single<MovieDetails>


}
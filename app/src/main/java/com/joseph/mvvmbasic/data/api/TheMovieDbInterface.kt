package com.joseph.mvvmbasic.data.api

import com.joseph.mvvmbasic.data.model.MovieDetails
import com.joseph.mvvmbasic.data.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {
    //https://api.themoviedb.org/3/movie/550?api_key=a1f8376a2ee3f152380b68f443070d2c

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int
    ): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("page") page: Int
    ): Single<MovieResponse>
}
package com.lbdev.dumbcharadestool

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("discover/movie")
    suspend fun getQuotes(
        @Query("api_key") apiKey: String = "7a6c9eeeb4573fa723434a79cd60518f",
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.asc",
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("primary_release_year") primaryReleaseYear: Int = 2000,
        @Query("with_original_language") withOriginalLanguage: String = "hi"
    ) : Response<MoviesList>
}
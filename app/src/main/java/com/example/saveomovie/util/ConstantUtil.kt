package com.example.saveomovie.util


const val API_KEY = "0e25e4523848dd2c78e7cca1d38aaf62"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

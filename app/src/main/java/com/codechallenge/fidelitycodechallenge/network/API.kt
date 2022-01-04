package com.codechallenge.fidelitycodechallenge.network

import com.codechallenge.fidelitycodechallenge.model.ResultResponse
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("search/anime/?q=")
    suspend fun getAnime(query: String): Response<ResultResponse>
}
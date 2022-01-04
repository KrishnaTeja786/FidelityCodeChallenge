package com.codechallenge.fidelitycodechallenge.viewmodel.repository

import com.codechallenge.fidelitycodechallenge.network.RetrofitInstance

class MainRepository() {
    suspend fun getAnime(query: String) =
        RetrofitInstance.api.getAnime(query)
}
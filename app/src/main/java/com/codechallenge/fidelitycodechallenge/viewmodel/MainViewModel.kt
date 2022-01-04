package com.codechallenge.fidelitycodechallenge.viewmodel

import androidx.lifecycle.*
import com.codechallenge.fidelitycodechallenge.model.ResultResponse
import com.codechallenge.fidelitycodechallenge.network.Resource
import com.codechallenge.fidelitycodechallenge.viewmodel.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    val mainRepository: MainRepository
) : ViewModel() {
    val animLiveData: MutableLiveData<Resource<ResultResponse>> = MutableLiveData()
    var animResponse: ResultResponse? = null
    var connectivityLD = MutableLiveData<Boolean>()
    private val _connectivityLD = connectivityLD


    fun getAnime() = viewModelScope.launch {
        safeSearchAnimeCall()
    }

    private suspend fun safeSearchAnimeCall() {
        animLiveData.postValue(Resource.Loading())
        try {
            if(isInternetAvailable() == true) {
                val response = mainRepository.getAnime("")
                animLiveData.postValue(handleResponse(response))
            } else {
                animLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> animLiveData.postValue(Resource.Error("Network Failure"))
                else -> animLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponse(response: Response<ResultResponse>) : Resource<ResultResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(animResponse == null ) {
                    animResponse = resultResponse
                }
                return Resource.Success(animResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun isInternetAvailable(): Boolean? {
        return _connectivityLD.value
    }
}
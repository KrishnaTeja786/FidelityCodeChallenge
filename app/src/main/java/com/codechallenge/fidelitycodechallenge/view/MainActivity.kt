package com.codechallenge.fidelitycodechallenge.view

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codechallenge.fidelitycodechallenge.databinding.ActivityMainBinding
import com.codechallenge.fidelitycodechallenge.utils.NetworkStatus
import com.codechallenge.fidelitycodechallenge.utils.NetworkStatusHelper
import com.codechallenge.fidelitycodechallenge.view.adapter.AnimeAdapter
import com.codechallenge.fidelitycodechallenge.viewmodel.MainViewModel
import com.codechallenge.fidelitycodechallenge.viewmodel.MainViewModelFactory
import com.codechallenge.fidelitycodechallenge.viewmodel.repository.MainRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val repository = MainRepository()
        val viewModelProviderFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        setContentView(view)
        setUpObservers()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
         layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL ,false)
         adapter = AnimeAdapter()
        }
    }

    private fun setUpObservers() {
        /* Observe network changes */
        NetworkStatusHelper(this@MainActivity).observe(this, {
            when(it){
                NetworkStatus.Available -> {
                    viewModel.connectivityLD.postValue(true)
                    display("Network Connection Established")
                }
                NetworkStatus.Unavailable -> {
                    display("No Internet")
                    viewModel.connectivityLD.postValue(false)
                }
            }
        })
    }

    private fun display(status: String) {
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show()
    }
}
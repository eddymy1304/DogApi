package com.example.dogapi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private var dogImagenes = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.setOnQueryTextListener(this)
        iniciarRecycler()
    }

    private fun iniciarRecycler() {
        adapter = DogAdapter(dogImagenes)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByName(query:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(DogApiService::class.java).getDogsByBreeds("$query/images")
            val perritos = call.body()
            //Esto va a correr en el hilo principal
            runOnUiThread{
                if (call.isSuccessful){
                    val imagenes = perritos?.imagenes ?: emptyList()
                    Log.e("ResInternet", imagenes.toString())
                    dogImagenes.clear()
                    dogImagenes.addAll(imagenes)
                    adapter.notifyDataSetChanged()
                    //show
                } else {
                    makeError()
                }
                ocultarTeclado()
            }

        }
    }

    private fun ocultarTeclado() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun makeError() {
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) searchByName(query.lowercase())
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}
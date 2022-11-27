package com.example.dogapi

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dogapi.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemDogBinding.bind(view)

    fun bind(imagen:String) {
        //Convertir una url a una imagen con Picasso
        Picasso.get().load(imagen).into(binding.imageDog)
    }
}
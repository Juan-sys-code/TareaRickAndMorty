package com.example.rickmortyapp.ui.theme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickmortyapp.R

class CharacterAdapter(private val characters: List<String>) :
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // CORRECCIÓN: Usar findViewById para asegurar que encuentra el ID del XML
        val img: ImageView = view.findViewById(R.id.imgCharacter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val characterUrl = characters[position]

        // Transformamos la URL del personaje en la URL de su imagen
        val characterId = characterUrl.split("/").last()
        val imageUrl = "https://rickandmortyapi.com/api/character/avatar/$characterId.jpeg"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.img)
    }

    override fun getItemCount() = characters.size
}
package com.example.rickmortyapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmortyapp.R
import com.example.rickmortyapp.model.Episode

class EpisodeAdapter(
    private var episodes: List<Episode>,
    private val onClick: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvEpisodeName)
        val tvCode: TextView = view.findViewById(R.id.tvEpisodeCode)
        val ivWatched: ImageView = view.findViewById(R.id.ivWatchedStatus)
        val container: View = view // Usamos la vista completa para el fondo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodeViewHolder(layoutInflater.inflate(R.layout.item_episode, parent, false))
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val item = episodes[position]
        holder.tvName.text = item.name
        holder.tvCode.text = item.episode

        // --- PASO 1: Lógica visual de "Visto" ---
        if (item.isWatched) {
            // Icono de estrella encendida
            holder.ivWatched.setImageResource(android.R.drawable.btn_star_big_on)
            // Fondo un poco gris para indicar que ya se "consumió"
            holder.container.setBackgroundColor(Color.parseColor("#F0F0F0"))
            holder.tvName.setTextColor(Color.GRAY)
        } else {
            // Icono de estrella apagada
            holder.ivWatched.setImageResource(android.R.drawable.btn_star_big_off)
            // Fondo blanco normal
            holder.container.setBackgroundColor(Color.WHITE)
            holder.tvName.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            // Al hacer clic, notificamos al Fragment
            onClick(item)
        }
    }

    override fun getItemCount(): Int = episodes.size

    fun updateEpisodes(newList: List<Episode>) {
        this.episodes = newList
        notifyDataSetChanged()
    }
}
package com.example.arview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.arview.data.GameInfo
import com.example.arview.databinding.ItemRecyclerBinding
import com.example.arview.network.dto.GamesResponse

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var gamesList = mutableListOf<GameInfo>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapter.ViewHolder {
        val binding =
            ItemRecyclerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return ViewHolder(binding)
    }

    fun update(gamesList: ArrayList<GameInfo>) {
        this.gamesList = gamesList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        holder?.bindData(gamesList!![position])
    }

    override fun getItemCount() = gamesList?.size ?: 0

    inner class ViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }

        fun bindData(gamesResponse: GameInfo) {
            val binding = binding as ItemRecyclerBinding

            binding.channelCount.text = gamesResponse.channel.toString()
            binding.gameName.text = gamesResponse.name
            binding.subCount.text = gamesResponse.viewer.toString()
            Glide.with(itemView.context).load(gamesResponse.image).into(binding.image)
        }

    }
}
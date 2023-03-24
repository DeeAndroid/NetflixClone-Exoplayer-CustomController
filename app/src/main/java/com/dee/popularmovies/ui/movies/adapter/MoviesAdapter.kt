package com.dee.popularmovies.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dee.popularmovies.data.res.ResultsItem
import com.dee.popularmovies.databinding.ItemMoviesBinding
import com.dee.popularmovies.ui.movies.listneres.OnItemClickListener
import com.dee.popularmovies.utils.loadImage

class MoviesAdapter(val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var modelArrayList: ArrayList<ResultsItem?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindUI(position, modelArrayList, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setModelArrayList(_modelArrayList: List<ResultsItem?>) {
        modelArrayList.addAll(_modelArrayList)
        notifyDataSetChanged()
    }


    class ViewHolder(val binding: ItemMoviesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUI(
            position: Int,
            _list: ArrayList<ResultsItem?>,
            onItemClickListener: OnItemClickListener,
        ) {
            binding.apply {
                movieDetails = _list[position]
                _list[position]?.posterPath?.let {
                    movieposter.loadImage("https://image.tmdb.org/t/p/w500/$it")
                }
                root.setOnClickListener {
                    onItemClickListener.onItemClick(_list[position], position)
                }
            }
        }
    }
}
package com.example.movies.ui.movieDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.model.Movie
import com.example.movies.util.formatDate
import com.example.movies.util.loadImage
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(
    private val onItemClick: ((Int) -> Unit)
) : ListAdapter<Movie, MoviesAdapter.ViewHolder>(
    MovieDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movie: Movie,
            onItemClick: (Int) -> Unit
        ) {
            with(itemView) {
                title.text = movie.title
                date.text = movie.releaseDate.formatDate()
                img.loadImage("https://image.tmdb.org/t/p/w500/${movie.imgPath}")

                img.setOnClickListener {
                    onItemClick(movie.id)
                }
                setOnClickListener {
                    onItemClick(movie.id)
                }
            }
        }
    }
}


class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
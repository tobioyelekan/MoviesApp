package com.example.movies.ui.movieDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.movies.R
import com.example.movies.data.helper.Resource.*
import com.example.movies.util.formatDate
import com.example.movies.util.loadImage
import com.example.movies.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovieDetails(args.movieId)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.observeMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
            it?.let { movieDetails ->
                img.loadImage("https://image.tmdb.org/t/p/w500/${movieDetails.imgPath}")
                title.text = movieDetails.title
                genre.text =
                    movieDetails.genres.joinToString(separator = ", ") { genre -> genre.name }
                overview.text = movieDetails.overview
                status.text = movieDetails.status
                budget.text = movieDetails.budget.toString()
                date.text = movieDetails.releaseDate.formatDate()
                revenue.text = movieDetails.revenue.toString()
            }
        })

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    loading.visibility = View.GONE
                }
                Status.ERROR -> {
                    showMessage(it.message!!)
                    loading.visibility = View.GONE
                }
            }
        })
    }
}
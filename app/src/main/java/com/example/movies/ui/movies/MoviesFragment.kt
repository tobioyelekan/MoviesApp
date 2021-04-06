package com.example.movies.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.data.helper.Resource.*
import com.example.movies.ui.movieDetails.MoviesAdapter
import com.example.movies.util.GridSpacingItemDecoration
import com.example.movies.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies.*

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val viewModel: MoviesViewModel by viewModels()

    private lateinit var adapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter { id ->
            val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            findNavController().navigate(direction)
        }

        movies.layoutManager = GridLayoutManager(requireContext(), 2)
        movies.addItemDecoration(GridSpacingItemDecoration(2, 60, true))
        movies.adapter = adapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.movieLoader.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    loading.visibility = if (adapter.currentList.isEmpty()) View.VISIBLE
                    else View.GONE
                }
                Status.SUCCESS -> {
                    loading.visibility = View.GONE
                }
                Status.ERROR -> {
                    loading.visibility = View.GONE
                    showMessage(it.message!!)
                }
            }
        })
    }
}
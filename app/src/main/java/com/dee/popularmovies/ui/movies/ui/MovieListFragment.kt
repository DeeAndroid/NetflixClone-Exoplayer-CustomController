package com.dee.popularmovies.ui.movies.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dee.popularmovies.R
import com.dee.popularmovies.data.res.ResultsItem
import com.dee.popularmovies.data.viewmodel.MainViewModel
import com.dee.popularmovies.databinding.FragmentMovieListBinding
import com.dee.popularmovies.di.utility.Resource
import com.dee.popularmovies.ui.movies.adapter.MoviesAdapter
import com.dee.popularmovies.ui.movies.listneres.OnItemClickListener
import com.dee.popularmovies.utils.showStatusNavigationBar
import kotlinx.coroutines.Job


class MovieListFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var page = 1
    private var topRatedpage = 1
    private var nowPlayingpage = 1
    private var isScroll = false
    private var istopRatedScroll = false
    private var isNowPlayingScroll = false
    private lateinit var adapter: MoviesAdapter
    private lateinit var topRatedadapter: MoviesAdapter
    private lateinit var nowPlayingadapter: MoviesAdapter
    private var totalPages: Int? = 0
    private var topRatedtotalPages: Int? = 0
    private var nowPlayingtotalPages: Int? = 0
    private lateinit var moviesjob: Job
    private lateinit var topRatedMoviesjob: Job
    private lateinit var nowPlayingMoviesjob: Job
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        activity?.showStatusNavigationBar()

        /** Initialising the adapters */
        adapter = MoviesAdapter(this)
        topRatedadapter = MoviesAdapter(this)
        nowPlayingadapter = MoviesAdapter(this)

        /** handling few ui things */
        binding.apply {

            /** setting up the adapter to the recyclerview */
            rvMovies.adapter = adapter
            rvTrending.adapter = topRatedadapter
            rvnowPlaying.adapter = nowPlayingadapter


            /** Initialising scroll listener for the recyclerviews */
            rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (rvMovies.isLastVisible() && isScroll) {
                        totalPages?.let {
                            if (page < it) {
                                page += 1
                                callMovieListApi()
                            }
                        }
                    }
                }
            })

            rvTrending.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (rvTrending.isLastVisible() && istopRatedScroll) {
                        topRatedtotalPages?.let {
                            if (topRatedpage < it) {
                                topRatedpage += 1
                                getTopRatedMovieList()
                            }
                        }
                    }
                }
            })

            rvnowPlaying.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (rvnowPlaying.isLastVisible() && isNowPlayingScroll) {
                        nowPlayingtotalPages?.let {
                            if (nowPlayingpage < it) {
                                nowPlayingpage += 1
                                getNowPlayingMovieList()
                            }
                        }
                    }
                }
            })
        }


        /** calling the API for the lists */
        callMovieListApi()
        getTopRatedMovieList()
        getNowPlayingMovieList()

        return binding.root
    }

    private fun getNowPlayingMovieList() {
        viewModel.nowPlayingMovieList(resources.getString(R.string.api_key), nowPlayingpage)
        geNowplayingMovieListResponse()
    }

    private fun geNowplayingMovieListResponse() {
        nowPlayingMoviesjob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.nowPlayingMovieList.collect() {
                isNowPlayingScroll = when (it) {
                    Resource.Empty -> {
                        false
                    }
                    is Resource.Failure -> {
                        false
                    }
                    Resource.Loading -> {
                        binding.latestShimmer.startShimmer()
                        false
                    }
                    is Resource.Success -> {
                        binding.latestShimmer.stopShimmer()
                        binding.latestShimmer.visibility = View.GONE
                        it.value.totalPages?.let { total_page ->
                            nowPlayingtotalPages = total_page
                        }
                        it.value.results?.let { it1 -> nowPlayingadapter.setModelArrayList(it1) }
                        nowPlayingMoviesjob.cancel()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }

    }

    private fun getTopRatedMovieList() {
        viewModel.topRatedMovieList(resources.getString(R.string.api_key), topRatedpage)
        getTopRatedMovieListResponse()
    }

    private fun getTopRatedMovieListResponse() {
        topRatedMoviesjob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.topRatedMovieList.collect() {
                istopRatedScroll = when (it) {
                    Resource.Empty -> {
                        false
                    }
                    is Resource.Failure -> {
                        false
                    }
                    Resource.Loading -> {
                        binding.trendingShimmer.startShimmer()
                        false
                    }
                    is Resource.Success -> {
                        binding.trendingShimmer.stopShimmer()
                        binding.trendingShimmer.visibility = View.GONE
                        it.value.totalPages?.let { total_page ->
                            topRatedtotalPages = total_page
                        }
                        it.value.results?.let { it1 -> topRatedadapter.setModelArrayList(it1) }
                        topRatedMoviesjob.cancel()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }

    }

    private fun callMovieListApi() {
        Log.d("TAG", "callMovieListApi: $page")
        viewModel.getMovieList(resources.getString(R.string.api_key), page)
        getMovieListResponse()
    }

    private fun getMovieListResponse() {
        moviesjob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.movieList.collect() {
                isScroll = when (it) {
                    Resource.Empty -> {
                        false
                    }
                    is Resource.Failure -> {
                        false
                    }
                    Resource.Loading -> {
                        binding.poularShimmer.startShimmer()
                        false
                    }
                    is Resource.Success -> {
                        binding.poularShimmer.stopShimmer()
                        binding.poularShimmer.visibility = View.GONE
                        it.value.totalPages?.let { total_page ->
                            totalPages = total_page
                        }

                        Log.d("TAG", "getMovieListResponse: ${it.value.results?.size}")
                        it.value.results?.let { it1 -> adapter.setModelArrayList(it1) }

                        moviesjob.cancel()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    fun RecyclerView.isLastVisible(): Boolean {
        binding.apply {
            val layoutManager = this@isLastVisible.layoutManager as LinearLayoutManager
            val pos = layoutManager.findLastCompletelyVisibleItemPosition()
            val numItems: Int = this@isLastVisible.adapter?.itemCount ?: 0
            return pos >= numItems - 1
        }
    }

    /** navigating to the other fragments on click of any item */
    override fun onItemClick(movielist: ResultsItem?, position: Int) {
        movielist?.let {
            findNavController().navigate(R.id.action_movieListFragment_to_playerFragment)
        }

    }

}
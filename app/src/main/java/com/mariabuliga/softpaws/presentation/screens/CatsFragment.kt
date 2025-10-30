package com.mariabuliga.softpaws.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mariabuliga.softpaws.R
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.databinding.FragmentCatsBinding
import com.mariabuliga.softpaws.presentation.CatAdapter
import com.mariabuliga.softpaws.presentation.CatInterface
import com.mariabuliga.softpaws.presentation.CatsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class CatsFragment : Fragment(), CatInterface {

    private var _binding: FragmentCatsBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by viewModels()

    private lateinit var adapter: CatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catsViewModel.loadInitialCats(requireContext())
        initAdapter()
        initRecyclerView(view)
        setupUI()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        binding.search.setQuery("", false)
        binding.search.clearFocus()
    }

    fun initAdapter() {
        adapter = CatAdapter(this)
    }

    fun initRecyclerView(view: View) {
        binding.petRecyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.petRecyclerView.adapter = adapter
    }

    fun setupUI() {
        binding.swipeRefresh.setOnRefreshListener {
            try {
                binding.swipeRefresh.isRefreshing = true
                catsViewModel.refreshCats(requireContext())
            } catch (e: IOException) {
                Snackbar.make(
                    binding.swipeRefresh,
                    e.message ?: "No internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Snackbar.make(binding.swipeRefresh, "Something went wrong $e", Snackbar.LENGTH_LONG)
                    .show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }

        }

        binding.petRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val shouldLoadMore =
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount - 3
                Log.d(
                    "Pagination",
                    "Scrolled: first=$firstVisibleItemPosition total=$totalItemCount"
                )

                if (shouldLoadMore && dy > 0) {
                    catsViewModel.loadNextPage(requireContext())
                }
            }
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    catsViewModel.searchCatByName(requireContext(), query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                when {
                    newText.isNullOrEmpty() -> {
                        catsViewModel.loadNextPage(requireContext())
                    }

                    newText.length >= 3 -> {
                        catsViewModel.searchCatByName(requireContext(), newText)
                    }
                }
                return true
            }
        })
    }

    private fun setupObservers() {
        catsViewModel.loadingLD.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        catsViewModel.catsLD.observe(viewLifecycleOwner) { cats ->
            if (cats != null && cats.isNotEmpty()) {
                adapter.cats = cats
                binding.errorLayout.root.visibility = View.GONE
            } else {
                binding.errorLayout.root.visibility = View.VISIBLE
            }
        }

        catsViewModel.emptyListLD.observe(viewLifecycleOwner) { empty ->
            if (empty) {
                binding.errorLayout.root.visibility = View.VISIBLE
                binding.petRecyclerView.visibility = View.GONE
            }
        }

        catsViewModel.errorLiveData.observe(viewLifecycleOwner) { pair ->
            if (pair.first) {
                Snackbar.make(requireView(), pair.second, Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onCatClick(cat: CatDataItem) {
        val bundle = Bundle()
        bundle.putSerializable("catItem", cat)

        findNavController().navigate(R.id.action_catsFragment_to_catDetailsFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
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
import com.mariabuliga.softpaws.R
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.databinding.FragmentCatsBinding
import com.mariabuliga.softpaws.presentation.CatAdapter
import com.mariabuliga.softpaws.presentation.CatInterface
import com.mariabuliga.softpaws.presentation.CatsViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        catsViewModel.loadInitialCats()
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
        binding.petRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val shouldLoadMore =
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount - 3
                Log.d("Pagination", "Scrolled: first=$firstVisibleItemPosition total=$totalItemCount")

                if (shouldLoadMore && dy> 0) {
                    catsViewModel.loadNextPage()
                }
            }
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    catsViewModel.searchCatByName(query)
                }
               return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
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
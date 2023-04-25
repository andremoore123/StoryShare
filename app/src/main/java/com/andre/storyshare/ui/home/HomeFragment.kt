package com.andre.storyshare.ui.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andre.storyshare.databinding.FragmentHomeBinding
import com.andre.storyshare.ui.adapter.PostRecyclerViewAdapter
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.home.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingBar: ProgressBar
    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: PostRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        loadingBar = binding.homeLoadingBar
        homeRecyclerView = binding.homeRecyclerView
        recyclerViewAdapter = PostRecyclerViewAdapter(requireContext(), emptyList())
        homeRecyclerView.adapter = recyclerViewAdapter
        homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        viewModel.getStories()
        viewModel.storyList.observe(viewLifecycleOwner){
            recyclerViewAdapter.updateData(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.message.observe(viewLifecycleOwner){
            showMessage(requireContext(),it)
        }
    }

    private fun showMessage(context: Context, errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) loadingBar.visibility = View.VISIBLE else loadingBar.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories()
    }
}
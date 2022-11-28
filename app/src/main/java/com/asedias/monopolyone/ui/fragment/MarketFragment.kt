package com.asedias.monopolyone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.asedias.monopolyone.databinding.FragmentRecyclerViewBinding
import com.asedias.monopolyone.ui.UIState
import com.asedias.monopolyone.ui.adapter.MarketAdapter
import com.asedias.monopolyone.ui.viewmodel.MarketViewModel
import com.asedias.monopolyone.util.setErrorCode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : Fragment() {

    private val viewModel: MarketViewModel by viewModels()
    private lateinit var marketAdapter: MarketAdapter

    companion object {
        fun newInstance() = MarketFragment()
    }

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marketAdapter = MarketAdapter()
        binding.recyclerView.apply {
            adapter = marketAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Show -> marketAdapter.differ.submitList(state.data.things)
                is UIState.Update -> marketAdapter.differ.submitList(state.data.things)
                is UIState.Error -> handleErrorView(state.code)
                is UIState.Loading -> binding.progressBar.isVisible = true
            }
        }
    }

    private fun handleErrorView(code: Int = 0, visibility: Int = View.VISIBLE) {
        handleProgress(View.INVISIBLE)
        binding.errorView.root.visibility = visibility
        binding.errorView.setErrorCode(code)
        binding.errorView.errorButton.setOnClickListener {
            viewModel.getLastSellups()
            handleErrorView(visibility = View.GONE)
            handleProgress()
        }
    }

    private fun handleProgress(visibility: Int = View.VISIBLE) {
        binding.progressBar.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
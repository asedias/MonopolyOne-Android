package com.example.material3test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.material3test.databinding.FragmentMarketBinding
import com.example.material3test.repository.MarketRepository
import com.example.material3test.setErrorCode
import com.example.material3test.ui.adapter.MarketAdapter
import com.example.material3test.ui.viewmodel.MarketViewModel
import com.example.material3test.ui.viewmodel.MarketViewModelProviderFactory
import com.haroldadmin.cnradapter.NetworkResponse

class MarketFragment : Fragment() {

    private val viewModel: MarketViewModel by viewModels() {
        MarketViewModelProviderFactory(MarketRepository())
    }
    private lateinit var marketAdapter: MarketAdapter

    companion object {
        fun newInstance() = MarketFragment()
    }

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marketAdapter = MarketAdapter()
        binding.rvMarketThings.apply {
            adapter = marketAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        viewModel.marketData.observe(viewLifecycleOwner) { market ->
            when (market) {
                is NetworkResponse.Success -> {
                    market.body.data?.let {
                        marketAdapter.differ.submitList(it.things)
                        handleProgress(View.INVISIBLE)
                    }
                }
                is NetworkResponse.ServerError -> {
                    market.body?.code.let {
                        handleErrorView(code = it!!)
                    }
                }
                is NetworkResponse.NetworkError -> {
                    handleErrorView(code = 0)
                }
                is NetworkResponse.UnknownError -> {
                    handleErrorView(code = 99)
                }
                else -> {
                    handleProgress()
                }
            }
        }
    }

    private fun handleErrorView(code: Int = 0, visibility: Int = View.VISIBLE) {
        handleProgress(View.INVISIBLE)
        binding.errorView.root.visibility = visibility
        binding.errorView.setErrorCode(code)
        binding.errorView.errorButton.setOnClickListener {
            viewModel.tryAgain()
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
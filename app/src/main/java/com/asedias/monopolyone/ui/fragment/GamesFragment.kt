package com.asedias.monopolyone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.asedias.monopolyone.databinding.FragmentRecyclerViewBinding
import com.asedias.monopolyone.ui.adapter.GamesSimpleAdapter
import com.asedias.monopolyone.ui.viewmodel.GamesViewModel
import com.asedias.monopolyone.util.setErrorCode
import kotlinx.coroutines.flow.collectLatest

class GamesFragment : Fragment() {

    private val viewModel: GamesViewModel by viewModels()
    private lateinit var adapter: GamesSimpleAdapter

    companion object {
        fun newInstance() = GamesFragment()
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
        adapter = GamesSimpleAdapter()
        binding.rvMarketThings.apply {
            adapter = this@GamesFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.games.collectLatest {
                adapter.setData(it)
            }
        }
    }

    private fun handleErrorView(
        code: Int = 0,
        visible: Boolean = true,
        click: () -> Unit = {}
    ) {
        binding.rvMarketThings.isVisible = !visible
        binding.errorView.root.isVisible = visible
        binding.errorView.setErrorCode(code)
        binding.errorView.errorButton.setOnClickListener {
            click()
            handleErrorView(visible = false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
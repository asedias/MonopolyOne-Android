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
import com.asedias.monopolyone.ui.UIState
import com.asedias.monopolyone.ui.adapter.MainFragmentAdapter
import com.asedias.monopolyone.ui.viewmodel.MainFragmentViewModel
import com.asedias.monopolyone.util.setErrorCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var adapter: MainFragmentAdapter

    companion object {
        fun newInstance() = MainFragment()
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
        adapter = MainFragmentAdapter()
        binding.recyclerView.apply {
            adapter = this@MainFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
        lifecycleScope.launch {
            viewModel.state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UIState.Show -> adapter.setData(state.data)
                    is UIState.Update -> adapter.setData(state.data)
                    is UIState.Error -> handleErrorView(state.code)
                    is UIState.Loading -> binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun handleErrorView(
        code: Int = 0,
        visible: Boolean = true,
        click: () -> Unit = {}
    ) {
        binding.recyclerView.isVisible = !visible
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
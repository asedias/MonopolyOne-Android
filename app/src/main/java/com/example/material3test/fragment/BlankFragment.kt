package com.example.material3test.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.material3test.viewmodels.BlankViewModel
import com.example.material3test.R
import com.example.material3test.databinding.FragmentBlankBinding
import com.example.material3test.fragment.`interface`.MaterialToolbarFragment
import com.example.material3test.fragment.`interface`.MaterialToolbarFragmentImpl

class BlankFragment : Fragment() {

    companion object {
        fun newInstance() = BlankFragment()
    }
    private lateinit var binding: FragmentBlankBinding
    private val viewModel: BlankViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlankBinding.inflate(inflater)
        viewModel.title.observe(viewLifecycleOwner) { newResID ->
            binding.blankText.setText(newResID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setTitle(R.string.login_desc)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.SettingsFragment)
        }
    }

}
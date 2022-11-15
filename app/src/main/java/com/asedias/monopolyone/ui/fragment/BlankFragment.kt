package com.asedias.monopolyone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asedias.monopolyone.databinding.HeaderGamesBinding
import com.asedias.monopolyone.ui.viewmodel.BlankViewModel

class BlankFragment : Fragment() {

    companion object {
        fun newInstance() = BlankFragment()
    }
    private var _binding: HeaderGamesBinding? = null
    private val viewModel: BlankViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HeaderGamesBinding.inflate(inflater, container, false)
        /*viewModel.title.observe(viewLifecycleOwner) { newResID ->
            binding.blankText.setText(newResID)
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.setTitle(R.string.login_desc)
        binding.chatButton.setOnClickListener {
            //findNavController().navigate(R.id.LoginFragment)
            val modalBottomSheet = LoginBottomSheet()
            activity?.supportFragmentManager?.let { it -> modalBottomSheet.show(it, LoginBottomSheet.TAG) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
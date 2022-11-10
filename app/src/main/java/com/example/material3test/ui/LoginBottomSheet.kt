package com.example.material3test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.material3test.databinding.SheetLoginBinding
import com.example.material3test.repository.AuthRepository
import com.example.material3test.ui.viewmodel.LoginViewModel
import com.example.material3test.util.Auth
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.cnradapter.NetworkResponse

class LoginBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SheetLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels()

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetLoginBinding.inflate(inflater, container, false)


        viewModel.loading.observe(viewLifecycleOwner) {
            val enabled = viewModel.loading.value == false
            binding.button2.isEnabled = enabled
            binding.button3.isEnabled = enabled
            binding.loginInputLayout.isEnabled = enabled
            binding.passInputLayout.isEnabled = enabled
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val email = binding.email.text.toString()
                val password = binding.pass.text.toString()
                viewModel.setIsLoading(true)
                when(val auth = AuthRepository().signIn(email, password)) {
                    is NetworkResponse.Success -> {
                        println(auth.toString())
                        auth.body.data?.let { it1 ->
                            Auth().authorize(it1)
                        }
                        requireDialog().dismiss()
                        }
                    is NetworkResponse.ServerError -> {
                        println("Server Error Code(${auth.body?.code}): ${auth.body?.description}")
                    }
                    is NetworkResponse.NetworkError -> {
                        println("Network Error Code(${auth.body?.code}): ${auth.body?.description}")
                    }
                    is NetworkResponse.UnknownError -> {
                        println("Unknown Error Code(${auth.body?.code}): ${auth.body?.description}")
                    }
                }
                viewModel.setIsLoading(false)
            }
        }
    }

    companion object {
        const val TAG = "LoginBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
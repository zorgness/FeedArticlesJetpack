package com.example.feedarticlesjetpack.fragment

import STATUS_USER_SUCCESS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentLoginBinding
import com.example.feedarticlesjetpack.databinding.FragmentRegisterBinding
import com.example.feedarticlesjetpack.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val myViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }


        myViewModel.statusLiveData.observe(this) { status ->
            (status == STATUS_USER_SUCCESS).run {
                val navDir = RegisterFragmentDirections.actionRegisterFragmentToMainFragment()
                findNavController().navigate(navDir)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnRegister.setOnClickListener {
            myViewModel.register(
                binding.loginData ?: "",
                binding.passwordData ?: "",
                binding.confirmPasswordData ?: ""
            )
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
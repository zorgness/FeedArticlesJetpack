package com.example.feedarticlesjetpack.fragment

import STATUS_USER_SUCCESS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentLoginBinding
import com.example.feedarticlesjetpack.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val myViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }


        myViewModel.statusLiveData.observe(this) { status ->
            if (status == STATUS_USER_SUCCESS) {
                val navDir = LoginFragmentDirections.actionLoginFragmentToMainFragment()
                findNavController().navigate(navDir)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.tvLoginToRegister.setOnClickListener {
            val navDir = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view?.findNavController()?.navigate(navDir)
        }

        binding.btnLogin.setOnClickListener {
            myViewModel.login(binding.loginData ?: "", binding.passwordData ?: "")
        }
        return binding.root
    }
}
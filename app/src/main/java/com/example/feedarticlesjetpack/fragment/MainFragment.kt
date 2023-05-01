package com.example.feedarticlesjetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentMainBinding
import com.example.feedarticlesjetpack.viewmodel.MainFragmentViewModel
import com.example.feedarticlesjetpack.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import myToast

@AndroidEntryPoint
class MainFragment : Fragment() {


    private val myViewModel: MainFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        myViewModel.isLogoutInLiveData.observe(this) { isLogout ->
            if (isLogout) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(layoutInflater)

        binding.btnLogout.setOnClickListener {
            myViewModel.logout()
        }

        return binding.root
    }

}
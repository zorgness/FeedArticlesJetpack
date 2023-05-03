package com.example.feedarticlesjetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.common.dateFormater
import com.example.feedarticlesjetpack.common.getCategoryById
import com.example.feedarticlesjetpack.databinding.FragmentDetailsArticleBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast
import com.example.feedarticlesjetpack.viewmodel.SharedViewModel

@AndroidEntryPoint
class DetailsArticleFragment : Fragment() {

    private var _binding: FragmentDetailsArticleBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args: DetailsArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel.getArticleById(args.articleId)

        sharedViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.askForRefreshArticlesList()
                findNavController().navigate(R.id.mainFragment)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsArticleBinding.inflate(inflater, container, false)

        binding.btnReturn.setOnClickListener {
            sharedViewModel.askForRefreshArticlesList()
            findNavController().navigate(R.id.mainFragment)
        }

        sharedViewModel.articleLiveData.observe(viewLifecycleOwner) { article ->
            binding.tvArticleTitle.text = article.titre
            binding.tvArticleDescription.text = article.descriptif
            binding.tvArticleDate.text =
                getString(R.string.created_at).format(dateFormater(article.createdAt))
            binding.tvArticleCategory.text =
                getString(R.string.categorie).format(getCategoryById(article.categorie)?.name)

            if (article.urlImage.isEmpty()) {
                Picasso.get()
                    .load(android.R.color.transparent)
                    .resize(300, 300)
                    .into(binding.ivArticleImage)
            } else {
                Picasso.get()
                    .load(article.urlImage).error(R.drawable.feedarticles_logo)
                    .resize(300, 300)
                    .into(binding.ivArticleImage)
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.feedarticlesjetpack.fragment

import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import STATUS_NEW_ARTICLE_SUCCESS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentNewEditArticleBinding
import com.example.feedarticlesjetpack.viewmodel.NewEditFragmentViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast

@AndroidEntryPoint
class NewEditArticleFragment : Fragment() {

    private val args: NewEditArticleFragmentArgs by navArgs()
    private val myViewModel: NewEditFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }


        myViewModel.statusLiveData.observe(this) { status ->
            if (status == STATUS_NEW_ARTICLE_SUCCESS) {
                findNavController().navigate(R.id.mainFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNewEditArticleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_edit_article, container, false)

        if(args.articleId > 0) {
            binding.tvNewEdit.text = "Edition Article"
        }

        //FIND ANOTHER SOLUTION WITH DATA BINDING
        binding.etImageUrl.onFocusChangeListener = View.OnFocusChangeListener { _, isFocus ->
            val urlImageToVisualize = binding.etImageUrl.text.toString().trim { it <= ' ' }
            if (urlImageToVisualize.isNotEmpty() && !isFocus) {
                Picasso.get()
                    .load(urlImageToVisualize)
                    .resize(300, 300)
                    .error(R.drawable.feedarticles_logo)
                    .into(binding.ivArticleImage)

            } else if (urlImageToVisualize.isEmpty()) {
                Picasso.get()
                    .load(R.drawable.feedarticles_logo)
                    .resize(300, 300)
                    .into(binding.ivArticleImage)
            }
        }
        //FIND ANOTHER SOLUTION WITH DATA BINDING

        fun getCategoryIdRadioButton(checkedId: Int) =
            when (checkedId) {
                binding.radioSport.id -> ID_SPORT_CATEGORY
                binding.radioManga.id -> ID_MANGA_CATEGORY
                binding.radioDivers.id -> ID_DIVERS_CATEGORY
                else -> ID_DIVERS_CATEGORY
            }

        binding.radioGroup.setOnCheckedChangeListener() { _, checkedId ->
            myViewModel.getCheckedCategory(getCategoryIdRadioButton(checkedId))
        }

        binding.btnSaveArticle.setOnClickListener {
           myViewModel.newArticle(
               binding.titleData ?: "",
               binding.descriptionData ?: "",
               binding.imageUrlData ?: ""
           )
        }

        return binding.root
    }

}
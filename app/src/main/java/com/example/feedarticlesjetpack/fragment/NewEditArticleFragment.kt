package com.example.feedarticlesjetpack.fragment

import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentNewEditArticleBinding
import com.example.feedarticlesjetpack.viewmodel.NewEditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewEditArticleFragment : Fragment() {


    private val myViewModel: NewEditFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNewEditArticleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_edit_article, container, false)

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
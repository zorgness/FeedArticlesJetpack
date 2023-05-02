package com.example.feedarticlesjetpack.fragment

import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import STATUS_NEW_EDIT_ARTICLE_SUCCESS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
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
import com.example.feedarticlesjetpack.viewmodel.DetailsArticleFragmentViewModel
import com.example.feedarticlesjetpack.viewmodel.MainFragmentViewModel
import com.example.feedarticlesjetpack.viewmodel.SharedViewModel

@AndroidEntryPoint
class NewEditArticleFragment : Fragment() {

    private val args: NewEditArticleFragmentArgs by navArgs()
    private val myViewModel: NewEditFragmentViewModel by viewModels()
    private val detailsViewModel: DetailsArticleFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        myViewModel.statusLiveData.observe(this) { status ->
            if (status == STATUS_NEW_EDIT_ARTICLE_SUCCESS) {
                sharedViewModel.askForRefreshArticlesList()
                findNavController().navigate(R.id.mainFragment)
            }
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
        val binding: FragmentNewEditArticleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_edit_article, container, false)

        fun getCategoryIdRadioButton(checkedId: Int) =
            when (checkedId) {
                binding.radioSport.id -> ID_SPORT_CATEGORY
                binding.radioManga.id -> ID_MANGA_CATEGORY
                binding.radioDivers.id -> ID_DIVERS_CATEGORY
                else -> ID_DIVERS_CATEGORY
            }

        fun radioBtnCheckedByCategoryId(categoryId: Int) =
            when (categoryId) {
                ID_SPORT_CATEGORY -> binding.radioSport.id
                ID_MANGA_CATEGORY -> binding.radioManga.id
                ID_DIVERS_CATEGORY -> binding.radioDivers.id
                else -> binding.radioDivers.id
            }

        binding.radioGroup.setOnCheckedChangeListener() { _, checkedId ->
            myViewModel.getCheckedCategory(getCategoryIdRadioButton(checkedId))
        }

        if (args.articleId > 0) {
            binding.tvNewEdit.text = getString(R.string.edit_article_title)
            binding.btnSaveArticle.visibility = View.GONE

            detailsViewModel.getArticleById(args.articleId)

            detailsViewModel.articleLiveData.observe(viewLifecycleOwner) { article ->
                binding.etTitleArticle.setText(article.titre)
                binding.etDescriptionArticle.setText(article.descriptif)
                binding.etImageUrl.setText(article.urlImage)
                binding.radioGroup.check(radioBtnCheckedByCategoryId(article.categorie))

                //FIND ANOTHER SOLUTION
                if (binding.etImageUrl.text.isNotEmpty()) {
                    Picasso.get()
                        .load(binding.etImageUrl.text.toString())
                        .resize(300, 300)
                        .error(R.drawable.feedarticles_logo)
                        .into(binding.ivArticleImage)

                } else if (binding.etImageUrl.text.isEmpty()) {
                    Picasso.get()
                        .load(R.drawable.feedarticles_logo)
                        .resize(300, 300)
                        .into(binding.ivArticleImage)
                }
                //FIND ANOTHER SOLUTION


                binding.btnEditArticle.setOnClickListener {
                    myViewModel.updateArticle(
                        article.id,
                        binding.titleData ?: "",
                        binding.descriptionData ?: "",
                        binding.imageUrlData ?: ""
                    )
                }

                binding.btnDeleteArticle.setOnClickListener {

                }
            }
        } else {
            binding.btnGroupUpdateDelete.visibility = View.GONE

            binding.btnSaveArticle.setOnClickListener {
                myViewModel.newArticle(
                    binding.titleData ?: "",
                    binding.descriptionData ?: "",
                    binding.imageUrlData ?: ""
                )
            }
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


        return binding.root
    }

}
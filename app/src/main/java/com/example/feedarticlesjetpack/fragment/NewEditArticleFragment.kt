package com.example.feedarticlesjetpack.fragment

import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import STATUS_MUTATION_ARTICLE_SUCCESS
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.common.myPicassoFun
import com.example.feedarticlesjetpack.databinding.FragmentNewEditArticleBinding
import com.example.feedarticlesjetpack.extensions.isPositive
import com.example.feedarticlesjetpack.viewmodel.NewEditFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast
import com.example.feedarticlesjetpack.viewmodel.SharedViewModel

@AndroidEntryPoint
class NewEditArticleFragment : Fragment() {

    private var _binding: FragmentNewEditArticleBinding? = null
    private val binding get() = _binding!!
    private val args: NewEditArticleFragmentArgs by navArgs()
    private val myViewModel: NewEditFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        myViewModel.statusLiveData.observe(this) { status ->
            (status == STATUS_MUTATION_ARTICLE_SUCCESS).run {
                sharedViewModel.requestForRefreshArticlesList()
                findNavController().navigate(
                    R.id.mainFragment, null,
                    NavOptions.Builder().setPopUpTo(
                        R.id.mainFragment, true
                    ).build()
                )
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.requestForRefreshArticlesList()
                findNavController().navigate(R.id.mainFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_edit_article, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = myViewModel


        if (args.articleId.isPositive) {
            binding.tvNewEdit.text = getString(R.string.edit_article_title)
            binding.btnSaveArticle.visibility = View.GONE

            sharedViewModel.getArticleById(args.articleId)

            sharedViewModel.articleLiveData.observe(viewLifecycleOwner) { article ->

                //FILL FORM FOR UPDATE
                myViewModel.getArticleId(article.id)
                binding.etTitleArticle.setText(article.titre)
                binding.etDescriptionArticle.setText(article.descriptif)
                binding.etImageUrl.setText(article.urlImage)
                binding.radioGroup.check(radioBtnCheckedByCategoryId(article.categorie))
                myPicassoFun(binding.etImageUrl.text.toString(), binding.ivArticleImage)


                binding.btnDeleteArticle.setOnClickListener {
                  AlertDialog.Builder(context).apply {
                        setMessage(R.string.are_you_sure_to_delete)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes) { _, _ ->
                                myViewModel.deleteArticle(article.id)
                            }
                            .setNegativeButton(R.string.no) { dialog, _ ->
                                dialog.dismiss()
                            }
                    }.run {
                        create()
                        show()
                    }
                }
            }
        } else
            binding.btnGroupUpdateDelete.visibility = View.GONE

        binding.etImageUrl.onFocusChangeListener = View.OnFocusChangeListener { _, isFocus ->
            val urlImageToVisualize = binding.etImageUrl.text.toString().trim { it <= ' ' }
            (!isFocus).run {
                myPicassoFun(urlImageToVisualize, binding.ivArticleImage)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            myViewModel.getCheckedCategory(getCategoryIdByRadioBtn(checkedId))
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategoryIdByRadioBtn(checkedId: Int) =
        when (checkedId) {
            R.id.radio_sport -> ID_SPORT_CATEGORY
            R.id.radio_manga -> ID_MANGA_CATEGORY
            R.id.radio_divers -> ID_DIVERS_CATEGORY
            else -> ID_DIVERS_CATEGORY
        }

    private fun radioBtnCheckedByCategoryId(categoryId: Int) =
        when (categoryId) {
            ID_SPORT_CATEGORY -> R.id.radio_sport
            ID_MANGA_CATEGORY -> R.id.radio_manga
            ID_DIVERS_CATEGORY -> R.id.radio_divers
            else -> R.id.radio_divers
        }

}
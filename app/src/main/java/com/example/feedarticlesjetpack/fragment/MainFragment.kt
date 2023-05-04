package com.example.feedarticlesjetpack.fragment

import ID_ALL_CATEGORY
import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.adapter.ArticleAdapter
import com.example.feedarticlesjetpack.common.getStarIcon
import com.example.feedarticlesjetpack.databinding.FragmentMainBinding
import com.example.feedarticlesjetpack.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.feedarticlesjetpack.extensions.myToast
import com.example.feedarticlesjetpack.viewmodel.SharedViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val myViewModel: MainFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.articlesLiveData.observe(this) { articles ->
            binding.swipeContainer.isRefreshing = false;
            articleAdapter.submitList(articles)
        }

        myViewModel.categoryIdLiveData.observe(this) { categoryId ->
            binding.radioGroup.check(radioBtnCheckedByCategoryId(categoryId))
        }

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        myViewModel.progressBarVisibilityLiveData.observe(this) { visibility ->
            binding.progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
        }

        myViewModel.isFavFilterLiveData.observe(this) { isFiltered ->
            binding.ivBtnFavoriteFilter.setImageResource(getStarIcon(isFiltered))
        }

        // NAV OPTIONS IS FOR CLEARING ALL BACKSTACK AFTER LOGOUT
        myViewModel.isLogoutLiveData.observe(this) { isLogout ->
            (isLogout).run {
                findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(
                        R.id.nav_graph, true
                    ).build()
                )
            }
        }

        //REQUEST FROM FORM FRAGMENT TO REFRESH ARTICLE
        sharedViewModel.refreshListLiveData.observe(this) { refreshList ->
            (refreshList).run {
                myViewModel.getArticlesListForArticleAdapter()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.rvArticles.layoutManager = LinearLayoutManager(container?.context)
        articleAdapter = ArticleAdapter()
        binding.rvArticles.adapter = articleAdapter


        binding.btnLogout.setOnClickListener {
            myViewModel.logout()
        }

        binding.btnNewArticle.setOnClickListener {
            val navDir = MainFragmentDirections.actionMainFragmentToNewEditArticleFragment()
            findNavController().navigate(navDir)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            myViewModel.getCheckedCategory(getCategoryIdByRadioButton(checkedId))
        }

        binding.ivBtnFavoriteFilter.setOnClickListener {
            myViewModel.setFavoriteFilter()
        }

        binding.swipeContainer.setOnRefreshListener {
            myViewModel.getArticlesListForArticleAdapter()
        }

        return binding.root
    }

    private fun getCategoryIdByRadioButton(checkedBtnId: Int) =
        when (checkedBtnId) {
            R.id.radio_all -> ID_ALL_CATEGORY
            R.id.radio_sport-> ID_SPORT_CATEGORY
            R.id.radio_manga-> ID_MANGA_CATEGORY
            R.id.radio_divers -> ID_DIVERS_CATEGORY
            else -> ID_ALL_CATEGORY
        }

    private fun radioBtnCheckedByCategoryId(categoryId: Int) =
        when (categoryId) {
            ID_SPORT_CATEGORY -> R.id.radio_sport
            ID_MANGA_CATEGORY -> R.id.radio_manga
            ID_DIVERS_CATEGORY -> R.id.radio_divers
            else -> R.id.radio_all
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
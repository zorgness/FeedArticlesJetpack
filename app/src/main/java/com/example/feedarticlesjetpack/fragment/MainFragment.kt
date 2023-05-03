package com.example.feedarticlesjetpack.fragment

import ID_ALL_CATEGORY
import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.adapter.ArticleAdapter
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
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myViewModel.articlesLiveData.observe(this) { articles ->
            articleAdapter.submitList(articles)
        }

        myViewModel.messageLiveData.observe(this) { message ->
            activity?.myToast(message)
        }

        myViewModel.progressBarVisibilityLiveData.observe(this) { visibility ->
            progressBar.visibility = if (visibility) View.VISIBLE else View.GONE
        }


        // TODO explication
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

        // TODO explication
        sharedViewModel.refreshListLiveData.observe(this) { refreshList ->
            (refreshList).run {
                myViewModel.getAllArticles()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        progressBar = binding.pbCyclic

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

        fun getCategoryIdByRadioButton(checkedId: Int) =
            when (checkedId) {
                binding.radioAll.id -> ID_ALL_CATEGORY
                binding.radioSport.id -> ID_SPORT_CATEGORY
                binding.radioManga.id -> ID_MANGA_CATEGORY
                binding.radioDivers.id -> ID_DIVERS_CATEGORY
                else -> ID_ALL_CATEGORY
            }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            myViewModel.getCheckedCategory(getCategoryIdByRadioButton(checkedId))
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
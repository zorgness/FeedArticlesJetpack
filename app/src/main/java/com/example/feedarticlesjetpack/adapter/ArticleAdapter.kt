package com.example.feedarticlesjetpack.adapter

import FAVORITE
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_USER_ID
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.example.feedarticlesjetpack.common.dateFormatter
import com.example.feedarticlesjetpack.common.getCategoryById
import com.example.feedarticlesjetpack.common.myPicassoFun
import com.example.feedarticlesjetpack.fragment.MainFragmentDirections
import javax.inject.Singleton


class ArticleDiffUtil : DiffUtil.ItemCallback<ArticleDto>() {
    override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem == newItem
    }

}
@Singleton
class ArticleAdapter:  ListAdapter<ArticleDto, ArticleAdapter.ArticleViewHolder>(ArticleDiffUtil()){

    lateinit var navDir: NavDirections
    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ArticleViewHolder{


        context = parent.context
        return LayoutInflater.from(parent.context).inflate(R.layout.item_rv_article, parent, false)
            .run {
                ArticleViewHolder(this)
            }

    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleViewHolder, position: Int) {

        val article: ArticleDto = getItem(position)

        with(holder.binding) {

            rlArticle.background = getCategoryById(article.categorie)?.color?.let { ContextCompat.getDrawable(rlArticle.context, it) }

            tvArticleTitle.text = article.titre
            tvArticleDate.text = dateFormatter(article.createdAt)
            tvArticleDescription.text = article.descriptif

            ivFavorite.visibility = if(article.isFav == FAVORITE) View.VISIBLE else View.GONE

            myPicassoFun(article.urlImage, ivArticleItem)

            rlArticle.setOnClickListener {view->
                with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
                    navDir = if(article.idU == getLong(SHAREDPREF_SESSION_USER_ID, 0)) {
                        MainFragmentDirections.actionMainFragmentToNewEditArticleFragment(article.id)
                    } else {
                        MainFragmentDirections.actionMainFragmentToDetailsArticleFragment(article.id)
                    }
                    view.findNavController().navigate(navDir)
                }

            }
        }
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRvArticleBinding.bind(itemView)
    }
}
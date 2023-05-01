package com.example.feedarticlesjetpack.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.squareup.picasso.Picasso
import com.example.feedarticlesjetpack.common.dateFormater
import com.example.feedarticlesjetpack.common.getCategoryById
import java.security.AccessController.getContext
import javax.inject.Inject
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ArticleViewHolder{


        return LayoutInflater.from(parent.context).inflate(R.layout.item_rv_article, parent, false)
            .run {
                ArticleViewHolder(this)
            }

    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleViewHolder, position: Int) {

        val article: ArticleDto = getItem(position)

        with(holder.binding) {
            tvArticleTitle.text = article.titre
            tvArticleDate.text = dateFormater(article.createdAt) //toFormatDate
            tvArticleDescription.text = article.descriptif

            val color = getCategoryById(article.categorie)?.color

            //println("color: $color")

            rlArticle.background = color?.let { ContextCompat.getDrawable(rlArticle.context, it) }

            if (article.urlImage.isEmpty()) {
                Picasso.get()
                    .load(android.R.color.transparent)
                    .resize(300, 300)
                    .into(ivArticleItem)
            } else {
                Picasso.get()
                    .load(article.urlImage).error(R.drawable.feedarticles_logo)
                    .resize(300, 300)
                    .into(ivArticleItem)
            }
        }
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRvArticleBinding.bind(itemView)

    }
}
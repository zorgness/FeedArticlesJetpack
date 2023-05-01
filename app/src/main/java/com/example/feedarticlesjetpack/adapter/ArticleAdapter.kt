package com.example.feedarticlesjetpack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.squareup.picasso.Picasso
import dateFormater

class ArticleDiffUtil : DiffUtil.ItemCallback<ArticleDto>() {
    override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem == newItem
    }

}
class ArticleAdapter ():  ListAdapter<ArticleDto, ArticleAdapter.ArticleViewHolder>(ArticleDiffUtil()){

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
package com.example.feedarticlesjetpack.common

import DIVERS_CATEGORY
import ID_DIVERS_CATEGORY
import ID_MANGA_CATEGORY
import ID_SPORT_CATEGORY
import MANGA_CATEGORY
import SPORT_CATEGORY
import STATUS_ARTICLES_ERROR_CON
import STATUS_ARTICLES_PARAM_PROBLEM
import STATUS_ARTICLES_SUCCESS
import STATUS_ARTICLES_UNAUTH
import STATUS_MUTATION_ARTICLE_FAILURE
import STATUS_MUTATION_ARTICLE_ID_PROBLEM
import STATUS_MUTATION_ARTICLE_PARAM_PROBLEM
import STATUS_MUTATION_ARTICLE_SUCCESS
import STATUS_MUTATION_ARTICLE_UNAUTHORIZED
import STATUS_USER_ALREADY_LOGGED
import STATUS_USER_FAILURE
import STATUS_USER_PARAMETERS_PROBLEM
import STATUS_USER_SUCCESS
import android.content.Context
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.dataclass.CategoryData
import java.text.SimpleDateFormat

fun responseRegisterStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_USER_SUCCESS -> context.getString(R.string.user_register)
        STATUS_USER_FAILURE -> context.getString(R.string.user_not_register)
        STATUS_USER_PARAMETERS_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_USER_ALREADY_LOGGED -> context.getString(R.string.login_already_used)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseLoginStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_USER_SUCCESS -> context.getString(R.string.user_athenticated)
        STATUS_USER_FAILURE -> context.getString(R.string.user_not_authenticated)
        STATUS_USER_PARAMETERS_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_USER_ALREADY_LOGGED -> context.getString(R.string.token_problem)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseArticlesStatus(status: String?, context: Context): String =
    when (status) {
        STATUS_ARTICLES_SUCCESS -> ""
        STATUS_ARTICLES_UNAUTH -> context.getString(R.string.articles_unauthorized)
        STATUS_ARTICLES_PARAM_PROBLEM -> context.getString(R.string.articles_param_problem)
        STATUS_ARTICLES_ERROR_CON -> context.getString(R.string.articles_error_connection)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseNewArticleStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_MUTATION_ARTICLE_SUCCESS -> context.getString(R.string.new_article_success)
        STATUS_MUTATION_ARTICLE_FAILURE -> context.getString(R.string.new_article_failure)
        STATUS_MUTATION_ARTICLE_PARAM_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_MUTATION_ARTICLE_UNAUTHORIZED -> context.getString(R.string.unauthorized)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseUpdateArticleStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_MUTATION_ARTICLE_SUCCESS -> context.getString(R.string.update_article_success)
        STATUS_MUTATION_ARTICLE_FAILURE -> context.getString(R.string.update_article_failure)
        STATUS_MUTATION_ARTICLE_PARAM_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_MUTATION_ARTICLE_ID_PROBLEM -> context.getString(R.string.update_article_id_problem)
        STATUS_MUTATION_ARTICLE_UNAUTHORIZED -> context.getString(R.string.unauthorized)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseDeleteArticleStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_MUTATION_ARTICLE_SUCCESS -> context.getString(R.string.article_deleted)
        STATUS_MUTATION_ARTICLE_FAILURE -> context.getString(R.string.article_not_deleted)
        STATUS_MUTATION_ARTICLE_PARAM_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_MUTATION_ARTICLE_UNAUTHORIZED -> context.getString(R.string.unauthorized)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseFavoriteStateArticleStatus(status: Int?, context: Context): String =
    when (status) {
        STATUS_MUTATION_ARTICLE_SUCCESS -> context.getString(R.string.status_favorite_changed)
        STATUS_MUTATION_ARTICLE_FAILURE -> context.getString(R.string.status_favorite_not_changed)
        STATUS_MUTATION_ARTICLE_PARAM_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_MUTATION_ARTICLE_UNAUTHORIZED -> context.getString(R.string.unauthorized)
        else -> context.getString(R.string.something_went_wrong)
    }


//TO IMPROVE
fun dateFormater(dateStr: String): String {

    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val dateFormated = parser.parse(dateStr)?.let { formatter.format(it) }
    return dateFormated.toString()
}

val categories = listOf(
    CategoryData(ID_SPORT_CATEGORY, SPORT_CATEGORY, R.color.sport),
    CategoryData(ID_MANGA_CATEGORY, MANGA_CATEGORY, R.color.manga),
    CategoryData(ID_DIVERS_CATEGORY, DIVERS_CATEGORY, R.color.divers)
)

fun getCategoryById(categoryId: Int): CategoryData? =
    categories.find { element ->
        element.id == categoryId
    }

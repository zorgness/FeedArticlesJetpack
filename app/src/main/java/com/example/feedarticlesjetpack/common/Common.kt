import android.content.Context
import com.example.feedarticlesjetpack.R

fun responseRegisterStatus(status: Int?, context: Context): String =

    when(status) {
        STATUS_USER_SUCCESS -> context.getString(R.string.user_register)
        STATUS_USER_FAILURE -> context.getString(R.string.user_not_register)
        STATUS_USER_PARAMETERS_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_USER_ALREADY_LOGGED-> context.getString(R.string.login_already_used)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseLoginStatus(status: Int?, context: Context): String =

    when(status) {
        STATUS_USER_SUCCESS -> context.getString(R.string.user_athenticated)
        STATUS_USER_FAILURE -> context.getString(R.string.user_not_authenticated)
        STATUS_USER_PARAMETERS_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_USER_ALREADY_LOGGED-> context.getString(R.string.token_problem)
        else -> context.getString(R.string.something_went_wrong)
    }

fun responseArticlesStatus(status: String?, context: Context): String =
    when(status) {
        STATUS_ARTICLES_SUCCESS -> context.getString(R.string.welcome)
        STATUS_ARTICLES_UNAUTH -> context.getString(R.string.articles_unauthorized)
        STATUS_ARTICLES_PARAM_PROBLEM -> context.getString(R.string.articles_param_problem)
        STATUS_ARTICLES_ERROR_CON -> context.getString(R.string.articles_error_connection)
        else -> context.getString(R.string.something_went_wrong)
    }
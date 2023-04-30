import android.content.Context
import com.example.feedarticlesjetpack.R

fun responseUserStatus(status: Int?, action: String, context: Context): String =

    when(status) {
        STATUS_USER_SUCCESS -> "${context.getString(R.string.user)} $action"
        STATUS_USER_FAILURE -> "${context.getString(R.string.user_not)} $action"
        STATUS_USER_PARAMETERS_PROBLEM -> context.getString(R.string.parameters_problem)
        STATUS_USER_ALREADY_LOGGED-> context.getString(R.string.unauthorized)
        else -> context.getString(R.string.something_went_wrong)
    }
const val SHAREDPREF_NAME = "feedarticle"
const val SHAREDPREF_SESSION_TOKEN = "session token"
const val SHAREDPREF_SESSION_USER_ID = "session user id"

const val ID_ALL_CATEGORY = 0
const val ID_SPORT_CATEGORY = 1
const val ID_MANGA_CATEGORY = 2
const val ID_DIVERS_CATEGORY = 3

const val SPORT_CATEGORY = "sport"
const val MANGA_CATEGORY = "manga"
const val DIVERS_CATEGORY = "divers"

const val STATUS_USER_SUCCESS = 1
const val STATUS_USER_FAILURE = 0
const val STATUS_USER_PARAMETERS_PROBLEM = -1
const val STATUS_USER_ALREADY_LOGGED = 5

const val STATUS_NEW_EDIT_ARTICLE_SUCCESS = 1
const val STATUS_NEW_EDIT_ARTICLE_FAILURE = 0
const val STATUS_NEW_EDIT_ARTICLE_ID_PROBLEM = 2
const val STATUS_NEW_EDIT_ARTICLE_PARAM_PROBLEM = -1
const val STATUS_NEW_EDIT_ARTICLE_UNAUTHORIZED = 5

const val STATUS_ARTICLES_SUCCESS = "ok"
const val STATUS_ARTICLES_UNAUTH = "unauthorized"
const val STATUS_ARTICLES_PARAM_PROBLEM = "error_param"
const val STATUS_ARTICLES_ERROR_CON = "error_connection"


const val WITH_FAVORITES = 1
const val WITHOUT_FAVORITES = 0
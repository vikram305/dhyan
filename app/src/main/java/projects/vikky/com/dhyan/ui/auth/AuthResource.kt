package projects.vikky.com.dhyan.ui.auth

class AuthResource<T>(val status: AuthStatus, val data: T?, val message: String?) {

    enum class AuthStatus {
        AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED, LOGGING_OUT
    }

    companion object {

        fun <T> authenticated(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.AUTHENTICATED, data, null)
        }

        fun <T> error(msg: String, data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.LOADING, data, null)
        }

        fun <T> logout(): AuthResource<T> {
            return AuthResource(AuthStatus.NOT_AUTHENTICATED, null, null)
        }

        fun <T> loggingout(): AuthResource<T> {
            return AuthResource(AuthStatus.LOGGING_OUT, null, null)
        }
    }

}
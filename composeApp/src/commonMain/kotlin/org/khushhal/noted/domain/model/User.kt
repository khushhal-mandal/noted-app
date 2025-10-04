package org.khushhal.noted.domain.model

data class User (
    val email: String? = null,
    val password: String? = null,
    var accessToken: String? = null
)

object CurrentUser {
    var user: User? = null
}
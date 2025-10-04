package org.khushhal.noted

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
package com.enrrolato.enrrolato.database

import com.google.firebase.database.FirebaseDatabase

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

object Enrrolato {
    val database = FirebaseDatabase.getInstance()
    var email: String? = null
    var username: String? = null
    var passwordHash: String? = null
    var provider: ProviderType? = null
}
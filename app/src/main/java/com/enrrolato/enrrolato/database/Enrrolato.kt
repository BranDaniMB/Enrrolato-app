package com.enrrolato.enrrolato.database

import com.enrrolato.enrrolato.iceCream.Container
import com.enrrolato.enrrolato.iceCream.Filling
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
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
    var flavors: Array<Flavor>? = null
    var fillings: Array<Filling>? = null
    var toppings: Array<Topping>? = null
    var containers: Array<Container>? = null

    init {
        // Acceder a la base de datos e inicializar las variables necesarias
    }

    fun isValidSession(): Boolean {
        return !email.isNullOrEmpty() && provider != null
    }

    fun update() {

    }
}
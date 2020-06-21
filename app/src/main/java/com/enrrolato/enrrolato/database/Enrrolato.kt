package com.enrrolato.enrrolato.database

import android.app.Activity
import android.app.Application
import android.content.Context
import com.enrrolato.enrrolato.MainActivity
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.iceCream.*
import com.google.firebase.database.*

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class Enrrolato: Application() {
    private val database: FirebaseDatabase
            get() = FirebaseDatabase.getInstance()
    private var user: User? = null
    var listFlavors: ArrayList<Flavor> = ArrayList()
        get() {
            loadFlavors()
            return field;
        }
    var listFillings: ArrayList<Filling> = ArrayList()
        get() {
            loadFillings()
            return field
        }
    var listToppings: ArrayList<Topping> = ArrayList()
        get() {
            loadToppings()
            return field
        }
    var listContainers: ArrayList<Container> = ArrayList()
        get() {
            loadContainers()
            return field
        }
    val String.toBoolean
        get() = this == "1"

    override fun onCreate() {
        super.onCreate()
        instance = this
        update()
    }

    companion object {
        lateinit var instance: Enrrolato
            private set
    }

    private fun loadFlavors() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFlavor = database.getReference(applicationContext.getString(R.string.db_flavors))

        val flavorListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> = dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                temp.forEach {
                    listFlavors.add(
                        Flavor(it.value["name"]!!,
                        it.value["isLiqueur"]!!.toBoolean,
                        it.value["isSpecial"]!!.toBoolean,
                        it.value["isExclusive"]!!.toBoolean,
                        it.value["avaliable"]!!.toBoolean)
                    )
                }
                println(listFlavors.size)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        refFlavor.addValueEventListener(flavorListener)
    }

    private fun loadFillings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFilling = database.getReference(applicationContext.getString(R.string.db_fillings))

        val fillingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> = dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                temp.forEach {
                    listFillings.add(Filling(it.value["name"]!!,
                        it.value["avaliable"]!!.toBoolean,
                        it.value["isExclusive"]!!.toBoolean)
                    )
                }
                println(listFlavors.size)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        refFilling.addValueEventListener(fillingListener)
    }

    private fun loadToppings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refTopping = database.getReference(applicationContext.getString(R.string.db_toppings))

        val toppingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> = dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                temp.forEach {
                    listToppings.add(
                        Topping(it.value["name"]!!,
                        it.value["avaliable"]!!.toBoolean)
                    )
                }
                println(listToppings.size)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        refTopping.addValueEventListener(toppingListener)
    }

    private fun loadContainers() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refContainer = database.getReference(applicationContext.getString(R.string.db_containers))

        val containerListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> = dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                temp.forEach {
                    listContainers.add(
                        Container(it.value["name"]!!,
                        it.value["avaliable"]!!.toBoolean)
                    )
                }
                println(listContainers.size)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        refContainer.addValueEventListener(containerListener)
    }

    public fun initUser(email: String?, provider: ProviderType?) {
        val ref = database.getReference(applicationContext.getString(R.string.db_app_users) +"/"+ email.hashCode().toString())

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = if (dataSnapshot.exists()) {
                    val username: String? = dataSnapshot.child("username").value as String?
                    User(email, username, provider)
                } else {
                    User(email, null, provider)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        ref.addValueEventListener(userListener)
    }

    fun isValidSession(): Boolean {
        return user != null
    }

    fun update() {
        loadFlavors()
        loadFillings()
        loadToppings()
        loadContainers()
    }
}

@IgnoreExtraProperties
data class User(
    var email: String?,
    var username: String?,
    var provider: ProviderType?
)
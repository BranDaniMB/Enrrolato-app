package com.enrrolato.enrrolato.database

import android.app.Application
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.iceCream.*
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
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
    var listFillings: ArrayList<Filling> = ArrayList()
    var listToppings: ArrayList<Topping> = ArrayList()
    var listContainers: ArrayList<Container> = ArrayList()
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
                listFlavors.clear();
                for ((key, value) in temp) {
                    listFlavors.add(
                        Flavor(value["name"]!!,
                            value["isLiqueur"]!!.toBoolean,
                            value["isSpecial"]!!.toBoolean,
                            value["isExclusive"]!!.toBoolean,
                            value["avaliable"]!!.toBoolean)
                    )
                }
                println("Flavor: " + listFlavors.size)
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
                listFillings.clear()
                for ((key, value) in temp) {
                    listFillings.add(Filling(value["name"]!!,
                        value["avaliable"]!!.toBoolean,
                        value["isExclusive"]!!.toBoolean)
                    )
                }
                println("Filling: " + listFillings.size)
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
                listToppings.clear()
                for ((key, value) in temp) {
                    listToppings.add(
                        Topping(value["name"]!!,
                            value["avaliable"]!!.toBoolean)
                    )
                }
                println("Topping: " + listToppings.size)
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
                listContainers.clear()
                for ((key, value) in temp) {
                    listContainers.add(
                        Container(value["name"]!!,
                            value["avaliable"]!!.toBoolean)
                    )
                }
                println("Container: " + listContainers.size)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        refContainer.addValueEventListener(containerListener)
    }

    public fun initUser(id: String, email: String?, provider: ProviderType?) {
        user = User(email, "", provider)
        val ref = database.getReference(applicationContext.getString(R.string.db_app_users))
        ref.child(id).setValue(user)
        /*ref.child("email").setValue(email)
        ref.child("username").setValue("")
        ref.child("provider").setValue(provider)*/

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = if (dataSnapshot.exists()) {
                    val username: String? = dataSnapshot.child("username").value as String?
                    User(email, username, provider)
                } else {
                    User(email,null, provider)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        ref.addValueEventListener(userListener)
    }

    public fun getId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    public fun getUsername(email: String?) : String {
        // MEJORARLO Y VER COMO SE HACE BIEN

        return ""

        /* val auth: FirebaseAuth = FirebaseAuth.getInstance()
         val mail = database.getReference(applicationContext.getString(R.string.db_app_users) +"/"+ email.hashCode().toString())
         val exist = database.reference.child("" + mail).child("username").toString()

         // BUSCANDO LAS KEY QUE SON IGUALES A ESTO, Y CUANDO LA ENCUENTRO, LA AGARRO, SINO EXISTE, DEJARLA VACÍA

         if(mail.equals(auth.currentUser?.equals(email))) {
             if(!exist.isEmpty()) {
                 return exist
             }
         }
         return ""*/
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
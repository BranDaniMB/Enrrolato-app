package com.enrrolato.enrrolato.database

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.createIcecream.process.IcecreamManager
import com.enrrolato.enrrolato.iceCream.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class Enrrolato: Application() {

    private var manager: IcecreamManager = IcecreamManager()
    private var list: ArrayList<Icecream> = ArrayList()

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

     fun initUser(id: String, username: String?) {
        user = User(username)
        val ref = database.getReference(applicationContext.getString(R.string.db_app_users))
        ref.child(id).setValue(user)

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = if (dataSnapshot.exists()) {
                    val u: String? = dataSnapshot.child("username").value as String?
                    User(u)
                } else {
                    User(null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        ref.addValueEventListener(userListener)
    }

    public fun getId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    public fun getUsername(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(applicationContext.getString(R.string.db_app_users) + "/" + getId())
    }

    public fun setUsername(u: String) {
        var id: String? = getId()
        val ref = FirebaseDatabase.getInstance().getReference(applicationContext.getString(R.string.db_app_users)  + "/" + id)

        if (id != null) {
            //ref.addValueEventListener(object: ValueEventListener {
               // override fun onDataChange(d: DataSnapshot) {
                    //ref.removeValue()

                   ref.setValue(User(u))
                //}

                //override fun onCancelled(d: DatabaseError) {
                //}
            //})
        }
    }

    fun update() {
        loadFlavors()
        loadFillings()
        loadToppings()
        loadContainers()
    }

    public fun createIceCream(): IcecreamManager {
        return manager
    }

    //PROCESO IR SUBIENDO POCO A POCO
    // COGER EL ID
    // CREAR UN ID NUEVO PARA LAS ORDERS
    // A PARTIR DE AHI, IR AGREGANDO LOS SABORES / TOPPINGS / JARABES
    // AL FINAL SE COGE EL NOMBRE Y LA FECHA EN LA QUE SE ENVIA
    fun createOrders() {
        val ref = FirebaseDatabase.getInstance().getReference("app/orders")
        var date = createIceCream().getDate()


        // fecha --> helado y username

        ref.child(date).child("username").setValue(getUsername())
        ref.child(date).child("icecream").setValue(create())

        createIceCream().cleanData()
    }

    // UNA LISTA DE MANAGER PARA METER SI SE PIDEN + DE UN HELADO, Y CUANDO YA SE HAYAN PEDIDO TODOS, ENVIAR LA ORDEN. PERO
    // MANTENER TODOS EN ALGUN LADO ALMACENANDOSE

    fun create(): Icecream {
        var id = getId()
        return Icecream(id, createIceCream().gFlavor(), createIceCream().getFilling(),
            createIceCream().gTopping(),  createIceCream().getContainer(), createIceCream().getPrice(), false)
    }

    fun addList() {
        list.add(create())
    }

    fun getList(): ArrayList<Icecream> {
        return list
    }


}

@IgnoreExtraProperties
data class Icecream(
    var id: String?,
    var flavor: String,
    var filling:String,
    var topping: String,
    var container: String,
    var price: Int,
    var delivered: Boolean
)

@IgnoreExtraProperties
data class User(
    var username: String?
)
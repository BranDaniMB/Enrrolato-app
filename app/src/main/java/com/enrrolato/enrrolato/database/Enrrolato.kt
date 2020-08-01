package com.enrrolato.enrrolato.database

import android.app.Application
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.createIcecream.process.IcecreamManager
import com.enrrolato.enrrolato.objects.*
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
    var listSeasonIcecream: ArrayList<SeasonIcecream> = ArrayList()
    var listPrices: ArrayList<Price> = ArrayList()
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
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listFlavors.clear();
                for ((key, value) in temp) {
                    listFlavors.add(
                        Flavor(
                            value["name"]!!,
                            value["isLiqueur"]!!.toBoolean,
                            value["isSpecial"]!!.toBoolean,
                            value["isExclusive"]!!.toBoolean,
                            value["avaliable"]!!.toBoolean
                        )
                    )
                }
                println("Flavor: " + listFlavors.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFlavor.addValueEventListener(flavorListener)
    }

    private fun loadFillings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFilling = database.getReference(applicationContext.getString(R.string.db_fillings))

        val fillingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listFillings.clear()
                for ((key, value) in temp) {
                    listFillings.add(
                        Filling(
                            value["name"]!!,
                            value["avaliable"]!!.toBoolean,
                            value["isExclusive"]!!.toBoolean
                        )
                    )
                }
                println("Filling: " + listFillings.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFilling.addValueEventListener(fillingListener)
    }

    private fun loadToppings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refTopping = database.getReference(applicationContext.getString(R.string.db_toppings))

        val toppingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listToppings.clear()
                for ((key, value) in temp) {
                    listToppings.add(
                        Topping(
                            value["name"]!!,
                            value["avaliable"]!!.toBoolean
                        )
                    )
                }
                println("Topping: " + listToppings.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refTopping.addValueEventListener(toppingListener)
    }

    private fun loadContainers() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refContainer =
            database.getReference(applicationContext.getString(R.string.db_containers))

        val containerListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listContainers.clear()
                for ((key, value) in temp) {
                    listContainers.add(
                        Container(
                            value["name"]!!,
                            value["avaliable"]!!.toBoolean
                        )
                    )
                }
                println("Container: " + listContainers.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refContainer.addValueEventListener(containerListener)
    }

    private fun loadSeason() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refSeason = database.getReference("business/ice_creams")

        val seasonListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listSeasonIcecream.clear()
                for ((key, value) in temp) {
                    listSeasonIcecream.add(
                        SeasonIcecream(
                            value["name"]!!,
                            value["flavor"]!!,
                            value["filling"]!!,
                            value["topping"]!!,
                            value["container"]!!,
                            value["isSpecial"]!!.toBoolean,
                            value["isLiqueur"]!!.toBoolean,
                            value["avaliable"]!!.toBoolean))
                }
                println("Season: " + listSeasonIcecream.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refSeason.addValueEventListener(seasonListener)
    }

    fun initUser(id: String?, username: String?) {
        user = User(username)
        val ref = database.getReference(applicationContext.getString(R.string.db_app_users))
        if (id != null) {
            ref.child(id).setValue(user)
        }

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

    fun getId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getUsername(): DatabaseReference {
        return FirebaseDatabase.getInstance()
            .getReference(applicationContext.getString(R.string.db_app_users) + "/" + getId())
    }

    fun setUsername(u: String) {
        var id: String? = getId()
        val ref = FirebaseDatabase.getInstance()
            .getReference(applicationContext.getString(R.string.db_app_users) + "/" + id)

        if (id != null) {
            ref.setValue(User(u))
        }
    }

    fun update() {
        loadFlavors()
        loadFillings()
        loadToppings()
        loadContainers()
        loadSeason()
    }

    fun createIceCream(): IcecreamManager {
        return manager
    }

    fun sendAllOrders(username: String?) {
        var count = 1
        val ref = FirebaseDatabase.getInstance().getReference("app/orders")
        var date = createIceCream().getDate()

        ref.child(date).child("username").setValue(username)

        for (l in getList()) {
            if (!l.sent) {
                setSent(count)
                ref.child(date).child("icecream_$count").setValue(l)
            }
            count++
        }
        createIceCream().cleanData()
    }

    fun create(): Icecream {
        var id = getId()
        return Icecream(
            id,
            createIceCream().gFlavor(),
            createIceCream().getFilling(),
            createIceCream().gTopping(),
            createIceCream().getContainer(),
            createIceCream().getPrice(),
            false,
            false,
            false
        )
    }

    fun createSeasonIceCream(): Icecream {
        var id = getId()
        return Icecream(
            id,
            createIceCream().getSeasonIcecream().flavor,
            createIceCream().getSeasonIcecream().filling,
            createIceCream().getSeasonIcecream().topping,
            createIceCream().getSeasonIcecream().container,
            2900,  //createIceCream().getPrice(), // ESTO HAY QUE CAMBIARLO PERO CON EL AGREGADO DE PRECIOS QUE ESTÁ PRESENTE
            false,
            false,
            false
        )
    }

    fun addListSeason() {
        list.add(createSeasonIceCream())
    }

    fun addList() {
        list.add(create())
    }

    fun getList(): ArrayList<Icecream> {
        return list
    }

    fun setFavorite(i: Int) {
        list[i].favorite = list[i].favorite != true
    }

    /*
    fun isEmpty(): Boolean {
        return manager.isEmpty()
    }
     */

    private fun setSent(i: Int) {
        list[i].sent = true
    }

    fun deleteFromList(p: Int) {
        if(list.isNotEmpty()) {
            list.removeAt(p)
        }
    }

}


package com.enrrolato.enrrolato.database

import android.app.Application
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.createIcecream.process.IcecreamManager
import com.enrrolato.enrrolato.createIcecream.process.Manager
import com.enrrolato.enrrolato.objects.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Enrrolato: Application() {

    //private var managerIcecream: IcecreamManager = IcecreamManager()
    private var list: ArrayList<Icecream> = ArrayList()
    private lateinit var manager : Manager

    private val database: FirebaseDatabase
        get() = FirebaseDatabase.getInstance()
    private var user: User? = null
    var listFlavors: ArrayList<Flavor> = ArrayList()
    var listFillings: ArrayList<Filling> = ArrayList()
    var listToppings: ArrayList<Topping> = ArrayList()
    var listContainers: ArrayList<Container> = ArrayList()
    var listSeasonIcecream: ArrayList<SeasonIcecream> = ArrayList()
    var listPrices: ArrayList<Price> = ArrayList()
    var listFavorites: ArrayList<Icecream> = ArrayList()
    private var df: String? = ""
    val String.toBoolean
        get() = this == "1"

    override fun onCreate() {
        super.onCreate()
        instance = this
        update()
        initialize()
    }

    companion object {
        lateinit var instance: Enrrolato
            private set
    }

    private fun initialize() {
        manager = Manager(instance)
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
                            value["isExclusive"]!!.toBoolean,
                            value["isLiqueur"]!!.toBoolean
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
                            value["isExclusive"]!!.toBoolean,
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
                            value["avaliable"]!!.toBoolean,
                            value["isExclusive"]!!.toBoolean,
                            value["price"]!!.toInt()
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
                            value["avaliable"]!!.toBoolean
                        )
                    )
                }
                println("Season: " + listSeasonIcecream.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refSeason.addValueEventListener(seasonListener)
    }

    private fun loadPrices() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refPrices = database.getReference("business/prices")

        val pricesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, String> =
                    dataSnapshot.getValue(false) as HashMap<String, String>
                listPrices.clear()
                for (value in temp) {
                    listPrices.add(
                        Price(value.key, value.value)
                    )
                }
                println("Prices: " + listPrices.size)

                /*
                for (l in listPrices) {
                    println("OJO AQUI = " + l.type + " - " + l.price + "\n")
                }
                 */
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refPrices.addValueEventListener(pricesListener)
    }

    private fun loadFavorites(id: String?) {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFavorites = database.getReference("users/" + id + "/favorites_ice_cream")

        val favoritesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                listFavorites.clear()
                for ((key, value) in temp) {
                    listFavorites.add(
                        Icecream(
                            value["id"],
                            value["flavor"]!!,
                            value["filling"]!!,
                            value["topping"]!!,
                            value["container"]!!,
                            value["price"]!!.toInt(),
                            value["delivered"]!!.toBoolean,
                            value["favorite"]!!.toBoolean,
                            value["sent"]!!.toBoolean
                        )
                    )
                }
                println("Favorites: " + listFavorites.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFavorites.addValueEventListener(favoritesListener)
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
        loadPrices()
    }

    fun createIcecream(): Manager {
        return manager
    }

    fun sendAllOrders(username: String?) {
        var count = 0
        val ref = FirebaseDatabase.getInstance().getReference("app/orders")
        var date = manager.getIcecreamManager().getDate()

        ref.child(date).child("username").setValue(username)

        for (l in getList()) {
            if (!l.sent) {
                setSent(count)
                count++
                ref.child(date).child("icecream_$count").setValue(l)
            }
        }
        manager.getIcecreamManager().cleanData()
    }

    fun addFavoriteIcecream(i: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("app/users")
        var id = getId()
        var icecream = getList()[i]

        if (id != null) {
            var d = ref.child(id).child("favorites_icecream").push()
            favorite(d.key)
            d.setValue(icecream)
        }
    }

    fun removeFavoriteIcecream(idFavorite: String?) {
        var id = getId()
        val ref = FirebaseDatabase.getInstance().getReference("app/users")

        if (id != null && idFavorite != null) {
            ref.child(id).child("favorites_icecream").child(idFavorite).removeValue()
        }
    }

    private fun favorite(df: String?) {
        this.df = df
    }

    fun catchFavorite(): String? {
        return df
    }

    fun showFavorites(id: String?) {
        if(listFavorites.isEmpty()) {
            loadFavorites(id)
        }
    }

    fun chargeFavorites() {
        for(l in listFavorites) {
            list.add(l)
        }
    }

    fun addListSeason() {
        var id = getId()
        list.add(manager.createSeasonIceCream(id))
        manager.getIcecreamManager().cleanData()
    }

    fun addList() {
        var id = getId()
        list.add(manager.create(id))
        manager.getIcecreamManager().cleanData()
    }

    fun getList(): ArrayList<Icecream> {
        return list
    }

    fun setFavorite(i: Int) {
        list[i].favorite = list[i].favorite != true
    }

    fun getFavorite(i: Int): Boolean {
        return list[i].favorite
    }

    private fun setSent(i: Int) {
        list[i].sent = true
    }

    fun deleteFromList(p: Int) {
        if(list.isNotEmpty()) {
            list.removeAt(p)
        }
    }

}


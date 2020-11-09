package com.enrrolato.enrrolato.manager

import android.app.Application
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.objects.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.utilities.Tree
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Enrrolato: Application() {
    private var shoppingCart: ArrayList<IceCream> = ArrayList()
    public lateinit var manager : MakeIceCream
    private val database: FirebaseDatabase
        get() = FirebaseDatabase.getInstance()
    private var user: User? = null
    lateinit var listFlavors: SortedMap<String,Flavor>
    lateinit var listFillings: SortedMap<String,Filling>
    lateinit var listToppings: SortedMap<String,Topping>
    lateinit var listContainers: SortedMap<String,Container>
    lateinit var listPredefinedIceCream: SortedMap<String,PredefinedIceCream>
    var listSchedules: HashMap<String, Schedule> = HashMap()
    lateinit var prices: Prices
    var listFavorites: ArrayList<IceCream> = ArrayList()
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
        manager = MakeIceCream(this);
    }

    private fun loadFlavors() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFlavor = database.getReference(applicationContext.getString(R.string.db_flavors));

        val flavorListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                val items: MutableMap<String, Flavor> = mutableMapOf()
                for ((key, value) in temp) {
                    items[key] = Flavor(
                        value["name"]!!,
                        value["isLiqueur"]!!.toBoolean,
                        value["isSpecial"]!!.toBoolean,
                        value["isExclusive"]!!.toBoolean,
                        value["avaliable"]!!.toBoolean
                    )
                }
                listFlavors = items.toSortedMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFlavor.addValueEventListener(flavorListener)
    }

    private fun loadFillings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refFilling = database.getReference(applicationContext.getString(R.string.db_fillings));

        val fillingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                val items: MutableMap<String, Filling> = mutableMapOf()
                for ((key, value) in temp) {
                    items[key] = Filling(
                        value["name"]!!,
                        value["avaliable"]!!.toBoolean,
                        value["isExclusive"]!!.toBoolean,
                        value["isLiqueur"]!!.toBoolean
                    )
                }
                listFillings = items.toSortedMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFilling.addValueEventListener(fillingListener)
    }

    private fun loadToppings() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refTopping = database.getReference(applicationContext.getString(R.string.db_toppings)).orderByKey();

        val toppingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                val items: MutableMap<String, Topping> = mutableMapOf()
                for ((key, value) in temp) {
                    items[key] = Topping(
                        value["name"]!!,
                        value["isExclusive"]!!.toBoolean,
                        value["avaliable"]!!.toBoolean
                    )
                }
                listToppings = items.toSortedMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refTopping.addValueEventListener(toppingListener)
    }

    private fun loadContainers() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refContainer =
            database.getReference(applicationContext.getString(R.string.db_containers)).orderByKey();

        val containerListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                val items: MutableMap<String, Container> = mutableMapOf()
                for ((key, value) in temp) {
                    items[key] = Container(
                        value["name"]!!,
                        value["avaliable"]!!.toBoolean,
                        value["isExclusive"]!!.toBoolean,
                        value["price"]!!.toInt()
                    )
                }
                listContainers = items.toSortedMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refContainer.addValueEventListener(containerListener)
    }

    private fun loadPredefinedIceCreams() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val refSeason = database.getReference("business/ice_creams").orderByKey();

        val seasonListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, String>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, String>>
                val items: MutableMap<String, PredefinedIceCream> = mutableMapOf()
                for ((key, value) in temp) {
                    items[key] = PredefinedIceCream(
                        value["name"]!!,
                        value["flavor"]!!,
                        value["filling"]!!,
                        value["topping"]!!,
                        value["container"]!!,
                        value["isSeason"]!!.toBoolean,
                        value["isSpecial"]!!.toBoolean,
                        value["isLiqueur"]!!.toBoolean,
                        value["avaliable"]!!.toBoolean
                    )
                }
                listPredefinedIceCream = items.toSortedMap()
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
                val temp: HashMap<String, String> = dataSnapshot.getValue(false) as HashMap<String, String>;
                prices = Prices(
                    temp["flavor_amount"]!!.toInt(),
                    temp["filling_amount"]!!.toInt(),
                    temp["topping_amount"]!!.toInt(),
                    temp["regular_price"]!!.toInt(),
                    temp["season_ice_cream"]!!.toInt(),
                    temp["special_flavor"]!!.toInt(),
                    temp["liqueur_flavor"]!!.toInt(),
                    temp["extra_topping_price"]!!.toInt()
                )
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
                        IceCream(
                            value["id"],
                            value["flavor"]!!,
                            value["filling"]!!,
                            value["topping"]!!,
                            value["container"]!!,
                            value["price"]!!.toInt(),
                            value["favorite"]!!.toBoolean,
                            value["sent"]!!.toBoolean,
                            value["finished"]!!.toBoolean,
                            value["plus18"]!!.toBoolean
                        )
                    )
                }
                println("Favorites: " + listFavorites.size)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        refFavorites.addValueEventListener(favoritesListener)
    }

    private fun loadSchedules() {
        // Acceder a la base de datos e inicializar las variables necesarias
        val ref = database.getReference("business/schedules")

        val schedulesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp: HashMap<String, HashMap<String, Any>> =
                    dataSnapshot.getValue(false) as HashMap<String, HashMap<String, Any>>
                listSchedules.clear()
                for ((key, value) in temp) {
                    listSchedules[key] = Schedule(
                        value["days"] as HashMap<String, Boolean>,
                        value["startDate"] as String,
                        value["endDate"] as String,
                        value["startTime"] as String,
                        value["endTime"] as String,
                        (value["isActive"] as String).toBoolean,
                        value["repeat"] as String,
                        (value["typeOfAvailability"] as String).toBoolean
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addValueEventListener(schedulesListener)
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

    fun verifyAvailability(): Boolean {
        return true;
    }

    fun getUsername(): DatabaseReference {
        return FirebaseDatabase.getInstance()
            .getReference(applicationContext.getString(R.string.db_app_users) + "/" + getId())
    }

    fun setUsername(u: String) {
        val id: String? = getId()
        val ref = FirebaseDatabase.getInstance()
            .getReference(applicationContext.getString(R.string.db_app_users) + "/" + id)

        if (id != null) {
            ref.setValue(User(u))
        }
    }

    private fun update() {
        loadFlavors()
        loadFillings()
        loadToppings()
        loadContainers()
        loadPredefinedIceCreams()
        loadPrices()
        loadSchedules()
    }

    private fun generateOrderID(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        sdf.timeZone = TimeZone.getTimeZone("GMT-6")
        return sdf.format(Date().time);
    }

    private fun getDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT-6")
        return sdf.format(Date().time);
    }

    fun sendAllOrders(username: String?) {
        var count = 0
        val ref = FirebaseDatabase.getInstance().getReference("app/orders")
        val date = generateOrderID();
        var orderPrice: Double = 0.0;

        ref.child(date).child("username").setValue(username)
        ref.child(date).child("prepared").setValue(false)
        ref.child(date).child("delivered").setValue(false)
        ref.child(date).child("done").setValue(false)
        ref.child(date).child("date").setValue(getDateTime())

        for (l in getList()) {
            if (!l.sent) {
                setSent(count)
                count++
                ref.child(date).child("icecreams").child("icecream_$count").setValue(l)
                orderPrice += l.price;
            }
        }

        ref.child(date).child("price").setValue(orderPrice);
    }

    fun addFavoriteIceCream(i: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("app/users")
        var id = getId()
        var icecream = getList()[i]

        if (id != null) {
            var d = ref.child(id).child("favorites_icecream").push()
            favorite(d.key)
            d.setValue(icecream)
        }
    }

    fun removeFavoriteIceCream(idFavorite: String?) {
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
            shoppingCart.add(l)
        }
    }

    fun getList(): ArrayList<IceCream> {
        return shoppingCart
    }

    fun setFavorite(i: Int) {
        shoppingCart[i].favorite = shoppingCart[i].favorite != true
    }

    fun getFavorite(i: Int): Boolean {
        return shoppingCart[i].favorite
    }

    private fun setSent(i: Int) {
        shoppingCart[i].sent = true
    }

    fun deleteFromList(p: Int) {
        if(shoppingCart.isNotEmpty()) {
            shoppingCart.removeAt(p)
        }
    }

    fun addIceCreamToShoppingCart(it: IceCream) {
        shoppingCart.add(it);
    }

}


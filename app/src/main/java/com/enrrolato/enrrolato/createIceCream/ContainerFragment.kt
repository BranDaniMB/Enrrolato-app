package com.enrrolato.enrrolato.createIceCream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.CartScreenFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.adapter.AdapterIceCream
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.manager.SelectionTypes
import com.enrrolato.enrrolato.manager.Steps
import com.enrrolato.enrrolato.objects.Container

class ContainerFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var selectedContainer: Container? = null
    private lateinit var back: ImageButton
    private lateinit var addCart: Button
    private lateinit var addNewIceCream: Button
    private lateinit var sp: Spinner
    private lateinit var recyclerViewContainer: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_select_container, container, false)
        back = view.findViewById(R.id.backBtn)
        addCart = view.findViewById(R.id.btAddToShopCart)
        addNewIceCream = view.findViewById(R.id.btAddNewIceCream)
        sp = view.findViewById(R.id.spContainer)
        recyclerViewContainer = view.findViewById(R.id.selectedContainer)

        loadContainers()

        enrrolato.manager.selectedContainer?.let {
            selectedContainer = it;
            addToRecycler(it.name);
        }

        back.setOnClickListener {
            goBack()
        }

        addCart.setOnClickListener {
            addToCart()
        }

        addNewIceCream.setOnClickListener {
            newIC()
        }
        return view
    }

    private fun loadContainers() {
        val leakedList = ArrayList<String>();
        leakedList.add(getString(R.string.container_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyContainer()) {
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (container in enrrolato.manager.getPredefinedContainer()) {
                    if (container.available) {
                        leakedList.add(container.name)
                    }
                }

            }
            SelectionTypes.NOT_PREDEFINED, SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY -> {
                for (container in enrrolato.listContainers.values) {
                    if (container.available && !container.isExclusive) {
                        leakedList.add(container.name)
                    }
                }
            }
        }


        fillContainer(leakedList);
    }

    private fun fillContainer(leakedList: ArrayList<String>) {
        if (leakedList.size <= 1) {
            leakedList.clear();
            leakedList.add(getString(R.string.no_elements));
        }

        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, leakedList)
        sp.adapter = array
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                val it = av?.getItemAtPosition(i).toString();
                if (it != getString(R.string.container_selector) && it != getString(R.string.no_elements)) {
                    chooseContainer(it)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun goBack() {
        val fragment = when (enrrolato.manager.takeStepBack()) {
            Steps.FLAVORS -> FlavorsFragment()
            Steps.FILLINGS -> FillingFragment()
            Steps.TOPPINGS -> ToppingFragment()
            Steps.CONTAINERS -> ContainerFragment()
            else -> DefaultFlavorFragment()
        }
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun addToCart() {
        selectedContainer?.let {
            enrrolato.manager.exportToCart();
            goToCart();
        } ?: run {
            errorPopup(getString(R.string.no_container))
        }
    }

    private fun goToCart() {
        val fragment = CartScreenFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun newIC() {
        selectedContainer?.let {
            enrrolato.manager.exportToCart();
            newIceCream();
        } ?: run {
            errorPopup(getString(R.string.no_container))
        }
    }

    private fun newIceCream() {
        selectedContainer?.let {
            val fragment = DefaultFlavorFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun chooseContainer(container: String) {
        selectedContainer?.let {
            if (it.name == container) {
                errorPopup(getString(R.string.duplicated_container))
                return;
            }
        }
        addToRecycler(container)
        selectedContainer = enrrolato.listContainers[container]
        enrrolato.manager.addContainer(container);
    }

    private fun addToRecycler(container: String) {
        recyclerViewContainer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        listToRecycler.clear();
        listToRecycler.add(container);
        val af: AdapterIceCream = AdapterIceCream(listToRecycler)

        // ELIMINAR UN SABOR
        af.setOnItemClickListener(object : AdapterIceCream.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                if (listToRecycler.size == 1 || listToRecycler.size == 0) {
                    selectedContainer = null;
                    enrrolato.manager.removeContainer()
                    Toast.makeText(
                        context,
                        "Se eliminó " + af.list[0],
                        Toast.LENGTH_SHORT
                    ).show()
                    listToRecycler.removeAt(0)
                    af.notifyItemRemoved(0)
                } else {
                    selectedContainer = null;
                    enrrolato.manager.removeContainer()
                    Toast.makeText(
                        context,
                        "Se eliminó " + af.list[position],
                        Toast.LENGTH_SHORT
                    ).show()
                    listToRecycler.removeAt(position)
                    af.notifyItemRemoved(position)
                    af.itemCount - 1
                }

                recyclerViewContainer.setItemViewCacheSize(listToRecycler.size)

                loadContainers();
            }
        })
        recyclerViewContainer.adapter = af
    }

    private fun errorPopup(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popup = layoutInflater.inflate(R.layout.popup_alert_message, null)
        val message = popup.findViewById<View>(R.id.txtMessage) as TextView
        message.text = msg
        val bt_ok: Button = popup.findViewById(R.id.btOk);
        alertDialogBuilder?.setView(popup)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
        }
    }

}
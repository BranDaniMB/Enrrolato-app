package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.enrrolato.enrrolato.DefaultFlavorFragment
import com.enrrolato.enrrolato.R
import com.google.firebase.database.*

class FlavorsFragment : Fragment() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var flavorList: ArrayList<String>
    private lateinit var flavorSelected: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        val backDefault = view.findViewById<View>(R.id.btBackToDefault) as ImageButton
        val flavor = view.findViewById<View>(R.id.spFlavor) as Spinner
        //val trad = view.findViewById<View>(R.id.rbTradicional) as RadioButton
        //val lic = view.findViewById<View>(R.id.rbLicour) as RadioButton

        chargeFlavors(flavor)

        backDefault.setOnClickListener {
            backToDefault()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun backToDefault() {
        val fragment = DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep() {
        // CODE HERE
    }

    private fun chargeFlavors(f: Spinner): ArrayList<String> {
        // CODE HERE
        database.child("business").child("ingredients").child("flavors").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (data: DataSnapshot in dataSnapshot.children) {
                        var id: String = data.key.toString()
                        var name: String = data.child("name").getValue().toString()
                        var licour: String = data.child("isLiqueur").getValue().toString()
                        var special: String = data.child("isSpecial").getValue().toString()
                        var exclusive: String = data.child("isExclusive").getValue().toString()
                        var avaliable: String = data.child("avaliable").getValue().toString()

                        if(special.equals("0") || (special.equals("1") && licour.equals("1")) && exclusive.equals("0")) {
                            flavorList = ArrayList()
                            flavorList.add(name)

                            // VAN LOS FILTROS

                        }
                    }
                    fillSpinner(f)
                }
            }

            override fun onCancelled(ds: DatabaseError) {
            }

        })
        return flavorList
    }

    private fun fillSpinner(flavor: Spinner) {
        val array: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, flavorList)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                flavorSelected = av?.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun flavorProcess() {
        // CODE HERE
    }

    private fun filtrer(f: Spinner) {
        // CODE HERE
    }

}
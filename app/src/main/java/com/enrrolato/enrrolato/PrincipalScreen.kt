package com.enrrolato.enrrolato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_principal_menu.*

class PrincipalScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_menu)

        loadFragment(PrincipalMenuFragment());

        nav.setOnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.home -> {
                    loadFragment(PrincipalMenuFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                menuItem.itemId == R.id.shop -> {
                    loadFragment(CartScreenFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                menuItem.itemId == R.id.profile -> {
                    loadFragment(ProfileScreenFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }

        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().also {
            fragmentTransaction -> fragmentTransaction.replace(R.id.principal_page, fragment)
            fragmentTransaction.commit()
        }
    }



}

//getWindow().setNavigationBarColor(Color.parseColor("@colors/backgroundColor")
//window.statusBarColor = Color.parseColor("@colors/backgroundColor")

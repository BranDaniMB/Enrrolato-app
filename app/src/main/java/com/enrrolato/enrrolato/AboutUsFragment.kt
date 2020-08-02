package com.enrrolato.enrrolato

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

public class AboutUsFragment : Fragment() {

    private val facebookId: String = "fb://page/2662776063795795"
    private val urlPageFB: String = "https://www.facebook.com/Enrrolato"
    private val urlPageIG: Uri = Uri.parse("http://instagram.com/_u/enrrolato")

    private lateinit var profile: ImageButton
    private lateinit var facebook: ImageButton
    private lateinit var instagram: ImageButton
    private lateinit var whatsapp: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        profile = view.findViewById(R.id.btBackToProfile)
        facebook = view.findViewById(R.id.btFB)
        instagram = view.findViewById(R.id.btIG)
        whatsapp = view.findViewById(R.id.btWhatsapp)

        profile.setOnClickListener {
            showProfileFragment()
        }

        facebook.setOnClickListener {
            fbLink()
        }

        instagram.setOnClickListener {
            igLink()
        }

        whatsapp.setOnClickListener {
            whatsappLink()
        }
        return view
    }

    private fun showProfileFragment() {
        val fragment = ProfileScreenFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_aboutUs, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fbLink() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)))
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPageFB)))
        }
    }

    private fun igLink() {
        val intent = Intent(Intent.ACTION_VIEW, urlPageIG)
        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/enrrolato/?hl=es-la")))
        }
    }

    private fun whatsappLink() {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = "whatsapp://send?phone=" + "+50683026566"
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }


}

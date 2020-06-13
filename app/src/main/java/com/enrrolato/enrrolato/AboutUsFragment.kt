package com.enrrolato.enrrolato

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment : Fragment() {

    private val facebookId: String = "fb://page/2662776063795795"
    private val urlPageFB: String = "https://www.facebook.com/Enrrolato"
    private val urlPageIG: Uri = Uri.parse("http://instagram.com/_u/enrrolato")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        val btProfile = view.findViewById<View>(R.id.btBackToProfile) as ImageButton
        val btFace = view.findViewById<View>(R.id.btFB) as ImageButton
        val btInstagram = view.findViewById<View>(R.id.btIG) as ImageButton
        val whatsapp = view.findViewById<View>(R.id.btWhatsapp) as ImageButton

        btProfile.setOnClickListener {
            //nav.visibility = View.VISIBLE
            showProfileFragment()
        }

        btFace.setOnClickListener {
            fbLink()
        }

        btInstagram.setOnClickListener {
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
            Log.e(TAG, "Aplicación no instalada.")
            //Abre url de pagina.
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPageFB)))
        }
    }

    private fun igLink() {
        val intent = Intent(Intent.ACTION_VIEW, urlPageIG)
        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //No encontró la aplicación, abre la versión web.
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/enrrolato/?hl=es-la")
                )
            )
        }
    }

    private fun whatsappLink() {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = "whatsapp://send?phone=" + "+50683026566"
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }


}

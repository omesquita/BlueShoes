package br.com.osnirmesquita.blueshoes.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.osnirmesquita.blueshoes.R
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iv_phone_cities.setOnClickListener(this)
        tv_phone_cities.setOnClickListener(this)
        iv_phone_other_regions.setOnClickListener(this)
        tv_phone_other_regions.setOnClickListener(this)
        iv_email_orders.setOnClickListener(this)
        tv_email_orders.setOnClickListener(this)
        iv_email_attendance.setOnClickListener(this)
        tv_email_attendance.setOnClickListener(this)
        iv_address.setOnClickListener(this)
        tv_address.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateToolbarTitleInFragment(R.string.contact_frag_title)
    }

    override fun onClick(view: View) {
        when (view.id) {
            iv_phone_cities.id, tv_phone_cities.id ->
                phoneCallIntent("0${tv_phone_cities.text}")
            iv_phone_other_regions.id, tv_phone_other_regions.id ->
                phoneCallIntent(tv_phone_other_regions.text.toString())
            iv_email_orders.id, tv_email_orders.id ->
                mailToIntent(tv_email_orders.text.toString())
            iv_email_attendance.id, tv_email_attendance.id ->
                mailToIntent(tv_email_attendance.text.toString())
            iv_address.id, tv_address.id ->
                mapsTouteIntent(getString(R.string.contact_frag_address_formatted_to_google_maps))
        }
    }

    private fun phoneCallIntent(number: String) {
        val phone = number.replace("(\\s|\\)|\\(|-)", "")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("tel:$phone")
        }

        activity?.startActivity(intent)
    }

    private fun mailToIntent(emailAddress: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        }

        try {
            val intentChooser = Intent.createChooser(intent, getString(R.string.chooser_email_text))
            activity?.startActivity(intentChooser)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity,
                getString(R.string.info_email_app_install),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun mapsTouteIntent(address: String) {
        val location = Uri.encode(address)
        val navigation = "google.navigation:q=$location"
        val navigationUri = Uri.parse(navigation)
        val intent = Intent(Intent.ACTION_VIEW, navigationUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(activity!!.packageManager) != null) {
            activity?.startActivity(intent)
        } else {
            Toast.makeText(
                activity,
                getString(R.string.info_google_maps_install),
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
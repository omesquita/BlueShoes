package br.com.osnirmesquita.blueshoes.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.osnirmesquita.blueshoes.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iv_instagram.setOnClickListener(this)
        tv_instagram.setOnClickListener(this)

        iv_facebook.setOnClickListener(this)
        tv_facebook.setOnClickListener(this)

        iv_twitter.setOnClickListener(this)
        tv_twitter.setOnClickListener(this)

        iv_youtube.setOnClickListener(this)
        tv_youtube.setOnClickListener(this)

        iv_linkedin.setOnClickListener(this)
        tv_linkedin.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).updateToolbarTitleInFragment(R.string.about_frag_title)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_instagram, R.id.iv_instagram -> openNetwork(
                "com.instagra.android",
                "http://instagram.com/_u/cbf_futebol",
                "http://instagram.com/cbf_futebol"
            )
            R.id.tv_facebook, R.id.iv_facebook -> openNetwork(
                "com.facebook.katana",
                "fb://facewebmodal/f?href=https://www.facebook.com/thiengoCalopsita",
                "https://www.facebook.com/thiengoCalopsita"
            )
            R.id.tv_twitter, R.id.iv_twitter -> openNetwork(
                "com.twitter.android",
                "https://twitter.com/thiengoCalops",
                "https://twitter.com/thiengoCalops"
            )
            R.id.tv_youtube, R.id.iv_youtube -> openNetwork(
                "com.google.android.youtube",
                "https://www.youtube.com/user/thiengoCalopsita",
                "https://www.youtube.com/user/thiengoCalopsita"
            )
            else -> openNetwork(
                "com.linkedin.android",
                "https://www.linkedin.com/in/vin%C3%ADcius-thiengo-5179b180/",
                "https://www.linkedin.com/in/vin%C3%ADcius-thiengo-5179b180/"
            )
        }
    }

    private fun openNetwork(appPackage: String, appAddress: String, webAddress: String) {
        val uri = Uri.parse(appPackage)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage(appPackage)

        try {
            activity?.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webAddress)))
        }
    }
}
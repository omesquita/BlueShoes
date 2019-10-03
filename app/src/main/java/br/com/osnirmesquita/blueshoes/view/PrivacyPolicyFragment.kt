package br.com.osnirmesquita.blueshoes.view

import android.graphics.Typeface
import android.os.Bundle
import android.text.Annotation
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import br.com.osnirmesquita.blueshoes.R
import br.com.osnirmesquita.blueshoes.util.CustomTypefaceSpan
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        privacyPolicyLoad()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity)
            .updateToolbarTitleInFragment( R.string.privacy_policy_frag_title )
    }

    private fun privacyPolicyLoad() {
        val text = getText(R.string.privacy_policy_content) as SpannedString
        val spannedText = SpannableString(text)
        val annotations = text.getSpans(0, text.length, Annotation::class.java)
        annotations.forEach { annotation ->
            val textStartPos = text.getSpanStart(annotation)
            val texEndPos = text.getSpanEnd(annotation)
            val spanFlag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE

            if (annotation.key == "title") {
                val typeFace =
                    ResourcesCompat.getFont(activity!!, R.font.pathway_gothic_one_regular)
                spannedText.setSpan(
                    CustomTypefaceSpan(typeFace!!),
                    textStartPos,
                    texEndPos,
                    spanFlag
                )
                spannedText.setSpan(StyleSpan(Typeface.BOLD), textStartPos, texEndPos, spanFlag)

                val textSizeSpan = when (annotation.value) {
                    "main" -> RelativeSizeSpan(1.5F)
                    "sub" -> RelativeSizeSpan(1.3F)
                    else -> RelativeSizeSpan(1.1F)
                }

                spannedText.setSpan(
                    textSizeSpan,
                    textStartPos,
                    texEndPos,
                    spanFlag
                )
            } else if (annotation.key == "link") {
                spannedText.setSpan(
                    URLSpan(annotation.value),
                    textStartPos + 1,
                    texEndPos - 1, spanFlag
                )
                spannedText.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.colorLink
                        )
                    ),
                    textStartPos + 1,
                    texEndPos - 1,
                    spanFlag
                )
            }
        }
        tv_privacy_policy_content.text = spannedText.trimStart()
        tv_privacy_policy_content.movementMethod = LinkMovementMethod.getInstance()
    }
}
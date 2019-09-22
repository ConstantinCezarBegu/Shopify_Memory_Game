package com.example.shopify_memory_game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.data.preference.UserData
import kotlinx.android.synthetic.main.dialog_high_score.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

// Fun dialog to display the user his high score
class HighScoreDialog : DialogFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val userData: UserData by instance()

    companion object {
        @JvmStatic
        fun newInstance() =
            HighScoreDialog()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_high_score, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setUpBasedOnMode()

    }


    private fun setUpBasedOnMode() {
        if (userData.userScore > userData.highScore) userData.highScore = userData.userScore

        currentScore.text = userData.userScore.toString()
        highScore.text = userData.highScore.toString()

        fullscreen_dialog_close.setOnClickListener {
            dismiss()
        }

    }

}
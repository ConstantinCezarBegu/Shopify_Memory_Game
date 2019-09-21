package com.example.shopify_memory_game.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.data.preference.UserData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.content_main_application.*
import kotlinx.android.synthetic.main.dialog_get_mode.view.*
import kotlinx.android.synthetic.main.menu_bottom_sheet.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BottomNavigationDrawerFragment :
    BottomSheetDialogFragment(), KodeinAware {
    override val kodein by closestKodein()
    val userData: UserData by instance()
    var isEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        score_text_view.text = userData.userScore.toString()

        navigation_view.setNavigationItemSelectedListener {
            val parentActivity = (requireActivity() as MainActivity)
            when (val itemId = it.itemId) {

                R.id.grid_size_button, R.id.match_size_button -> {
                    displayMarkAllDialog(
                        itemId,
                        parentActivity,
                        parentActivity.mainAcivityConstraint,
                        parentActivity.mainAcivityConstraint as ViewGroup
                    )
                }
                R.id.shuffle_button -> {
                    parentActivity.restartActivity()
                }
            }
            dismiss()
            true
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isEnabled && isVisible.not()) {
            super.show(manager, tag)
        }
    }

    private fun displayMarkAllDialog(
        itemId: Int,
        parentActivity: MainActivity,
        view: View,
        parent: ViewGroup
    ) {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        val mView: View = LayoutInflater.from(view.context)
            .inflate(R.layout.dialog_get_mode, parent, false)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        val matchSizeList = listOf(2, 4, 5)

        mView.headerText.text =
            if (itemId == R.id.grid_size_button) "Adjust grid size" else "Adjust match size"

        mView.spinnerSelection

        mView.promtCancel.setOnClickListener {
            dialog.dismiss()
        }


        val spinnerArrayAdapter = if (itemId == R.id.grid_size_button)
            ArrayAdapter.createFromResource(
                this.requireActivity(),
                R.array.grid_size,
                android.R.layout.simple_spinner_item
            )
        else ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            matchSizeList
        )


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spinnerSelection.adapter = spinnerArrayAdapter
        mView.spinnerSelection.setSelection(
            if (itemId == R.id.grid_size_button) userData.gridSize else matchSizeList.indexOf(
                userData.matchSize
            )
        )

        mView.promptAccept.setOnClickListener {
            if (itemId == R.id.grid_size_button) userData.gridSize =
                mView.spinnerSelection.selectedItemPosition else userData.matchSize =
                matchSizeList[mView.spinnerSelection.selectedItemPosition]
            dialog.dismiss()
            parentActivity.restartActivity()
        }

        dialog.show()
    }

}

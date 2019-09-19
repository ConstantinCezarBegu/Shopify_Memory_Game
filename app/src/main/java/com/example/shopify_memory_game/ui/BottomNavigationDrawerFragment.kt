package com.example.shopify_memory_game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.shopify_memory_game.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.menu_bottom_sheet.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class BottomNavigationDrawerFragment(private val onNavigationGestures: OnNavigationGestures) :
    BottomSheetDialogFragment(), KodeinAware {
    override val kodein by closestKodein()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation_view.setNavigationItemSelectedListener {
            onNavigationGestures.onNavigationItemSelected(it.itemId)
            dismiss()
            true
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isVisible.not()) {
            super.show(manager, tag)
        }
    }

    interface OnNavigationGestures {
        fun onNavigationItemSelected(itemId: Int)
    }
}

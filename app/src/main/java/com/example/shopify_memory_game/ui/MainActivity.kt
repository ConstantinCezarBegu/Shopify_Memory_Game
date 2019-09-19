package com.example.shopify_memory_game.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.RecyclerViewAdapter
import com.example.shopify_memory_game.internal.ScopedActivity
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.content_main_application.*
import kotlinx.android.synthetic.main.dialog_get_mode.view.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : ScopedActivity(), KodeinAware, RecyclerViewAdapter.OnRecyclerOnClickListener,
    BottomNavigationDrawerFragment.OnNavigationGestures {

    override val kodein by closestKodein()

    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment(this)
    private val viewModelFactory: MainActivityViewModelFactory by instance()
    private lateinit var viewmodel: MainActivityViewModel

    private lateinit var lisAdapter: RecyclerViewAdapter

    override fun onNavigationItemSelected(itemId: Int) {
        displayMarkAllDialog(itemId, this.mainAcivityConstraint, this.mainAcivityConstraint as ViewGroup)
    }

    override fun onRecyclerViewClickListener() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        viewmodel =
            ViewModelProviders.of(this, MainActivityViewModelFactory(this.applicationContext, this))
                .get(MainActivityViewModel::class.java)

        setUpToolBar()
        setUpRecyclerView()
        setupUI()
    }


    private fun setUpToolBar() {
        val toolbar: BottomAppBar = findViewById(R.id.bottomAppBar)

        toolbar.setNavigationOnClickListener {
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }
    }

    private fun setupUI() = launch {
        viewmodel.imageList.await().observe(this@MainActivity, Observer {
            progressBar.visibility = View.INVISIBLE
            lisAdapter.submitList(it)
        })
    }

    private fun setUpRecyclerView() {
        (mainActivityRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        lisAdapter =
            RecyclerViewAdapter(
                this
            ).also(
                mainActivityRecyclerView::setAdapter
            )

        mainActivityRecyclerView.layoutManager =
            GridLayoutManager(this, RecyclerView.VERTICAL)
    }
    private fun displayMarkAllDialog(
        itemId: Int,
        view: View,
        parent: ViewGroup
    ) {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        val mView: View = LayoutInflater.from(view.context)
            .inflate(R.layout.dialog_get_mode, parent, false)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        mView.headerText.text = itemId.toString()
        mView.promptBodyText.text = ""

        mView.promtCancel.setOnClickListener {
            dialog.dismiss()
        }

        mView.promptAccept.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {

        }

        dialog.show()
    }
}

package com.example.shopify_memory_game.ui

import android.os.Bundle
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
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : ScopedActivity(), KodeinAware, RecyclerViewAdapter.OnRecyclerOnClickListener {

    override val kodein by closestKodein()

    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
    private val viewModelFactory: MainActivityViewModelFactory by instance()
    private lateinit var viewmodel: MainActivityViewModel

    private lateinit var lisAdapter: RecyclerViewAdapter

    override fun onRecyclerViewClickListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}

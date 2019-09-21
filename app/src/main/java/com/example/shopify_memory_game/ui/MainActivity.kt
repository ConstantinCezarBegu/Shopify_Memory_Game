package com.example.shopify_memory_game.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.core.view.get
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.adapters.GridLayoutWrapper
import com.example.shopify_memory_game.adapters.RecyclerViewAdapter
import com.example.shopify_memory_game.adapters.RecyclerViewSelectionImageTracker
import com.example.shopify_memory_game.data.network.DataSource
import com.example.shopify_memory_game.internal.ScopedActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_host.*
import kotlinx.android.synthetic.main.content_main_application.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


class MainActivity : ScopedActivity(), KodeinAware, RecyclerViewAdapter.OnRecyclerOnClickListener {

    override val kodein by closestKodein()
    private lateinit var viewmodel: MainActivityViewModel
    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment()


    private lateinit var lisAdapter: RecyclerViewAdapter

    private var mLastClickTime = System.currentTimeMillis()
    private val CLICK_TIME_INTERVAL: Long = 200


    override fun onRecyclerViewClickListener(imageData: RecyclerViewAdapter.ImageData) {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        activityFunctionality(false)
        when (viewmodel.imagesRecyclerViewTracker.modifyList(imageData)) {
            RecyclerViewSelectionImageTracker.CardsSelectionResponse.Add -> {
                lisAdapter.notifyItemChanged(imageData.position)
                activityFunctionality(true)
            }
            RecyclerViewSelectionImageTracker.CardsSelectionResponse.Match -> {
                viewmodel.userScore += 10
                lisAdapter.notifyDataSetChanged()
                activityFunctionality(true)
            }
            RecyclerViewSelectionImageTracker.CardsSelectionResponse.NoMatch -> {
                lisAdapter.notifyItemChanged(imageData.position)
                viewmodel.userScore -= 2
                Handler().postDelayed({
                    viewmodel.imagesRecyclerViewTracker.clearSelected()
                    lisAdapter.notifyDataSetChanged()
                    activityFunctionality(true)
                }, 500)
            }
        }
        if (viewmodel.imagesRecyclerViewTracker.cardsMatched.size == lisAdapter.itemCount) {
            activityFunctionality(true)
            HighScoreDialog.newInstance().show(
                supportFragmentManager,
                null
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        val viewModelFactory: AbstractSavedStateViewModelFactory =
            MainActivityViewModelFactory(
                this,
                this
            )

        viewmodel =
            ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        bottomAppBar.menu[0].setOnMenuItemClickListener {
            restartActivity()
            true
        }

        setUpToolBar()
        setUpRecyclerView()
        setupErrorHandling()
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

    private fun setupErrorHandling() {
        viewmodel.errorLiveData.removeObservers(this)
        viewmodel.errorLiveData.observe(this, Observer {
            if (it != DataSource.HttpErrors.SUCCESS) {
                Snackbar.make(
                    this@MainActivity.activity_host_coordinator_layout,
                    "Connection error",
                    Snackbar.LENGTH_SHORT
                ).run {
                    anchorView = this@MainActivity.findViewById(R.id.bottomAppBar)
                    show()
                }
            }
        })
    }


    private fun setUpRecyclerView() {
        (mainActivityRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        lisAdapter =
            RecyclerViewAdapter(
                this,
                viewmodel.imagesRecyclerViewTracker
            ).also(
                mainActivityRecyclerView::setAdapter
            )

        val gridSize = viewmodel.gridSize
        mainActivityRecyclerView.layoutManager =
            GridLayoutWrapper(
                this,
                when {
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> gridSize + 4
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> 2 * gridSize + 5 + if (gridSize == 0) 0 else 1
                    else -> gridSize + 4
                }, RecyclerView.VERTICAL, false
            )
    }

    fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun activityFunctionality(isEnabled: Boolean) {
        bottomNavDrawerFragment.isEnabled = isEnabled
        if (isEnabled) {
            this.window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            this.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }


}

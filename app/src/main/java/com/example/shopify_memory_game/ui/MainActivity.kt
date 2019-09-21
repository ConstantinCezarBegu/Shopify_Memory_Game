package com.example.shopify_memory_game.ui

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.adapters.RecyclerViewAdapter
import com.example.shopify_memory_game.data.network.request.Image
import com.example.shopify_memory_game.internal.ScopedActivity
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_host.*
import kotlinx.android.synthetic.main.content_main_application.*
import kotlinx.android.synthetic.main.dialog_get_mode.view.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


class MainActivity : ScopedActivity(), KodeinAware, RecyclerViewAdapter.OnRecyclerOnClickListener,
    BottomNavigationDrawerFragment.OnNavigationGestures {

    override val kodein by closestKodein()
    private lateinit var viewmodel: MainActivityViewModel
    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment(this)


    private lateinit var lisAdapter: RecyclerViewAdapter

    override fun onNavigationItemSelected(itemId: Int) {

        when (itemId) {
            R.id.grid_size_button, R.id.match_size_button -> {
                displayMarkAllDialog(
                    itemId,
                    this.mainAcivityConstraint,
                    this.mainAcivityConstraint as ViewGroup
                )
            }
            R.id.shuffle_button -> {
                restartActivity()
            }
        }
    }

    override fun onRecyclerViewClickListener(imageData: RecyclerViewAdapter.ImageData) {
        viewmodel.imagesRecyclerViewTracker.modifyList(imageData)
        lisAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        val viewModelFactory: AbstractSavedStateViewModelFactory =
            MainActivityViewModelFactory(this, {
                activityFunctionality(false)
                Handler().postDelayed({
                    viewmodel.imagesRecyclerViewTracker.clearSelected()
                    activityFunctionality(true)
                    lisAdapter.notifyDataSetChanged()
                }, 500)
            }, this)

        viewmodel =
            ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        bottomAppBar.menu[0].setOnMenuItemClickListener {
            restartActivity()
            true
        }

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
            lisAdapter.submitList(cardSelection(it))
        })
    }

    private fun cardSelection(allImages: List<Image>): List<Image> {
        val selectedList =
            allImages.shuffled().take((20 + viewmodel.gridSize * 20) / viewmodel.matchSize)
        var finalList = listOf<Image>()
        for (i in 1..viewmodel.matchSize) {
            finalList = finalList + selectedList
        }
        return (finalList).shuffled()
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
            GridLayoutManager(
                this,
                when {
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> gridSize + 4
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> 2 * gridSize + 5 + if (gridSize == 0) 0 else 1
                    else -> gridSize + 4
                }, RecyclerView.VERTICAL, false
            )
    }

    private fun restartActivity() {
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

        val matchSizeList = listOf(2, 4, 5)

        mView.headerText.text =
            if (itemId == R.id.grid_size_button) "Adjust grid size" else "Adjust match size"

        mView.spinnerSelection

        mView.promtCancel.setOnClickListener {
            dialog.dismiss()
        }


        val spinnerArrayAdapter = if (itemId == R.id.grid_size_button)
            ArrayAdapter.createFromResource(
                this,
                R.array.grid_size,
                android.R.layout.simple_spinner_item
            )
        else ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            matchSizeList
        )


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView.spinnerSelection.adapter = spinnerArrayAdapter
        mView.spinnerSelection.setSelection(
            if (itemId == R.id.grid_size_button) viewmodel.gridSize else matchSizeList.indexOf(
                viewmodel.matchSize
            )
        )

        mView.promptAccept.setOnClickListener {
            if (itemId == R.id.grid_size_button) viewmodel.gridSize =
                mView.spinnerSelection.selectedItemPosition else viewmodel.matchSize =
                matchSizeList[mView.spinnerSelection.selectedItemPosition]
            dialog.dismiss()
            restartActivity()
        }

        dialog.show()
    }
}

package com.example.shopify_memory_game.ui

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
        displayMarkAllDialog(
            itemId,
            this.mainAcivityConstraint,
            this.mainAcivityConstraint as ViewGroup
        )
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
        val selectedList = allImages.shuffled().take(10)
        return (selectedList + selectedList).shuffled()
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

        mainActivityRecyclerView.layoutManager =
            GridLayoutManager(
                this,
                when {
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> 4
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> 5
                    else -> 4
                }, RecyclerView.VERTICAL, false
            )
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

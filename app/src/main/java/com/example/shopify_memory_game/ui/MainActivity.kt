package com.example.shopify_memory_game.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shopify_memory_game.R
import com.google.android.material.bottomappbar.BottomAppBar

class MainActivity : AppCompatActivity() {

    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)


        val toolbar: BottomAppBar = findViewById(R.id.bottomAppBar)

        toolbar.setNavigationOnClickListener {
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }
    }
}

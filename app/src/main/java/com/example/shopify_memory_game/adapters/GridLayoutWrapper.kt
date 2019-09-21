package com.example.shopify_memory_game.adapters

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class GridLayoutWrapper(context: Context, gridSize: Int, orientation: Int, reverseLayout: Boolean): GridLayoutManager(context, gridSize, orientation, reverseLayout) {
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}
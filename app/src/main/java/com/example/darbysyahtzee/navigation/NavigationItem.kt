package com.example.darbysyahtzee.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.darbysyahtzee.R


sealed interface NavigationIcon
data class ImageVectorIcon(val imageVector: ImageVector) : NavigationIcon
data class ResourceIcon(val resourceId: Int) : NavigationIcon

sealed class NavigationItem(var route: String, val icon: NavigationIcon, var title: String) {
    data object Play : NavigationItem("play", ResourceIcon(R.drawable.people), "Gioca")
    data object Account : NavigationItem("account", ResourceIcon(R.drawable.people), "Io")
}

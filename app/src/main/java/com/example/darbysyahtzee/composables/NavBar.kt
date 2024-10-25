package com.example.darbysyahtzee.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.darbysyahtzee.navigation.ImageVectorIcon
import com.example.darbysyahtzee.navigation.NavigationItem
import com.example.darbysyahtzee.navigation.ResourceIcon
import com.example.darbysyahtzee.ui.theme.*


@Composable
fun NavBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Play,
        NavigationItem.Account,
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Play.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }
    BottomAppBar(
        containerColor = DarkBgColor,
    ) {
        NavigationBar(
            containerColor = DarkBgColor,
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkBgColor,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray,
                        selectedTextColor = CreamBackground,
                        indicatorColor = CreamBackground
                    ),
                    alwaysShowLabel = true,
                    icon = {
                        when (item.icon) {
                            is ImageVectorIcon -> {
                                Icon(
                                    imageVector = item.icon.imageVector,
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(25.dp)
                                )
                            }

                            is ResourceIcon -> {
                                Icon(
                                    painterResource(item.icon.resourceId),
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(25.dp)
                                )
                            }
                        }
                    },
                    label = {
                        Text(
                            item.title,
                            fontSize = 12.sp,
                           // fontFamily = if (selectedItem == index) titleFont() else null
                        )
                    },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        currentRoute = item.route
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    inclusive = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


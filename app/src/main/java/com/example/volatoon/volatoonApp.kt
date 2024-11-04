package com.example.volatoon

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.volatoon.view.DashboardScreen
import com.example.volatoon.view.NotificationsScreen
import com.example.volatoon.view.ProfileScreen
import com.example.volatoon.view.SearchScreen
import com.example.volatoon.view.TrendingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolatoonApp(navController: NavHostController){

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = TopLevelRoute.Dashboard.route){
            composable(route = TopLevelRoute.Dashboard.route){
                DashboardScreen()
            }

            composable(route = TopLevelRoute.Trending.route){
                TrendingScreen()
            }

            composable(route = TopLevelRoute.Search.route){
                SearchScreen()
            }

            composable(route = TopLevelRoute.Notifications.route){
                NotificationsScreen()
            }

            composable(route = TopLevelRoute.Profile.route){
                ProfileScreen()
            }
        }
    }



}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        TopLevelRoute.Dashboard,
        TopLevelRoute.Trending,
        TopLevelRoute.Search,
        TopLevelRoute.Notifications,
        TopLevelRoute.Profile
    )

    NavigationBar  {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = "${item.label} icon")
                       },
                label = { Text(item.label) },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
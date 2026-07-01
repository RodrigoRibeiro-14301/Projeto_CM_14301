package com.example.campo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.campo.ui.screens.splash.SplashScreen
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campo.ui.components.BottomNavTab
import com.example.campo.ui.components.CampoBottomBar
import com.example.campo.ui.screens.details.FieldDetailsScreen
import com.example.campo.ui.screens.explore.ExploreScreen
import com.example.campo.ui.screens.home.HomeScreen
import com.example.campo.ui.screens.login.LoginScreen
import com.example.campo.ui.screens.profile.HelpScreen
import com.example.campo.ui.screens.profile.ProfileScreen
import com.example.campo.ui.screens.profile.ProScreen
import com.example.campo.ui.screens.profile.SettingsScreen
import com.example.campo.ui.screens.profile.TermsScreen
import com.example.campo.ui.screens.reservations.ReservationsScreen
import com.example.campo.ui.theme.CampoTheme
import com.example.campo.ui.theme.DarkBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampoTheme {
                RentaBallApp()
            }
        }
    }
}

@Composable
fun RentaBallApp() {
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute == "home" || currentRoute == "explore" ||
            currentRoute == "reservas" || currentRoute == "profile"

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            if (showBottomBar) {
                val selectedTab = when (currentRoute) {
                    "explore" -> BottomNavTab.Explorar
                    "reservas" -> BottomNavTab.Reservas
                    "profile" -> BottomNavTab.Perfil
                    else -> BottomNavTab.Inicio
                }
                CampoBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        val route = when (tab) {
                            BottomNavTab.Inicio -> "home"
                            BottomNavTab.Explorar -> "explore"
                            BottomNavTab.Reservas -> "reservas"
                            BottomNavTab.Perfil -> "profile"
                        }
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen()
                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    onFieldClick = { fieldId -> navController.navigate("field_details/$fieldId") }
                )
            }
            composable("explore") {
                ExploreScreen(
                    onFieldClick = { fieldId -> navController.navigate("field_details/$fieldId") }
                )
            }
            composable("reservas") {
                ReservationsScreen()
            }
            composable("profile") {
                ProfileScreen(
                    onLoggedOut = {
                        // popUpTo(0) limpa TODO o histórico de navegação, não só o Login.
                        // Sem isto, ao premir "voltar" depois de sair, voltarias a entrar
                        // direto no Home sem precisar de fazer login outra vez.
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    },
                    onTermsClick = { navController.navigate("terms") },
                    onSettingsClick = { navController.navigate("settings") },
                    onHelpClick = { navController.navigate("help") }
                )
            }
            composable("settings") {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() },
                    onProClick = { navController.navigate("pro") }
                )
            }
            composable("pro") {
                ProScreen(onBackClick = { navController.popBackStack() })
            }
            composable("help") {
                HelpScreen(onBackClick = { navController.popBackStack() })
            }
            composable("terms") {
                TermsScreen(onBackClick = { navController.popBackStack() })
            }
            composable("field_details/{fieldId}") { backStackEntry ->
                val fieldId = backStackEntry.arguments?.getString("fieldId") ?: ""
                FieldDetailsScreen(
                    fieldId = fieldId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
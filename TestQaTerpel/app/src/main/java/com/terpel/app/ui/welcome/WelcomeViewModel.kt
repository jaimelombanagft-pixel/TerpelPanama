package com.terpel.app.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class WelcomePage(
    val title: String,
    val description: String
)

class WelcomeViewModel : ViewModel() {
    private val _pages = MutableLiveData(
        listOf(
            WelcomePage(
                title = "Bienvenido a Terpel",
                description = "Tu viaje inicia aquí, regístrate y descubre las estaciones más cercanas, conoce el precio de cada Estación, acumula puntos Friend Terpel para obtener beneficios en cupones, experiencias y más. \n\n¡Descubre la app que lo tiene todo!"
            ),
            WelcomePage(
                title = "Beneficios",
                description = "Acumula puntos y redímelos en cupones y experiencias."
            )
        )
    )
    val pages: LiveData<List<WelcomePage>> = _pages
}


package com.darlingson.ndalamagoals.data.repositories

import android.content.Context
import com.darlingson.ndalamagoals.data.Settings
import com.darlingson.ndalamagoals.data.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class SettingsRepository(
    private val context: Context
) {
    val settingsFlow: Flow<Settings> =
        context.settingsDataStore.data

    suspend fun updateCurrency(currency: String) {
        context.settingsDataStore.updateData {
            it.copy(currency = currency)
        }
    }

    suspend fun updateNumberFormat(format: Int) {
        context.settingsDataStore.updateData {
            it.copy(numberFormat = format)
        }
    }

    suspend fun updateBiometrics(enabled: Boolean) {
        context.settingsDataStore.updateData {
            it.copy(biometricsEnabled = enabled)
        }
    }
}

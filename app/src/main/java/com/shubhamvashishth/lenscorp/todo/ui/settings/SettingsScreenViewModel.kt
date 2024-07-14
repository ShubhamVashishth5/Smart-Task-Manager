package com.shubhamvashishth.lenscorp.todo.ui.settings

// SettingsViewModel.kt
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModelViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _biometricEnabled = MutableStateFlow(sharedPreferences.getBoolean("biometric_enabled", false))
    val biometricEnabled: StateFlow<Boolean> get() = _biometricEnabled

    private val _pushNotificationsEnabled = MutableStateFlow(sharedPreferences.getBoolean("push_notifications_enabled", false))
    val pushNotificationsEnabled: StateFlow<Boolean> get() = _pushNotificationsEnabled

    private val _locationRemindersEnabled = MutableStateFlow(sharedPreferences.getBoolean("location_reminders_enabled", false))
    val locationRemindersEnabled: StateFlow<Boolean> get() = _locationRemindersEnabled

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _biometricEnabled.value = enabled
            sharedPreferences.edit().putBoolean("biometric_enabled", enabled).apply()
        }
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _pushNotificationsEnabled.value = enabled
            sharedPreferences.edit().putBoolean("push_notifications_enabled", enabled).apply()
        }
    }

    fun setLocationRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _locationRemindersEnabled.value = enabled
            sharedPreferences.edit().putBoolean("location_reminders_enabled", enabled).apply()
        }
    }
}

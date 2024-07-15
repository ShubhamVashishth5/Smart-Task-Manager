package com.shubhamvashishth.lenscorp.todo.ui.settings

// SettingsViewModel.kt
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamvashishth.lenscorp.todo.helper.PREF_KEY_BIOMETRIC
import com.shubhamvashishth.lenscorp.todo.helper.PREF_KEY_LOCATION
import com.shubhamvashishth.lenscorp.todo.helper.PREF_KEY_NOTIFICATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModelViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _biometricEnabled = MutableStateFlow(sharedPreferences.getBoolean(PREF_KEY_BIOMETRIC, false))
    val biometricEnabled: StateFlow<Boolean> get() = _biometricEnabled

    private val _pushNotificationsEnabled = MutableStateFlow(sharedPreferences.getBoolean(
        PREF_KEY_NOTIFICATION, false))
    val pushNotificationsEnabled: StateFlow<Boolean> get() = _pushNotificationsEnabled

    private val _locationRemindersEnabled = MutableStateFlow(sharedPreferences.getBoolean(
        PREF_KEY_LOCATION, false))
    val locationRemindersEnabled: StateFlow<Boolean> get() = _locationRemindersEnabled

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _biometricEnabled.value = enabled
            sharedPreferences.edit().putBoolean(PREF_KEY_BIOMETRIC, enabled).apply()
        }
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _pushNotificationsEnabled.value = enabled
            sharedPreferences.edit().putBoolean(PREF_KEY_NOTIFICATION, enabled).apply()
        }
    }

    fun setLocationRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _locationRemindersEnabled.value = enabled
            sharedPreferences.edit().putBoolean(PREF_KEY_LOCATION, enabled).apply()
        }
    }
}

package com.example.reminderapp.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileViewModel {
}
@HiltViewModel
class ProfileViewModelImpl @Inject constructor(

): ViewModel(), ProfileViewModel {

}
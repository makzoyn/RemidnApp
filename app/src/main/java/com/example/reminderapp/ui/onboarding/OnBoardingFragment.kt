package com.example.reminderapp.ui.onboarding

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.databinding.FragmentOnBoardingBinding
import com.example.reminderapp.ui.reminds.adapter.viewholders.ViewPagerOnBoardingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment: BaseFragment(R.layout.fragment_on_boarding) {
    private val viewModel: OnBoardingViewModel by viewModels<OnBoardingViewModelImpl>()
    private val binding by viewBinding(FragmentOnBoardingBinding::bind)

    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            callbackNotificationsPermission(isGranted)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        observeViewModel()
    }

    private var shouldShowRationale = false
    private val permissionRequestRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

    private fun prepareUi()  {
        val adapter = ViewPagerOnBoardingAdapter(this)
        binding.viewPager.adapter = adapter
    }

    private fun observeViewModel() = with(viewModel) {
        buttonState.listenValue(::bindButton)
        navigationFlow.listenValue(::onNavigate)
    }

    private fun bindButton(statePair: Pair<Boolean, String>) = with(binding.button) {
        setOnClickListener {
            if (statePair.first) {
                if (permissionRequestRequired) {
                    checkPermission()
                } else {
                    viewModel.finishOnBoarding()
                }
            } else {
                viewModel.nextScreen()
            }
        }
        text = statePair.second
    }

    private fun onNavigate(navId: Int?) {
        navId?.let {
            navigate(navId)
        }
    }

    private fun callbackNotificationsPermission(isGranted: Boolean) {
        if (isGranted) {
            viewModel.finishOnBoarding()
        } else {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() {
        shouldShowRationale = shouldShowRequestPermissionRationale(notificationPermission)
        notificationPermissionLauncher.launch(notificationPermission)
    }
}

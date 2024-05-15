package com.example.reminderapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.ToolbarController
import com.example.reminderapp.common.ToolbarControllerProvider
import com.example.reminderapp.common.base.BaseActivity
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.databinding.ActivityAppBinding
import com.example.reminderapp.databinding.FragmentMainRemindsBinding
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity: BaseActivity(R.layout.activity_app), ToolbarControllerProvider {
    private var navController : NavController? = null

    val viewModel: AppViewModel by viewModels<AppViewModelImpl>()
    private val binding by viewBinding(ActivityAppBinding::bind)

    @Inject
    lateinit var preferencesDataStoreRepository: PreferencesDataStoreRepository

    @Inject
    lateinit var receiveMessageFromPushResult: ReceiveMessageFromPushResult

    private var toolbarController: ToolbarController? = null
    override fun provideToolbarController(): ToolbarController? = toolbarController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        lifecycleScope.launch {
            subscribeOnDeleteMessageResult()
        }
        prepareToolbarController()
        observeViewModel()
    }

    private fun observeViewModel() = with(viewModel) {
        navigationFlow.listenValue(::onNavigation)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun onNavigation(navId: Int?) {
        navId?.let {
            navController?.navigate(navId)
        }
    }

    private fun prepareToolbarController() {
        toolbarController = ToolbarController(binding.toolbar)
    }

    private suspend fun subscribeOnDeleteMessageResult() {
        receiveMessageFromPushResult.events.collect { event ->
            if(event is ReceiveMessageFromPushEvent.ReceivePushEvent) {
                viewModel.deleteRemindAfterPush(event.id)
            }
        }
    }

}
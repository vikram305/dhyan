package projects.vikky.com.dhyan.di.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import projects.vikky.com.dhyan.di.ViewModelKey
import projects.vikky.com.dhyan.ui.main.chart.ChartViewModel
import projects.vikky.com.dhyan.ui.main.chart.DisplayChartViewModel
import projects.vikky.com.dhyan.ui.main.dashboard.DashboardViewModel
import projects.vikky.com.dhyan.ui.main.entries.EntriesViewModel
import projects.vikky.com.dhyan.ui.main.machine.MachineViewModel
import projects.vikky.com.dhyan.ui.main.posts.PostViewModel
import projects.vikky.com.dhyan.ui.main.profile.ProfileViewModel

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun bindPostsViewModel(postViewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MachineViewModel::class)
    abstract fun bindMachineViewModel(machineViewModel: MachineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EntriesViewModel::class)
    abstract fun bindEntriesViewModel(entriesViewModel: EntriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChartViewModel::class)
    abstract fun bindChartViewModel(chartViewModel: ChartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisplayChartViewModel::class)
    abstract fun bindDisplayChartViewModel(displayChartViewModel: DisplayChartViewModel): ViewModel
}
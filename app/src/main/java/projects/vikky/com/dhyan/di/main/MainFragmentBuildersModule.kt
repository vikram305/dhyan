package projects.vikky.com.dhyan.di.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import projects.vikky.com.dhyan.ui.main.chart.ChartFragment
import projects.vikky.com.dhyan.ui.main.chart.DisplayChartFragment
import projects.vikky.com.dhyan.ui.main.dashboard.DashboardFragment
import projects.vikky.com.dhyan.ui.main.entries.EntriesFragment
import projects.vikky.com.dhyan.ui.main.machine.MachineFragment
import projects.vikky.com.dhyan.ui.main.posts.PostsFragment
import projects.vikky.com.dhyan.ui.main.profile.ProfileFragment

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributePostsFragment(): PostsFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeMachineFragment(): MachineFragment

    @ContributesAndroidInjector
    abstract fun contributeEntriesFragment(): EntriesFragment

    @ContributesAndroidInjector
    abstract fun contributeChartFragment(): ChartFragment

    @ContributesAndroidInjector
    abstract fun contributeDisplayChartFragment(): DisplayChartFragment
}
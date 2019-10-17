package projects.vikky.com.dhyan.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import projects.vikky.com.dhyan.di.auth.AuthModule
import projects.vikky.com.dhyan.di.auth.AuthScope
import projects.vikky.com.dhyan.di.auth.AuthViewModelsModule
import projects.vikky.com.dhyan.di.main.MainFragmentBuildersModule
import projects.vikky.com.dhyan.di.main.MainModule
import projects.vikky.com.dhyan.di.main.MainScope
import projects.vikky.com.dhyan.di.main.MainViewModelsModule
import projects.vikky.com.dhyan.ui.PlaceHolder
import projects.vikky.com.dhyan.ui.auth.AuthActivity
import projects.vikky.com.dhyan.ui.main.MainAcitvity

@Module
abstract class ActivityBuilderModule {


    @ContributesAndroidInjector
    abstract fun contributePlaceHolderActivity(): PlaceHolder

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthViewModelsModule::class,
            AuthModule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity


    @MainScope
    @ContributesAndroidInjector(
        modules = [MainFragmentBuildersModule::class,
            MainViewModelsModule::class,
            MainModule::class]
    )
    abstract fun contributeMainActivity(): MainAcitvity


}
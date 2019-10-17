package projects.vikky.com.dhyan.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}
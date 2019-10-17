package projects.vikky.com.dhyan.di.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import projects.vikky.com.dhyan.di.ViewModelKey
import projects.vikky.com.dhyan.ui.auth.AuthViewModel

@Module
abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel


}
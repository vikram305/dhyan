package projects.vikky.com.dhyan.di.auth

import dagger.Module
import dagger.Provides
import projects.vikky.com.dhyan.network.auth.AuthApi
import retrofit2.Retrofit

@Module
object AuthModule {

    @AuthScope
    @JvmStatic
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
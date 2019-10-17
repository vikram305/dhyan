package projects.vikky.com.dhyan.di.main

import dagger.Module
import dagger.Provides
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.entries.EntryRecyclerAdapter
import projects.vikky.com.dhyan.ui.main.machine.MachineRecyclerAdapter
import projects.vikky.com.dhyan.ui.main.posts.PostRecyclerAdapter
import retrofit2.Retrofit

@Module
object MainModule {

    @MainScope
    @JvmStatic
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }

    @MainScope
    @JvmStatic
    @Provides
    fun provideAdapter(): PostRecyclerAdapter {
        return PostRecyclerAdapter()
    }

    @MainScope
    @JvmStatic
    @Provides
    fun provideMachineAdapter(): MachineRecyclerAdapter {
        return MachineRecyclerAdapter()
    }

    @MainScope
    @JvmStatic
    @Provides
    fun provideEntryAdapter(): EntryRecyclerAdapter {
        return EntryRecyclerAdapter()
    }
}
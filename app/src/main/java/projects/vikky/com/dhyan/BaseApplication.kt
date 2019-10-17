package projects.vikky.com.dhyan

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import projects.vikky.com.dhyan.di.DaggerAppComponent

open class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}
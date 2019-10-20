package projects.vikky.com.dhyan.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import projects.vikky.com.dhyan.BaseActivity
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.models.User
import projects.vikky.com.dhyan.ui.auth.AuthResource

class MainAcitvity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val TAG: String = "MainActivity"

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var user: LiveData<AuthResource<User>>
    private lateinit var headerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

//        var viewHeader=navigationView.getHeaderView(0)
//        headerTextView =viewHeader.findViewById(R.id.headerTitle) as TextView
//        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show()

        init()
//        observers()
    }

//    private fun observers() {
//        user=sessionManager.getAuthUser()
//
//        user.observe(this, Observer {
//            if(it.status==AuthResource.AuthStatus.AUTHENTICATED){
//                headerTextView.text="Welcome, ${it.userData?.userData?.name}"
//            }
//        })
//    }

    private fun init() {
        var navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflator: MenuInflater = menuInflater
//        inflator.inflate(R.menu.main_menu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.logout -> {
                sessionManager.logout(loggingout = true)
                return true
            }

            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                } else {
                    return false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.nav_logout -> {
                sessionManager.logout(loggingout = true)
            }
            R.id.nav_dashboard -> {
                var navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.main, true)
                    .build()
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                    R.id.dashboardScreen,
                    null,
                    navOptions
                )
            }
            R.id.nav_machine -> {
                var navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.machineFragment, true)
                    .build()
                if (isValidDestination(R.id.machineFragment)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                        R.id.machineFragment,
                        null, navOptions
                    )
                }
            }

            R.id.nav_entries -> {
                var navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.entriesFragment, true)
                    .build()
                if (isValidDestination(R.id.entriesFragment)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                        R.id.entriesFragment,
                        null, navOptions
                    )
                }
            }
            R.id.nav_chart -> {
                var navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.chartFragment, true)
                    .build()
                if (isValidDestination(R.id.nav_chart)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                        R.id.chartFragment,
                        null, navOptions
                    )
                }
            }
//            R.id.nav_profile->{
//                if(isValidDestination(R.id.profileScreen)){
//                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.profileScreen)
//                }
//
//            }
        }

        menuItem.setChecked(true)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun isValidDestination(destination: Int): Boolean {
        return destination != Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        ).currentDestination!!.id
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment),
            drawerLayout
        )
    }
}
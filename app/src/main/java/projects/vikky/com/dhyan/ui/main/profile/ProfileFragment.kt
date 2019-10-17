package projects.vikky.com.dhyan.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import dagger.android.support.DaggerFragment
import projects.vikky.com.dhyan.R

class ProfileFragment : DaggerFragment() {

    private val TAG: String = "ProfileFragment"
//    private lateinit var viewModel: ProfileViewModel

//    @Inject
//    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var emailTV: TextView
    private lateinit var unameTV: TextView
    private lateinit var websiteTV: TextView
    private lateinit var contactTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity, "Profile Fragment", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment was created ")
//        emailTV=view.findViewById(R.id.email)
//        contactTV=view.findViewById(R.id.contactTextView)
//        unameTV=view.findViewById(R.id.username)
//        websiteTV=view.findViewById(R.id.website)

        emailTV.text = ""

//        viewModel=ViewModelProviders.of(this,providerFactory).get(ProfileViewModel::class.java )
//        subscribeObservers()
    }

//    private fun subscribeObservers(){
//        viewModel.getAuthenticatedUser().removeObservers(viewLifecycleOwner)
//        viewModel.getAuthenticatedUser().observe(
//            viewLifecycleOwner, Observer {
//                if(it!=null){
//                    when(it.status){
//                        AuthResource.AuthStatus.AUTHENTICATED->{
//                            setUserDetails(it.data)
//                        }
//                        AuthResource.AuthStatus.LOADING->{
//                            setErrorDetails(it.message)
//                        }
//                    }
//                }
//            }
//        )
//    }
//
//    private fun setErrorDetails(message: String?) {
//        emailTV.text=message
//        unameTV.text="error"
//        websiteTV.text="error"
//    }
//
//    private fun setUserDetails(data: User?) {
//        emailTV.text=data?.userData?.email
//        unameTV.text=data?.userData?.name
//        websiteTV.text=data?.userData?.mobile.toString()
//    }
}
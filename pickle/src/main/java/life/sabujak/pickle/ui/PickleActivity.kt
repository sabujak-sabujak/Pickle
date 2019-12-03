package life.sabujak.pickle.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import io.reactivex.disposables.CompositeDisposable
import life.sabujak.pickle.R
import life.sabujak.pickle.data.PickleContentObserver
import life.sabujak.pickle.databinding.ActivityPickleBinding
import life.sabujak.pickle.util.Logger

class PickleActivity : AppCompatActivity() {

    private val logger = Logger.getLogger("PickleActivity")
    private val picklePermission = PicklePermission(this)
    private val disposables = CompositeDisposable()
    private val viewModel: PickleViewModel by viewModels()

    private lateinit var binding: ActivityPickleBinding
    private lateinit var contentObserver: PickleContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.d("onCreate")
        disposables.add( picklePermission.request().subscribe { granted ->
            logger.d("pickle permission = $granted")
            if(granted){
                initUI()
            }else{
                logger.w("pickle permission has not granted")
            }
        })
    }

    private fun initUI(){
        logger.d("initUI")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pickle)

        val navHostFragment = NavHostFragment()
        supportFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(navHostFragment)
            .replace(R.id.nav_host_container, navHostFragment)
            .commitNow()

        val navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.pickle_graph)

        if(intent.action!=null && intent.action!!.equals("insta")){
            navGraph.startDestination = R.id.instagramFragment
        }else{
            navGraph.startDestination = R.id.pickleFragment
        }
        navController.graph = navGraph

        contentObserver = PickleContentObserver(this)
        contentObserver.contentChangedEvent.observe(this,
            Observer {
                logger.i("onChangedEvent From PickleContentObserver")
                viewModel.invalidateDataSource()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!disposables.isDisposed){
            disposables.dispose()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        picklePermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
        logger.d("onRequestPermissionsResult")
    }
}

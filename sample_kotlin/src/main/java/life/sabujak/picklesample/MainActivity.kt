package life.sabujak.picklesample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import life.sabujak.pickle.Pickle
import life.sabujak.pickle.ui.dialog.OnResultListener
import life.sabujak.pickle.ui.dialog.PickleResult
import life.sabujak.picklesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.basic.setOnClickListener {
            Pickle.start(supportFragmentManager, object :
                OnResultListener {
                override fun onSuccess(result: PickleResult) {
                    result.mediaList.forEach {
                        Log.e("MainActivity", it.toString())
                    }
                    val adapter = RvAdapter(ArrayList(result.mediaList))
                    binding.rlView.adapter = adapter
                }
                override fun onError(t: Throwable) {
                }
            })
        }

        binding.insta.setOnClickListener {
            Pickle.start(supportFragmentManager, R.id.frag_container, object :
                OnResultListener {
                override fun onSuccess(result: PickleResult) {
                    result.mediaList.forEach {
                        Log.e("MainActivity", it.toString())
                    }
                    val adapter = RvAdapter(ArrayList(result.mediaList))
                    binding.rlView.adapter = adapter
                }
                override fun onError(t: Throwable) {
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        actionBar?.show()
    }
}

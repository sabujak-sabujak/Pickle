package life.sabujak.picklesample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import life.sabujak.pickle.Pickle
import life.sabujak.pickle.PickleType
import life.sabujak.pickle.ui.PickleActivity
import life.sabujak.picklesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        //Pickle을 어떤식으로 호출할지 고민중
        //임시로 임의의 action을 정의함
        binding.insta.setOnClickListener {
            startActivity(Intent(this, PickleActivity::class.java).apply { action = "insta" })
        }
        binding.basic.setOnClickListener {
//            startActivity(Intent(this, PickleActivity::class.java).apply { action="basic" })
            Pickle.start(this, PickleType.BASIC)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            Pickle.onActivityResult(requestCode, resultCode, it) { items ->
                Log.e("MainActivity", "${items.size}")
                items.forEach {
                    Log.e("MainActivity", "$it")
                }
            }
        }

    }
}

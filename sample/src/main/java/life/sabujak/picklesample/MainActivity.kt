package life.sabujak.picklesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import life.sabujak.pickle.ui.PickleActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickle)
        startActivity(Intent(this, PickleActivity::class.java))
    }
}

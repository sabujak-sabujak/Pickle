package life.sabujak.pickle.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import life.sabujak.pickle.databinding.ActivityPickleBinding
import javax.inject.Inject

class PickleActivity : AppCompatActivity() {

    @Inject
    lateinit var binding:ActivityPickleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerPickleComponent.factory().create(this).inject(this)
    }
}

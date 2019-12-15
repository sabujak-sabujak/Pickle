package life.sabujak.pickle.util.ext

import androidx.fragment.app.Fragment

fun Fragment.showToast(message:String){
    activity?.showToast(message)
}
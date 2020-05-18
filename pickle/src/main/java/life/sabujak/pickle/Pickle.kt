package life.sabujak.pickle

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import life.sabujak.pickle.data.entity.ErrorCode
import life.sabujak.pickle.data.entity.PickleError
import life.sabujak.pickle.ui.dialog.Config
import life.sabujak.pickle.ui.dialog.OnResultListener
import life.sabujak.pickle.ui.dialog.PickleDialogFragment
import life.sabujak.pickle.ui.insta.InstaFragment

class Pickle {
    companion object{
        @JvmStatic
        fun start(context:Context, fragmentManager: FragmentManager, listener: OnResultListener) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                PickleDialogFragment(Config.Builder(listener).build()).show(
                    fragmentManager,
                    PickleDialogFragment::class.simpleName
                )
            }
            else{
                listener.onError(PickleError(ErrorCode.NO_PERMISSION))
            }
        }
        @JvmStatic
        fun start(fragmentManager: FragmentManager, containerId: Int, listener: OnResultListener){
            //FIXME Insta용 Config 별도로 분리
            fragmentManager.beginTransaction()
                .add(containerId, InstaFragment(Config.Builder(listener).build()),"Insta")
                .addToBackStack(null)
                .commit()
        }
    }

}
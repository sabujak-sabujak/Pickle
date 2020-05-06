package life.sabujak.pickle.ui.dialog

import life.sabujak.pickle.data.entity.PickleError

interface OnResultListener {
    fun onSuccess(result: PickleResult)
    fun onError(pickleError: PickleError)
}
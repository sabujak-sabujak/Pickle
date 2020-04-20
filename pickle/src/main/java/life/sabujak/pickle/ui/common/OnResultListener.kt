package life.sabujak.pickle.ui.common

import life.sabujak.pickle.data.entity.PickleResult

interface OnResultListener {
    fun onSuccess(result: PickleResult)
    fun onFailure(t: Throwable)
}
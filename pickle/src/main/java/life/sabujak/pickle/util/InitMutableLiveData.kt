package life.sabujak.pickle.util

import androidx.lifecycle.MutableLiveData

class InitMutableLiveData<T>(value:T):MutableLiveData<T>(){
    init {
        setValue(value)
    }
}
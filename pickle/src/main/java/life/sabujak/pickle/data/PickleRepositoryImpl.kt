package life.sabujak.pickle.data

import android.content.Context
import io.reactivex.Observable
import life.sabujak.pickle.data.entity.VisualMedia

class PickleRepositoryImpl constructor(val context: Context) :
    PickleRepository {

    override fun load(): Observable<VisualMedia> {
//        context.contentResolver.query()
        TODO()
    }

}
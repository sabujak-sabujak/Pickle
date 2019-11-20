package life.sabujak.pickle.data

import io.reactivex.Observable
import life.sabujak.pickle.data.entity.VisualMedia

interface PickleRepository{
    fun load():Observable<VisualMedia>
}
package life.sabujak.pickle.data.datasource

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class DataSourceState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED = DataSourceState(
            Status.SUCCESS
        )
        val LOADING = DataSourceState(
            Status.RUNNING
        )
        fun error(msg: String?) =
            DataSourceState(
                Status.FAILED,
                msg
            )
    }
}
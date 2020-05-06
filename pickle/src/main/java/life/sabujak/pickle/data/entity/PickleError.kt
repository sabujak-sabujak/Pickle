package life.sabujak.pickle.data.entity

import life.sabujak.pickle.data.entity.ErrorCode

class PickleError(val errorCode: ErrorCode, vararg args: String)
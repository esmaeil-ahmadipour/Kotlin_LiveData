package ir.ea2.kotlin_livedata.data.remote;

import ir.ea2.kotlin_livedata.util.AppStatus

class Resource<T> private constructor(val status: AppStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(AppStatus.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T?): Resource<T> {
            return Resource(AppStatus.ERROR, data, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(AppStatus.LOADING, null, null)
        }
    }
}

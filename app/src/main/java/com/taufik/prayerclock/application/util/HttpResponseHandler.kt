package com.taufik.prayerclock.application.util

import android.content.Context
import com.taufik.prayerclock.R
import com.taufik.prayerclock.data.dto.ResponseWrapper
import retrofit2.Response

abstract class HttpResponseHandler(
    private val context: Context
) {
    protected fun <T : Any> Response<ResponseWrapper<T>>.handleWrappedResponse(): T {
        if (this.isSuccessful.not()) throw Exception(
            this.message().orDefault(context.getString(R.string.err_msg_internal_server_error))
        )

        this.body()?.let { responseWrapper ->
            if (responseWrapper.status == true) {
                return responseWrapper.data
                    ?: throw Exception(context.getString(R.string.err_msg_internal_server_error))
            } else {
                throw Exception(
                    responseWrapper.message.orDefault(context.getString(R.string.err_msg_internal_server_error))
                )
            }
        } ?: throw Exception(context.getString(R.string.err_msg_internal_server_error))
    }

    protected fun <T : Any> Response<T>.handleDirectResponse(): T {
        if (this.isSuccessful.not()) throw Exception(
            this.message().orDefault(context.getString(R.string.err_msg_internal_server_error))
        )

        return this.body()
            ?: throw Exception(context.getString(R.string.err_msg_internal_server_error))
    }
}
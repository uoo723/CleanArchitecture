package com.umanji.umanjiapp.common.util

import com.umanji.umanjiapp.data.network.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class RestApiErrorUtils
@Inject constructor(private val converter: Converter<ResponseBody, ApiResponse<*>>) {

    open fun parseError(response: Response<*>): ApiResponse<*> {
            return converter.convert(response.errorBody()!!)
    }
}

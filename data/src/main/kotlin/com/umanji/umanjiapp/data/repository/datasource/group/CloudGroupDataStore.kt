package com.umanji.umanjiapp.data.repository.datasource.group

import com.google.gson.Gson
import com.umanji.umanjiapp.data.entity.GroupEntity
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.data.util.MediaUtils
import io.reactivex.Completable
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by paulhwang on 18/02/2018.
 */

@Singleton
open class CloudGroupDataStore @Inject constructor(
        private val restApi: RestApi
) : GroupDataStore {
    override fun createGroup(group: GroupEntity): Completable {
        val map: MutableMap<String, RequestBody> = HashMap()

        val groupName = createPartFromString(group.group_name)

        map["group_name"] = groupName

        return restApi.createGroup(map)
    }

    private fun createPartFromString(str: String, mimeType: String = "text/plain"): RequestBody {
        return RequestBody.create(MediaType.parse(mimeType), str)
    }
}


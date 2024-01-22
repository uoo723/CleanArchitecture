package com.umanji.umanjiapp.data.repository.datasource.post

import android.net.Uri
import com.google.gson.Gson
import com.umanji.umanjiapp.data.entity.LocationEntity
import com.umanji.umanjiapp.data.entity.PostEntity
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.data.util.MediaUtils
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [PostDataStore] implementation based on connections to the api.
 *
 * @constructor A [PostDataStore] based on connections to the api (Cloud).
 * @property restApi The [RestApi] implementation to use.
 */
@Singleton
open class CloudPostDataStore @Inject constructor(
        private val restApi: RestApi,
        private val gson: Gson,
        private val mediaUtils: MediaUtils
) : PostDataStore {

    override fun posts(offset: Int, portalName: String, portalType: String,
                       limit: Int?): Single<List<PostEntity>> {
        return restApi.posts(offset, portalName, portalType, limit).map { it.data }
    }

    override fun posts(latitude: Double, longitude: Double, zoom: Int, limit: Int?)
            : Single<List<PostEntity>> {
        return restApi.posts(latitude, longitude, zoom, limit).map { it.data }
    }

    override fun posts(type: String, id: String, limit: Int?): Single<List<PostEntity>> {
        return restApi.posts(type, id, limit).map { it.data }
    }

    override fun posts(userId: String): Single<List<PostEntity>> {
        return restApi.posts(userId).map { it.data }
    }

    override fun post(id: String): Single<PostEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGeoType(location: LocationEntity): Single<PostEntity> {
        return restApi.getGeoType(location.coordinates[1], location.coordinates[0]).map { it.data }
    }

    override fun createPost(post: PostEntity): Completable {
        val image = post.images.let {
            if (it.isNotEmpty()) prepareFilePart("image", it[0])
            else null
        }

        val map: MutableMap<String, RequestBody> = HashMap()

        val content = createPartFromString(post.content)
        val createType = createPartFromString(post.createType)
        val placeType = createPartFromString(post.placeType)
        val placeId = createPartFromString(post.placeId)
        val placeGoogleId = createPartFromString(post.placeGoogleId)
        val portalLevel = createPartFromString(post.portalLevel)
        val ownerName = createPartFromString(post.ownerName)
        val ownerId = createPartFromString(post.ownerId)
        val location = createPartFromString(gson.toJson(post.location), "application/json")
        val countryCode = createPartFromString(post.countryCode)
        val latitude = createPartFromString(post.location.coordinates[1].toString())
        val longitude = createPartFromString(post.location.coordinates[0].toString())

        map["content"] = content
        map["create_type"] = createType
        map["place_type"] = placeType
        map["place_rid"] = placeId
        map["place_id"] = placeGoogleId
        map["portal_level"] = portalLevel
        map["owner_name"] = ownerName
        map["owner_id"] = ownerId
        map["location"] = location
        map["countryCode"] = countryCode
        map["latitude"] = latitude
        map["longitude"] = longitude

        return restApi.createPost(map, image)
    }

    override fun updatePost(post: PostEntity): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePost(id: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createPartFromString(str: String, mimeType: String = "text/plain"): RequestBody {
        return RequestBody.create(MediaType.parse(mimeType), str)
    }

    private fun prepareFilePart(name: String, uriString: String): MultipartBody.Part {
        val uri: Uri = Uri.parse(uriString)
        val file = File(mediaUtils.getPath(uri))
        val mimeType: String = mediaUtils.getMimeType(uri)
        return MultipartBody.Part.createFormData(name, file.name,
                RequestBody.create(MediaType.parse(mimeType), file))
    }
}

package com.umanji.umanjiapp.data.repository.datasource.post

import com.umanji.umanjiapp.data.entity.LocationEntity
import com.umanji.umanjiapp.data.entity.PostEntity
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Interface that represents a data store from where post data is retrieved.
 */
internal interface PostDataStore {

    /**
     * Get a [Single] which will emit a list of [PostEntity].
     *
     * @param offset offset
     * @param portalName portal Name
     * @param portalType portal type
     * @param limit limit
     */
    fun posts(offset: Int = 0, portalName: String, portalType: String,
              limit: Int? = null): Single<List<PostEntity>>

    /**
     * Get a [Single] which will emit a list of [PostEntity].
     *
     * @param latitude latitude
     * @param longitude longitude
     * @param zoom zoom
     * @param limit limit
     */
    fun posts(latitude: Double, longitude: Double, zoom: Int, limit: Int? = null)
            : Single<List<PostEntity>>

    /**
     * Get a [Single] which will emit a list of [PostEntity].
     *
     * @param type type
     * @param id id
     * @param limit limit
     */
    fun posts(type: String, id: String, limit: Int? = null): Single<List<PostEntity>>

    /**
     * Get a [Single] which will emit a list of [PostEntity].
     *
     * @param userId user id
     */
    fun posts(userId: String): Single<List<PostEntity>>

    /**
     * Get a [Single] which will emit a [PostEntity].
     *
     * @param id The post id used to retrieve post data.
     */
    fun post(id: String): Single<PostEntity>

    /**
     * Get a [Single] which will emit a [PostEntity].
     *
     * @param location The location where post will be created.
     */
    fun getGeoType(location: LocationEntity): Single<PostEntity>

    /**
     * Get a [Completable] which will be completed if creating [PostEntity] is succeed.
     *
     * @param post The [PostEntity] to be newly created.
     */
    fun createPost(post: PostEntity): Completable

    /**
     * Get a [Completable] which will be completed if updating [PostEntity] is succeed.
     *
     * @param post The [PostEntity] to be updated.
     */
    fun updatePost(post: PostEntity): Completable

    /**
     * Get a [Completable] which will be completed if deleting [PostEntity] is succeed.
     *
     * @param id The id of [PostEntity] to be deleted.
     */
    fun deletePost(id: String): Completable
}

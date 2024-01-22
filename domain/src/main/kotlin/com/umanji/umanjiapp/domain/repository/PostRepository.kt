package com.umanji.umanjiapp.domain.repository

import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Post
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a Repository for [Post].
 */
interface PostRepository {

    /**
     * Get a [Single] which will emit a list of [Post].
     *
     * @param offset offset
     * @param portalName portal name
     * @param portalType portal type
     * @param limit limit
     */
    fun posts(offset: Int = 0, portalName: String, portalType: String,
              limit: Int? = null): Single<List<Post>>

    /**
     * Get a [Single] which will emit a list of [Post].
     *
     * @param latitude latitude
     * @param longitude longitude
     * @param zoom zoom
     * @param limit limit
     */
    fun posts(latitude: Double, longitude: Double, zoom: Int, limit: Int? = null): Single<List<Post>>

    /**
     * Get a [Single] which will emit a list of [Post].
     *
     * @param type type
     * @param id id
     * @param limit limit
     */
    fun posts(type: String, id: String, limit: Int? = null): Single<List<Post>>

    /**
     * Get a [Single] which will emit a list of [Post].
     *
     * @param userId user id
     */
    fun posts(userId: String): Single<List<Post>>

    /**
     * Get a [Single] which will emit a [Post].
     *
     * @param id The post id used to retrieve post data.
     */
    fun post(id: String): Single<Post>

    /**
     * Get a [Single] which will emit a [Post].
     *
     * @param location The location where post will be created.
     */
    fun geoType(location: Location): Single<Post>

    /**
     * Get a [Completable] which will be completed if creating [Post] is succeed.
     *
     * @param post The [Post] to be newly created.
     */
    fun createPost(post: Post): Completable

    /**
     * Get a [Completable] which will be completed if updating [Post] is succeed.
     *
     * @param post The [Post] to be updated.
     */
    fun updatePost(post: Post): Completable

    /**
     * Get a [Completable] which will be completed if deleting [Post] is succeed.
     *
     * @param id The id of [Post] to be deleted.
     */
    fun deletePost(id: String): Completable
}

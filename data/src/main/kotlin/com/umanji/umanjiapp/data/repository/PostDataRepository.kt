package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.LocationEntityMapper
import com.umanji.umanjiapp.data.entity.mapper.PostEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.post.CloudPostDataStore
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [PostRepository] for retrieving post data.
 */
@Singleton
class PostDataRepository @Inject constructor(
        private val cloudPostDataStore: CloudPostDataStore
) : PostRepository {

    override fun posts(offset: Int, portalName: String, portalType: String,
                       limit: Int?): Single<List<Post>> {
        return cloudPostDataStore.posts(offset, portalName, portalType, limit)
                .map { it.map(PostEntityMapper::transformDomain) }
    }

    override fun posts(latitude: Double, longitude: Double, zoom: Int, limit: Int?)
            : Single<List<Post>> {
        return cloudPostDataStore.posts(latitude, longitude, zoom, limit)
                .map { it.map(PostEntityMapper::transformDomain) }
    }

    override fun posts(type: String, id: String, limit: Int?): Single<List<Post>> {
        return cloudPostDataStore.posts(type, id, limit)
                .map { it.map(PostEntityMapper::transformDomain) }
    }

    override fun posts(userId: String): Single<List<Post>> {
        return cloudPostDataStore.posts(userId).map { it.map(PostEntityMapper::transformDomain) }
    }

    override fun post(id: String): Single<Post> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun geoType(location: Location): Single<Post> {
        return cloudPostDataStore.getGeoType(LocationEntityMapper.transformData(location))
                .map { PostEntityMapper.transformDomain(it) }
    }

    override fun createPost(post: Post): Completable {
        return cloudPostDataStore.createPost(PostEntityMapper.transformData(post))
    }

    override fun updatePost(post: Post): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePost(id: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

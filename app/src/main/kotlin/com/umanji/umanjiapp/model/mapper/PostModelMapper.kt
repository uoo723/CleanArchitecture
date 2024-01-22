package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.model.PostModel


/**
 * Mapper for [Post] and [PostModel].
 */
object PostModelMapper : ModelMapper<Post, PostModel> {

    override fun transform(domainEntity: Post): PostModel {
        return PostModel(
                id = domainEntity.id,
                content = domainEntity.content,
                locality = domainEntity.adminDistrict?.locality ?: "",
                subLocality1 = domainEntity.adminDistrict?.subLocality1 ?: "",
                subLocality2 = domainEntity.adminDistrict?.subLocality2 ?: "",
                premise = domainEntity.adminDistrict?.premise ?: "",
                placeType = domainEntity.place?.type?.name ?: "",
                createType = domainEntity.createType,
                userName = domainEntity.user?.name ?: "",
                userId = domainEntity.user?.id ?: "",
                placeName = domainEntity.place?.name ?: "",
                placeId = domainEntity.place?.id ?: "",
                placeGoogleId = domainEntity.place?.googleId ?: "",
                portalId = domainEntity.portal?.id ?: "",
                latitude = domainEntity.place?.location?.latitude ?: 0.0,
                longitude = domainEntity.place?.location?.longitude ?: 0.0,
                viewLevel = domainEntity.viewLevel,
                images = domainEntity.images.map { it.uri },
                channels = domainEntity.channels.map { it.name },
                createdAt = domainEntity.dateTime.createdAt,
                updatedAt = domainEntity.dateTime.updatedAt
        )
    }
}

package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.LocationEntity
import com.umanji.umanjiapp.data.entity.PostEntity
import com.umanji.umanjiapp.domain.entity.*

/**
 * Mapper for [Post] and [PostEntity].
 */
internal object PostEntityMapper : EntityMapper<PostEntity, Post> {

    override fun transformDomain(dataEntity: PostEntity): Post {
        return Post(
                id = dataEntity.id,
                content = dataEntity.content,
                user = User(
                        id = dataEntity.ownerId,
                        name = dataEntity.ownerName
                ),
                adminDistrict = AdminDistrict(
                        country = dataEntity.country,
                        countryCode = dataEntity.countryCode,
                        locality = dataEntity.locality,
                        subLocality1 = dataEntity.subLocalityLevel1,
                        subLocality2 = dataEntity.subLocalityLevel2,
                        premise = dataEntity.premise
                ),
                place = Place(
                        id = dataEntity.placeId,
                        googleId = dataEntity.placeGoogleId,
                        name = dataEntity.placeName,
                        type = when (dataEntity.placeType) {
                            PlaceType.SPACE.value -> PlaceType.SPACE
                            PlaceType.BUILDING.value -> PlaceType.BUILDING
                            PlaceType.MULTI.value -> PlaceType.MULTI
                            else -> PlaceType.SPACE
                        },
                        location = try {
                            Location(
                                    dataEntity.location.coordinates[1],
                                    dataEntity.location.coordinates[0]
                            )
                        } catch (e: IndexOutOfBoundsException) {
                            Location(0.0, 0.0)
                        }
                ),
                portal = Portal(
                        id = dataEntity.portalId
                ),
                images = dataEntity.images.map { Image(uri = it) },
                createType = dataEntity.createType,
                portalLevel = dataEntity.portalLevel,
                viewLevel = try {
                    dataEntity.viewLevel.toInt()
                } catch (e: Throwable) {
                    0
                },
                channels = dataEntity.channels.map { Channel(name = it) },
                dateTime = EntityDateTime(dataEntity.createdAt, dataEntity.updatedAt)
        )
    }

    override fun transformData(domainEntity: Post): PostEntity {
        return PostEntity(
                id = domainEntity.id,
                content = domainEntity.content,
                location = LocationEntity(
                        coordinates = listOf(
                                domainEntity.place?.location?.longitude ?: 0.0,
                                domainEntity.place?.location?.latitude ?: 0.0
                        )
                ),
                images = domainEntity.images.map { it.uri },
                createType = domainEntity.createType,
                portalLevel = domainEntity.portalLevel,
                country = domainEntity.adminDistrict?.country ?: "",
                countryCode = domainEntity.adminDistrict?.countryCode ?: "",
                locality = domainEntity.adminDistrict?.locality ?: "",
                subLocalityLevel1 = domainEntity.adminDistrict?.subLocality1 ?: "",
                subLocalityLevel2 = domainEntity.adminDistrict?.subLocality2 ?: "",
                premise = domainEntity.adminDistrict?.premise ?: "",
                channels = domainEntity.channels.map { it.name },
                ownerId = domainEntity.user?.id ?: "",
                ownerName = domainEntity.user?.name ?: "",
                placeId = domainEntity.place?.id ?: "",
                placeGoogleId = domainEntity.place?.googleId ?: "",
                portalId = domainEntity.portal?.id ?: "",
                placeName = domainEntity.place?.name ?: "",
                placeType = domainEntity.place?.type?.value ?: "",
                createdAt = domainEntity.dateTime.createdAt,
                updatedAt = domainEntity.dateTime.updatedAt
        )
    }
}

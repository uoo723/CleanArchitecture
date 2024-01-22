package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.domain.entity.Portal
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PortalEntityMapperTest : Spek({
    on("which domain entity transformation is successful") {
        val portalEntity = PortalEntity(id = "1")

        val portal = PortalEntityMapper.transformDomain(portalEntity)

        it("must be 1") {
            portal.id shouldEqual "1"
        }
    }

    on("which data entity transformation is successful") {
        val portal = Portal(id = "2")
        val portalEntity = PortalEntityMapper.transformData(portal)

        it("must be 2") {
            portalEntity.id shouldEqual "2"
        }
    }
})

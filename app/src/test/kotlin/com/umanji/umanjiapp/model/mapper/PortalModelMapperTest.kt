package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Portal
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PortalModelMapperTest : Spek({
    on("which model transformation is successful") {
        val portal = Portal(id = "1")

        val portalModel = PortalModelMapper.transform(portal)

        it("must be 1") {
            portalModel.id shouldEqual "1"
        }
    }
})

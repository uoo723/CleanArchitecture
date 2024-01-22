package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.AuthInfoEntity
import com.umanji.umanjiapp.domain.entity.AuthInfo
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class AuthInfoEntityMapperTest : Spek({
    on("which domain entity transformation is successful") {
        val authInfoEntity = AuthInfoEntity(id = "test")

        val authInfo = AuthInfoEntityMapper.transformDomain(authInfoEntity)

        it("must be test") {
            authInfo.id!! shouldEqual "test"
        }
    }

    on("which data entity transformation is successful") {
        val authInfo = AuthInfo(id = "test2")
        val authInfoEntity = AuthInfoEntityMapper.transformData(authInfo)

        it("must be test2") {
            authInfoEntity.id!! shouldEqual "test2"
        }
    }
})

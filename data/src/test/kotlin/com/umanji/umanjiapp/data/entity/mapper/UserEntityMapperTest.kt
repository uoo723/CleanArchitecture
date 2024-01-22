package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.domain.entity.User
import org.amshove.kluent.shouldEqual
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


class UserEntityMapperTest : Spek({

    val currentDate = Calendar.getInstance().time!!

    on("which domain entity transformation is successful") {
        val userEntity = UserEntity(
                id = "123",
                email = "example@example.com",
                phone = "01012345678",
                userName = "test user",
                createdAt = currentDate,
                updatedAt = currentDate
        )

        val user = UserEntityMapper.transformDomain(userEntity)

        it("must be instance of User") {
            assertThat(user, `is`(instanceOf(User::class.java)))
            user.id shouldEqual "123"
        }
    }

    on("which data entity transformation is successful") {
        val user = User(id = "555")

        val userEntity = UserEntityMapper.transformData(user)

        it("must be 555") {
            userEntity.id shouldEqual "555"
        }
    }
})

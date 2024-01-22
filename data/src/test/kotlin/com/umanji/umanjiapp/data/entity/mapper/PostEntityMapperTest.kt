package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.PostEntity
import com.umanji.umanjiapp.domain.entity.Post
import org.amshove.kluent.shouldEqual
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PostEntityMapperTest : Spek({
    on("which domain entity transformation is successful") {
        val postEntity = PostEntity(id = "1")

        val post = PostEntityMapper.transformDomain(postEntity)

        it("must be instance of User") {
            MatcherAssert.assertThat(post, `is`(instanceOf(Post::class.java)))
            post.id shouldEqual "1"
        }
    }

    on("which data entity transformation is successful") {
        val post = Post(id = "2")
        val postEntity = PostEntityMapper.transformData(post)

        it("must be 2") {
            postEntity.id shouldEqual "2"
        }
    }
})

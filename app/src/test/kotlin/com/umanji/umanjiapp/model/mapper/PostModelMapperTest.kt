package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Post
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PostModelMapperTest : Spek({
    on("which model transformation is successful") {
        val post = Post(id = "1")

        val postModel = PostModelMapper.transform(post)

        it("must 1") {
            postModel.id shouldEqual "1"
        }
    }
})

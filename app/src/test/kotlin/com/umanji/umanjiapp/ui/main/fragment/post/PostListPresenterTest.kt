package com.umanji.umanjiapp.ui.main.fragment.post

import android.content.Context
import com.nhaarman.mockitokotlin2.*
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.ui.fragment.post.PostListPresenter
import com.umanji.umanjiapp.ui.fragment.post.PostListView
import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PostListPresenterTest : Spek({
    lateinit var postListPresenter: PostListPresenter

    val mockContext: Context = mock()
    val mockGetPosts: GetPosts = mock()
    val mockNetworkUtils: NetworkUtils = mock()
    val mockPostListView: PostListView = mock()

    beforeEachTest {
        postListPresenter = PostListPresenter(mockContext, mockNetworkUtils, mockGetPosts)
    }

    afterEachTest {
        reset(mockGetPosts, mockNetworkUtils, mockPostListView)
    }

    on("that network is available") {
        When calling mockNetworkUtils.isConnected() itReturns true

        postListPresenter.bindView(mockPostListView)
        postListPresenter.getPosts()

        it("must be executed") {
            Verify on mockGetPosts that mockGetPosts.execute(
                    anyOrNull(),
                    any(),
                    any(),
                    any()) was called
        }
    }

    on("that network is unavailable") {
        When calling mockNetworkUtils.isConnected() itReturns false

        postListPresenter.bindView(mockPostListView)
        postListPresenter.getPosts()

        it("must show required network message") {
            Verify on mockPostListView that mockPostListView.showError(any()) was called
        }

        it("must be not executed") {
            verify(mockGetPosts, never()).execute(
                    anyOrNull(),
                    any(),
                    any(),
                    any())
        }
    }
})

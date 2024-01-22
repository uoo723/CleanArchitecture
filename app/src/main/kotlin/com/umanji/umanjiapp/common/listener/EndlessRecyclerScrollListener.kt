package com.umanji.umanjiapp.common.listener

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


@Suppress("unused")
abstract class EndlessRecyclerScrollListener private constructor(
        // The minimum amount of items to have below your current scroll position
        // before loading more
        private val visibleThreshold: Int,
        private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = EndlessRecyclerScrollListener::class.java.simpleName

    companion object {
        private const val DEFAULT_VISIBLE_THRESHOLD = 5
    }

    constructor(layoutManager: LinearLayoutManager) : this(DEFAULT_VISIBLE_THRESHOLD, layoutManager)
    constructor(layoutManager: GridLayoutManager)
            : this(DEFAULT_VISIBLE_THRESHOLD * layoutManager.spanCount, layoutManager)
    constructor(layoutManager: StaggeredGridLayoutManager)
            : this(DEFAULT_VISIBLE_THRESHOLD * layoutManager.spanCount, layoutManager)

    // The current offset index of data you have loaded
    private var currentPage: Int = 0
    // The total number of items in the data set after the last load
    private var previousTotalItemCount: Int = 0
    // True if we are still waiting for the last set of data to be load.
    private var loading: Boolean = true
    // Sets the starting page index
    private val startingIndex: Int = 0

    private fun getLastVisibleItem(lastVisiblePositions: IntArray): Int {
        return (0 until lastVisiblePositions.size)
                .reduce { acc, i ->
                    if (i == 0 || lastVisiblePositions[i] > acc) {
                        acc + lastVisiblePositions[i]
                    } else {
                        acc
                    }
                }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy <= 0) return

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager ->
                // get maximum element within the list
                getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null))
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw IllegalArgumentException("Unsupported layoutManager")
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingIndex
            previousTotalItemCount = totalItemCount
            loading = true
        }

        // If it's still loading, we check to see if the data set count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            currentPage++
            previousTotalItemCount = totalItemCount
            loading = false
        }

        // It it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch th data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            onLoadMore(currentPage, totalItemCount, recyclerView)
            loading = true
        }
    }

    fun resetState() {
        currentPage = startingIndex
        previousTotalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView)
}

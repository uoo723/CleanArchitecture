package com.umanji.umanjiapp.ui.main.fragment.map

import com.google.android.gms.maps.model.Marker
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BaseView


interface MapView : BaseView {
    fun showLocation(zoom: Int)
    fun showGeoType(marker: Marker, post: PostModel)
    fun showPosts(posts: List<PostModel>)
}

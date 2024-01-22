package com.umanji.umanjiapp.ui.main.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.common.util.AdminType
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.common.util.getBitmap
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.interactor.GetGeoType
import com.umanji.umanjiapp.domain.interactor.GetMe
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.model.mapper.PostModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import com.umanji.umanjiapp.ui.main.MainActivity
import com.umanji.umanjiapp.ui.post.WriteActivity
import kotlinx.android.synthetic.main.custom_map_marker.view.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@FragmentScope
class MapPresenter @Inject constructor(
        private val locationProvider: ReactiveLocationProvider,
        private val networkUtils: NetworkUtils,
        private val context: Context,
        private val getPosts: GetPosts,
        private val getMe: GetMe,
        private val getGeoType: GetGeoType
) : BasePresenter<MapView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MapPresenter::class.java.simpleName

    private var view: MapView? = null
    private var currentAdminType: AdminType? = null
    private var post: Post? = null

    var addedMarker: Marker? = null
    var googleMap: GoogleMap? = null

    override fun bindView(view: MapView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null

        getMe.dispose()
        getPosts.dispose()
        getGeoType.dispose()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity, googleMap: GoogleMap) {
        val intent: Intent = activity.intent
        val zoom: Int = intent.getIntExtra(MainActivity.KEY_TAB_POSITION, 0).let {
            when (it) {
                0 -> AdminType.getZoomLevel(AdminType.SUBLOCALITY2)
                1 -> AdminType.getZoomLevel(AdminType.SUBLOCALITY1)
                2 -> AdminType.getZoomLevel(AdminType.LOCALITY)
                3 -> AdminType.getZoomLevel(AdminType.COUNTRY)
                4 -> AdminType.getZoomLevel(AdminType.WORLD)
                else -> throw IllegalArgumentException("cannot be reached: tabPosition = $it")
            }
        }

        val latitude: Double? = intent.getDoubleExtra(MainActivity.KEY_LATITUDE, -1.0).let {
            when (it) {
                -1.0 -> null
                else -> it
            }
        }
        val longitude: Double? = intent.getDoubleExtra(MainActivity.KEY_LONGITUDE, -1.0).let {
            when (it) {
                -1.0 -> null
                else -> it
            }
        }

        Log.d(TAG, "zoom: $zoom, ($latitude, $longitude)")

        RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe {
                    if (it) {
                        getLocation(latitude, longitude, zoom)
                        googleMap.isMyLocationEnabled = true
                    } else {
                        Log.d(TAG, "ACCESS_FINE_LOCATION denied")
                    }
                }
    }

    fun getPosts(forced: Boolean = false, zoom: Int? = googleMap?.cameraPosition?.zoom?.toInt()) {
        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        val latitude = googleMap?.cameraPosition?.target?.latitude
        val longitude = googleMap?.cameraPosition?.target?.longitude

        if (latitude == null || longitude == null || zoom == null)
            return

        if (!forced && currentAdminType != null && currentAdminType == AdminType.getAdminType(zoom))
            return

        currentAdminType = AdminType.getAdminType(zoom)

        val params = GetPosts.Params2(
                latitude = latitude,
                longitude = longitude,
                zoom = zoom,
                limit = 20
        )
        view?.showProgress()

        getPosts.clear()
        getPosts.execute(params, {
            view?.hideProgress()
            googleMap?.clear()
            addedMarker = null
            view?.showPosts(it.map(PostModelMapper::transform))
        }, {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    view?.showError(it.message)
                    Log.d(TAG, "getPosts", it)
                }
            }
        })
    }

    fun getGeoType() {
        val marker = addedMarker ?: return
        val location = Location(marker.position.latitude, marker.position.longitude)

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        view?.showProgress()

        getGeoType.clear()
        getGeoType.execute(location, onNext = {
            post = it
            view?.hideProgress()
            view?.showGeoType(marker, PostModelMapper.transform(it))
        }, onError = {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                        view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                        view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "getGeoType", it)
                    view?.showError(it.message)
                }
            }
        })
    }

    fun setZoom(zoom: Int, animate: Boolean = false, onFinish: (() -> Unit)? = null) {
        if (animate) {
            googleMap?.animateCamera(CameraUpdateFactory.zoomTo(zoom.toFloat()),
                    object : GoogleMap.CancelableCallback {
                override fun onFinish() { onFinish?.invoke() }
                override fun onCancel() { onFinish?.invoke() }
            })
        } else {
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(zoom.toFloat()))
        }
    }

    fun addMarker(): Marker? {
        if (addedMarker != null) {
            googleMap?.cameraPosition?.target?.let {
                addedMarker?.position = LatLng(it.latitude, it.longitude)
            }
            addedMarker?.showInfoWindow()
            getGeoType()
            return addedMarker
        }

        val map = googleMap ?: throw IllegalStateException("googleMap is null")
        val latitude = map.cameraPosition?.target?.latitude
        val longitude = map.cameraPosition?.target?.longitude

        if (latitude == null || longitude == null) {
            throw IllegalStateException("latitude or longitude is null")
        }

        addedMarker = map.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(context.getString(R.string.please_move_marker))
                .snippet(context.getString(R.string.please_click_to_create_post))
                .draggable(true))

        addedMarker?.showInfoWindow()
        getGeoType()
        return addedMarker
    }

    fun getBundle(tabPosition: Int): Bundle {
        return Bundle().apply {
            val portalLevel = when (tabPosition) {
                0 -> AdminType.SUBLOCALITY2.name
                1 -> AdminType.SUBLOCALITY1.name
                2 -> AdminType.LOCALITY.name
                3 -> AdminType.COUNTRY.name
                4 -> AdminType.WORLD.name
                else -> throw IllegalArgumentException("tab position is invalid")
            }

            val latitude = addedMarker?.position?.latitude
                    ?: throw IllegalStateException("addedMarker is null")
            val longitude = addedMarker?.position?.longitude
                    ?: throw IllegalStateException("addedMarker is null")

            val createType = post?.createType
                    ?: throw IllegalStateException("Post#createType must be existed")

            val placeType = post?.place?.type?.name
                    ?: throw IllegalStateException("Post#Place#type must be existed")

            val placeId = post?.place?.id
                    ?: throw IllegalStateException("Post#Place#id must be existed")

            val placeGoogleId = post?.place?.googleId
                    ?: throw IllegalStateException("Post#Place#googldId must be existed")

            putString(MainActivity.KEY_PORTAL_LEVEL, portalLevel)
            putDouble(MainActivity.KEY_LATITUDE, latitude)
            putDouble(MainActivity.KEY_LONGITUDE, longitude)
            putString(WriteActivity.KEY_CREATE_TYPE, createType)
            putString(WriteActivity.KEY_PLACE_TYPE, placeType)
            putString(WriteActivity.KEY_PLACE_ID, placeId)
            putString(WriteActivity.KEY_PLACE_GOOGLE_ID, placeGoogleId)
        }
    }

    fun refresh(forced: Boolean = false, intent: Intent? = null) {
        if (forced) {
            getPosts(true)
        } else {
            if (intent == null)
                throw IllegalArgumentException("intent is null")

            if (intent.getBooleanExtra(WriteActivity.KEY_SUCCEED_WRITE, false)) {
                getPosts(true)
            }
        }
    }

    fun setCurrentState(intent: Intent, tabPosition: Int) {
        currentAdminType = null

        val latitude = googleMap?.cameraPosition?.target?.latitude
        val longitude = googleMap?.cameraPosition?.target?.longitude

        intent.putExtra(MainActivity.KEY_TAB_POSITION, tabPosition)
        latitude?.let { intent.putExtra(MainActivity.KEY_LATITUDE, it) }
        longitude?.let { intent.putExtra(MainActivity.KEY_LONGITUDE, it) }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(latitude: Double?, longitude: Double?, zoom: Int) {
        if (latitude != null && longitude != null) {
            setLocation(latitude, longitude, zoom)
            view?.showLocation(zoom)
        } else {
            locationProvider.lastKnownLocation.subscribe {
                setLocation(it.latitude, it.longitude, zoom)
                view?.showLocation(zoom)
            }
        }
    }

    private fun setLocation(latitude: Double, longitude: Double, zoom: Int) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude),
                zoom.toFloat()))
    }
}

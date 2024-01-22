package com.umanji.umanjiapp.ui.main.fragment.map

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.AdminType
import com.umanji.umanjiapp.common.util.getBitmap
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BaseFragment
import com.umanji.umanjiapp.ui.main.MainComponent
import com.umanji.umanjiapp.ui.main.fragment.DaggerMainFragmentComponent
import com.umanji.umanjiapp.ui.main.fragment.MainFragmentComponent
import com.umanji.umanjiapp.ui.post.PostActivity
import com.umanji.umanjiapp.ui.post.WriteActivity
import com.umanji.umanjiapp.ui.sign.SigninActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.custom_map_marker.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : BaseFragment<MainComponent, MainFragmentComponent>(),
        MapView, OnMapReadyCallback {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MapFragment::class.java.simpleName

    @Suppress("PrivatePropertyName")
    private val REQUEST_CODE_WRITE = 0

    @Inject
    lateinit var presenter: MapPresenter

    private val tab: TabLayout get() = activity.tab

    private var tabSelectedByUser: Boolean = true
    private var animateByUser: Boolean = false
    private var isTabReGenerated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        animateByUser = false
    }

    override fun initWidgets() {
        map.getMapAsync(this)

        if (tab.tabCount == 0) {
            tab.addTab(tab.newTab().setText(R.string.title_fragment_town))
            tab.addTab(tab.newTab().setText(R.string.title_fragment_district))
            tab.addTab(tab.newTab().setText(R.string.title_fragment_province))
            tab.addTab(tab.newTab().setText(R.string.title_fragment_nation))
            tab.addTab(tab.newTab().setText(R.string.title_fragment_world))

            isTabReGenerated = true
        }

        tab.clearOnTabSelectedListeners()
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "onTabSelected: tabSelectedByUser $tabSelectedByUser," +
                        "animateByUser $animateByUser")

                if (tabSelectedByUser) {
                    val zoom = when (tab.position) {
                        0 -> AdminType.getZoomLevel(AdminType.SUBLOCALITY2)
                        1 -> AdminType.getZoomLevel(AdminType.SUBLOCALITY1)
                        2 -> AdminType.getZoomLevel(AdminType.LOCALITY)
                        3 -> AdminType.getZoomLevel(AdminType.COUNTRY)
                        4 -> AdminType.getZoomLevel(AdminType.WORLD)
                        else -> throw IllegalStateException("cannot be reached")
                    }

                    animateByUser = true
                    presenter.setZoom(zoom, true) {
                        animateByUser = false
                    }

                    // we must specify zoom level explicitly
                    // other than getting from googleMap#cameraPosition
                    // because of the zoom animation delay
                    presenter.getPosts(zoom = zoom)
                }
            }
        })

        refresh.setOnClickListener {
            presenter.refresh(true)
        }

        activity.fab.let {
            (it.layoutParams as CoordinatorLayout.LayoutParams)
                    .gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

            it.setOnClickListener {
                presenter.addMarker()
            }

            if (it.visibility != View.VISIBLE) {
                it.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return

        if (requestCode == REQUEST_CODE_WRITE)
            presenter.refresh(intent = data)
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        map.onStop()
        presenter.unbindView()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val tabPosition = tab.selectedTabPosition
        presenter.setCurrentState(activity.intent, tabPosition)

        map.onDestroy()
        presenter.addedMarker = null
        presenter.googleMap = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun showProgress() {
        progress.bringToFront()
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showLocation(zoom: Int) {
        zoom_level.text = "$zoom"
        presenter.getPosts()
    }

    override fun showGeoType(marker: Marker, post: PostModel) {
        Log.d(TAG, "showGeoType: $post")
        Log.d(TAG, "marker title: ${marker.title}")
        post.let {
            marker.title = getString(R.string.question_creating_post,
                    "${it.subLocality1} ${it.subLocality2} ${it.premise}")
            marker.showInfoWindow()
        }
    }

    @SuppressLint("InflateParams")
    override fun showPosts(posts: List<PostModel>) {
        val markerView = LayoutInflater.from(context).inflate(R.layout.custom_map_marker, null)
        posts.forEach {
            markerView.post_title.text = it.content
            presenter.googleMap?.addMarker(MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(markerView.getBitmap())))
                    ?.tag = it
        }
    }

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")
        message?.let { activity.coordinator_layout.showSnackBar(it) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        presenter.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnCameraMoveListener {
            val zoom = googleMap.cameraPosition.zoom.toInt()
            zoom_level?.text = "$zoom"

            if (animateByUser)
                return@setOnCameraMoveListener

            tabSelectedByUser = false
            selectTab(zoom)
        }
        googleMap.setOnCameraIdleListener {
            tabSelectedByUser = true
            presenter.getPosts(true)
        }

        googleMap.setOnInfoWindowClickListener {
            try {
                it.tag as PostModel
            } catch (e: TypeCastException) {
                if (isLogin) {
                    startActivityForResult(
                            Intent(context, WriteActivity::class.java).apply {
                                putExtras(presenter.getBundle(tab.selectedTabPosition))
                            },
                            REQUEST_CODE_WRITE, ActivityOptions.makeCustomAnimation(context,
                            R.anim.slide_in_top, R.anim.out_back).toBundle())
                } else {
                    startActivity(Intent(context, SigninActivity::class.java))
                }
            }
        }

        googleMap.setOnMarkerClickListener {
            try {
                startActivity(Intent(context, PostActivity::class.java).apply {
                    putExtra(PostActivity.KEY_POST, it.tag as PostModel)
                }, ActivityOptions.makeCustomAnimation(context,
                        R.anim.slide_in_right, R.anim.out_back).toBundle())
            } catch (e: TypeCastException) {
                it.showInfoWindow()
            }
            true
        }

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker?) = Unit
            override fun onMarkerDrag(p0: Marker?) = Unit
            override fun onMarkerDragEnd(marker: Marker) {
                presenter.getGeoType()
            }
        })

        if (isTabReGenerated) {
            tab.getTabAt(when (AdminType.getAdminType(googleMap.cameraPosition.zoom.toInt())) {
                AdminType.SUBLOCALITY2 -> 0
                AdminType.SUBLOCALITY1 -> 1
                AdminType.LOCALITY -> 2
                AdminType.COUNTRY -> 3
                AdminType.WORLD -> 4
            })?.select()

            isTabReGenerated = false
        }
        presenter.getLocation(activity, googleMap)
    }

    private fun selectTab(zoom: Int) {
        when (AdminType.getAdminType(zoom)) {
            AdminType.SUBLOCALITY2 -> tab.getTabAt(0)?.select()
            AdminType.SUBLOCALITY1 -> tab.getTabAt(1)?.select()
            AdminType.LOCALITY -> tab.getTabAt(2)?.select()
            AdminType.COUNTRY -> tab.getTabAt(3)?.select()
            AdminType.WORLD -> tab.getTabAt(4)?.select()
        }
    }

    override fun createComponent(): MainFragmentComponent =
            DaggerMainFragmentComponent.builder()
                    .mainComponent(activityComponent)
                    .build()
}

package com.umanji.umanjiapp.ui.main.fragment.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.common.util.AdminType
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import com.umanji.umanjiapp.domain.interactor.GetPortal
import com.umanji.umanjiapp.model.mapper.PortalModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import com.umanji.umanjiapp.ui.main.MainActivity
import com.umanji.umanjiapp.ui.post.WriteActivity
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@FragmentScope
class MainFragmentPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val locationProvider: ReactiveLocationProvider,
        private val getPortal: GetPortal
) : BasePresenter<MainFragmentView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MainFragmentPresenter::class.java.simpleName

    private var view: MainFragmentView? = null

    private var latitude: Double? = null
    private var longitude: Double? = null

    private var portal: Portal? = null

    override fun bindView(view: MainFragmentView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()
        view = null

        getPortal.dispose()
    }

    fun getPortal(activity: Activity) {
        val intent: Intent = activity.intent

        val tabPosition = intent.getIntExtra(MainActivity.KEY_TAB_POSITION, 0)

        val latitude = intent.getDoubleExtra(MainActivity.KEY_LATITUDE, -1.0).let {
            when (it) {
                -1.0 -> null
                else -> it
            }
        }

        val longitude = intent.getDoubleExtra(MainActivity.KEY_LONGITUDE, -1.0).let {
            when (it) {
                -1.0 -> null
                else -> it
            }
        }

        this.latitude = latitude
        this.longitude = longitude

        Log.d(TAG, "tabPosition: $tabPosition, ($latitude, $longitude)")

        if (latitude != null && longitude != null) {
            getPortal(latitude, longitude, tabPosition)
        } else {
            RxPermissions(activity)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe {
                        if (it) {
                            getLocation(activity, tabPosition)
                        } else {
                            Log.d(TAG, "ACCESS_FINE_LOCATION denied")
                        }
                    }
        }
    }

    fun getBundle(intent: Intent, tabPosition: Int): Bundle {
        return Bundle().apply {
            val portalLevel = when (tabPosition) {
                0 -> AdminType.SUBLOCALITY2.name
                1 -> AdminType.SUBLOCALITY1.name
                2 -> AdminType.LOCALITY.name
                3 -> AdminType.COUNTRY.name
                4 -> AdminType.WORLD.name
                else -> null
            }

            val latitude = intent.getDoubleExtra(MainActivity.KEY_LATITUDE, -1.0).let {
                when (it) {
                    -1.0 -> null
                    else -> it
                }
            }

            val longitude = intent.getDoubleExtra(MainActivity.KEY_LONGITUDE, -1.0).let {
                when (it) {
                    -1.0 -> null
                    else -> it
                }
            }

            portalLevel?.let { putString(MainActivity.KEY_PORTAL_LEVEL, it) }
            latitude?.let { putDouble(MainActivity.KEY_LATITUDE, it) }
            longitude?.let { putDouble(MainActivity.KEY_LONGITUDE, it) }
        }
    }

    fun refresh(forced: Boolean = false, activity: Activity? = null, intent: Intent? = null) {
        Log.d(TAG, "refresh")
        if (forced) {
            if (activity == null)
                throw IllegalArgumentException("activity is null")

            getPortal(activity)
            view?.refreshed()
        } else {
            if (intent == null)
                throw IllegalArgumentException("intent is null")
            if (intent.getBooleanExtra(WriteActivity.KEY_SUCCEED_WRITE, false)) {
                view?.refreshed()
            }
        }
    }

    fun setCurrentState(activity: Activity, tabPosition: Int) {
        val intent: Intent = activity.intent
        intent.putExtra(MainActivity.KEY_TAB_POSITION, tabPosition)
        latitude?.let { intent.putExtra(MainActivity.KEY_LATITUDE, it) }
        longitude?.let { intent.putExtra(MainActivity.KEY_LONGITUDE, it) }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(activity: Activity, tabPosition: Int) {
        locationProvider.lastKnownLocation.subscribe {
            latitude = it.latitude
            longitude = it.longitude
            setCurrentState(activity, tabPosition)
            getPortal(it.latitude, it.longitude, tabPosition)
        }
    }

    private fun getPortal(latitude: Double, longitude: Double, tabPosition: Int) {
        Log.d(TAG, "getPortal")
        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        view?.showProgress()

        getPortal.clear()
        getPortal.execute(GetPortal.Params(location = Location(latitude, longitude)), {
            Log.d(TAG, "portal == it: ${portal == it}")
            view?.hideProgress()
            if (portal != null && portal != it) {
                view?.refreshed(PortalModelMapper.transform(it))
            } else {
                view?.showPortal(PortalModelMapper.transform(it), tabPosition)
            }
            portal = it
        }, {
            view?.hideProgress()
            Log.d(TAG, "getPortal", it)
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> view?.showError(it.message)
            }
        })
    }
}

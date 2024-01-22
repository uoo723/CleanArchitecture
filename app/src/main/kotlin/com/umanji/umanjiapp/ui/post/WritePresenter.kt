package com.umanji.umanjiapp.ui.post

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.marchinram.rxgallery.RxGallery
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.AdminType
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.domain.entity.*
import com.umanji.umanjiapp.domain.interactor.CreatePost
import com.umanji.umanjiapp.domain.interactor.GetMe
import com.umanji.umanjiapp.model.mapper.UserModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import com.umanji.umanjiapp.ui.main.MainActivity
import io.reactivex.Observable
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@ActivityScope
class WritePresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val getMe: GetMe,
        private val createPost: CreatePost
) : BasePresenter<WriteView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = WritePresenter::class.java.simpleName

    private var view: WriteView? = null

    private var user: User? = null

    private var uri: Uri? = null

    override fun bindView(view: WriteView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        getMe.dispose()
        createPost.dispose()
    }

    fun getMe() {
        getMe.clear()
        getMe.execute(null, onNext = {
            user = it
            view?.showMe(UserModelMapper.transform(it))
        }, onError = { Log.d(TAG, "getMe(): unexpected", it) })
    }

    fun createPost(isLogin: Boolean, intent: Intent, content: String) {
        if (content.isBlank()) {
            view?.showError(context.getString(R.string.required_content))
            return
        }

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        val user = user

        if (!isLogin || user == null) {
            view?.showError(context.getString(R.string.required_login))
            return
        }

        val portalLevel = intent.getStringExtra(MainActivity.KEY_PORTAL_LEVEL)
                ?: AdminType.SUBLOCALITY2.name
        val latitude = intent.getDoubleExtra(MainActivity.KEY_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(MainActivity.KEY_LONGITUDE, 0.0)
        val countryCode = intent.getStringExtra(MainActivity.KEY_COUNTRY_CODE)
                ?: getCurrentCountryCode()
        val createType = intent.getStringExtra(WriteActivity.KEY_CREATE_TYPE) ?: "newPortal"
        val placeType = intent.getStringExtra(WriteActivity.KEY_PLACE_TYPE) ?: ""
        val placeId = intent.getStringExtra(WriteActivity.KEY_PLACE_ID) ?: ""
        val placeGoogleId = intent.getStringExtra(WriteActivity.KEY_PLACE_GOOGLE_ID) ?: ""

        val adminType = AdminType.valueOf(portalLevel)
        val viewLevel = AdminType.getZoomLevel(adminType)
        val placeTypeEnum = if (placeType.isNotBlank()) {
            PlaceType.valueOf(placeType)
        } else {
            PlaceType.SPACE
        }

        Log.d(TAG, "uri: $uri")
        val post = Post(
                content = content,
                user = user,
                createType = createType,
                portalLevel = adminType.value,
                adminDistrict = AdminDistrict(countryCode = countryCode),
                viewLevel = viewLevel,
                place = Place(
                        id = placeId,
                        googleId = placeGoogleId,
                        type = placeTypeEnum,
                        location = Location(latitude, longitude)
                ),
                images = uri?.let { listOf(Image(uri = it.toString())) } ?: listOf()
        )

        view?.showProgress()
        createPost.clear()
        createPost.execute(post, onComplete = {
            view?.hideProgress()
            view?.succeedCreatePost()
        }, onError = {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "createPost", it)
                    view?.showError(it.message)
                }
            }
        })
    }

    fun takeCamera(activity: Activity) {
        val dir = File(activity.externalCacheDir, "camera")
        dir.mkdirs()
        val file: File = File.createTempFile("IMG_", ".jpg", dir)
        uri = Uri.fromFile(file)

        RxPermissions(activity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .flatMap {
                    if (!it) {
                        Observable.empty<Uri>()
                    } else {
                        RxGallery.photoCapture(activity, uri).toObservable()
                    }
                }
                .subscribe({
                    view?.showPhoto(it, true)
                    Log.d(TAG, "path: ${it.path}")
                }, {
                    Log.d(TAG, "takeCamera", it)
                    view?.showError(it.message)
                })
    }

    fun takeGallery(activity: Activity) {
        RxPermissions(activity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .flatMap {
                    if (!it) {
                        Observable.empty<List<Uri>>()
                    } else {
                        RxGallery.gallery(activity, false,
                                RxGallery.MimeType.IMAGE).toObservable()
                    }
                }
                .subscribe({
                    uri = it[0]
                    view?.showPhoto(it[0], false)
                }, {
                    Log.d(TAG, "takeGallery", it)
                    view?.showError(it.message)
                })
    }

    fun clearPhoto() {
        uri = null
        view?.showClearPhoto()
    }

    @Suppress("unused")
    fun deleteFile(uri: Uri) {
        Log.d(TAG, "path: ${uri.path}")
        Log.d(TAG, "deleted: ${File(uri.path).delete()}")
    }

    @Suppress("DEPRECATION")
    private fun getCurrentCountryCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].country
        } else {
            context.resources.configuration.locale.country
        }
    }
}

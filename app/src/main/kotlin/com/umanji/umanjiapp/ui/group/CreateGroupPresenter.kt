package com.umanji.umanjiapp.ui.group

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
import com.umanji.umanjiapp.domain.interactor.CreateGroup
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
class CreateGroupPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val getMe: GetMe,
        private val createGroup: CreateGroup
) : BasePresenter<CreateGroupView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = CreateGroupPresenter::class.java.simpleName

    private var view: CreateGroupView? = null

    private var user: User? = null

    private var uri: Uri? = null

    override fun bindView(view: CreateGroupView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        getMe.dispose()
        createGroup.dispose()
    }

    fun getMe() {
        getMe.clear()
        getMe.execute(null, onNext = {
            user = it
            view?.showMe(UserModelMapper.transform(it))
        }, onError = { Log.d(TAG, "getMe(): unexpected", it) })
    }

    fun createGroup(isLogin: Boolean, intent: Intent, groupName: String) {

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        val user = user

        if (!isLogin || user == null) {
            view?.showError(context.getString(R.string.required_login))
            return
        }

        val groupName = "test group name"

        Log.d(TAG, "uri: $uri")
        val group = Group(
                group_name = groupName

        )

        view?.showProgress()
        createGroup.clear()
        createGroup.execute(group, onComplete = {
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
                    Log.d(TAG, "createGroup", it)
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

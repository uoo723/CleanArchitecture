package com.umanji.umanjiapp.ui.info

import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.BasePresenter
import javax.inject.Inject


@ActivityScope
class InfoPresenter @Inject constructor(

): BasePresenter<InfoView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = InfoPresenter::class.java.simpleName

    private var view: InfoView? = null

    override fun bindView(view: InfoView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
    }
}

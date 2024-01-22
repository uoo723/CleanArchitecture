package com.umanji.umanjiapp.ui

/**
 * Interface that represents Presenter in MVP.
 */
interface BasePresenter<in T : BaseView> {
    fun bindView(view: T)
    fun unbindView()
}

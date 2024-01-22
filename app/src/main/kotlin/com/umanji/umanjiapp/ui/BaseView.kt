package com.umanji.umanjiapp.ui

/**
 * Interface that represents View in MVP.
 */
interface BaseView {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String? = null)
}

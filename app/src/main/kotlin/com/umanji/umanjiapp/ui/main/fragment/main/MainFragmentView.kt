package com.umanji.umanjiapp.ui.main.fragment.main

import com.umanji.umanjiapp.model.PortalModel
import com.umanji.umanjiapp.ui.BaseView


interface MainFragmentView : BaseView {
    fun showPortal(portal: PortalModel, tabPosition: Int)
    fun refreshed(portal: PortalModel? = null)
}

package com.umanji.umanjiapp.ui.main.fragment.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.broadcastreceiver.NetworkChangeReceiver
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.domain.entity.PoliticalType
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.model.PortalModel
import com.umanji.umanjiapp.ui.BaseFragment
import com.umanji.umanjiapp.ui.fragment.post.GroupListFragment
import com.umanji.umanjiapp.ui.fragment.post.PostListFragment
import com.umanji.umanjiapp.ui.main.MainComponent
import com.umanji.umanjiapp.ui.main.fragment.DaggerMainFragmentComponent
import com.umanji.umanjiapp.ui.main.fragment.MainFragmentComponent
import com.umanji.umanjiapp.ui.main.fragment.main.adapter.MainPagerAdapter
import com.umanji.umanjiapp.ui.post.WriteActivity
import com.umanji.umanjiapp.ui.sign.SigninActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : BaseFragment<MainComponent, MainFragmentComponent>(), MainFragmentView {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MainFragment::class.java.simpleName

    @Inject lateinit var presenter: MainFragmentPresenter

    private val tab: TabLayout get() = activity.tab

    @Suppress("PrivatePropertyName")
    private val REQUEST_CODE_WRITE = 0

    private val pagerAdapter: MainPagerAdapter
            by lazy { MainPagerAdapter(childFragmentManager, context) }

    private val networkChangeReceiver = NetworkChangeReceiver()
    private var first: Boolean = true

    private var headerTexts: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        networkChangeReceiver.onChanged = onChanged@ {
            Log.d(TAG, "network state is changed: $it")
            if (first) {
                first = false
                return@onChanged
            }

            if (it) {
                presenter.refresh(true, activity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        context.registerReceiver(networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        first = true
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
        context.unregisterReceiver(networkChangeReceiver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getPortal(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.setCurrentState(activity, tab.selectedTabPosition)
    }

    override fun initWidgets() {
        viewpager.adapter = pagerAdapter

        tab.setupWithViewPager(viewpager)
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabSelected(tab: TabLayout.Tab) {
                headerTexts?.let { header.text = it[tab.position] }
            }
        })

        activity.fab.setOnClickListener {
            if (isLogin) {
                startActivityForResult(
                        Intent(context, WriteActivity::class.java).apply {
                            putExtras(presenter.getBundle(activity.intent, tab.selectedTabPosition))
                        },
                        REQUEST_CODE_WRITE, ActivityOptions.makeCustomAnimation(context,
                        R.anim.slide_in_top, R.anim.out_back).toBundle())
            } else {
                startActivity(Intent(context, SigninActivity::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        if (resultCode != Activity.RESULT_OK || data == null)
            return

        if (requestCode == REQUEST_CODE_WRITE) {
            presenter.bindView(this)
            presenter.refresh(intent = data)
        }
    }

    override fun showProgress() = Unit
    override fun hideProgress() = Unit

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")
        message?.let { activity.coordinator_layout.showSnackBar(it) }
    }

    override fun showPortal(portal: PortalModel, tabPosition: Int) {
        Log.d(TAG, "portal: $portal")
        header.text = getString(R.string.portal, portal.portalName)

        val portalTypes = PoliticalType.values().reversed()
        val portalNames = arrayOf(portal.subLocality2, portal.subLocality1,
                portal.locality, portal.country, "세계").map { getString(R.string.portal, it) }

        headerTexts = portalNames
        headerTexts?.let { header.text = it[tab.selectedTabPosition] }

        (0 until pagerAdapter.count)
                .map {
                    Triple(pagerAdapter.getItem(it) as PostListFragment, portalNames[it],
                            portalTypes[it])
                }
                .forEach {
                    it.first.params = GetPosts.Params1(portalName = it.second,
                            portalType = it.third.value)
                }

        tab.getTabAt(tabPosition)?.select()
    }

    override fun refreshed(portal: PortalModel?) {
        Log.d(TAG, "refreshed")
        portal?.let { header.text = getString(R.string.portal, it.portalName) }

        val portalTypes = PoliticalType.values().reversed()
        val portalNames = arrayOf(portal?.subLocality2, portal?.subLocality1,
                portal?.locality, portal?.country, "세계").map { getString(R.string.portal, it) }

        if (portal != null) {
            headerTexts = portalNames
            headerTexts?.let { header.text = it[tab.selectedTabPosition] }
        }

        (0 until pagerAdapter.count)
                .map {
                    Triple(pagerAdapter.getItem(it) as PostListFragment, portalNames[it],
                            portalTypes[it])
                }
                .forEach {
                    if (portal != null) {
                        it.first.refresh(GetPosts.Params1(portalName = it.second,
                                portalType = it.third.value))
                    } else {
                        it.first.refresh()
                    }
                }
    }

    override fun createComponent(): MainFragmentComponent =
            DaggerMainFragmentComponent.builder()
                    .mainComponent(activityComponent)
                    .build()
}

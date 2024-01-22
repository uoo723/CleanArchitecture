package com.umanji.umanjiapp.ui.info

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.umanji.umanjiapp.HasComponent
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.ui.BaseActivity
import com.umanji.umanjiapp.ui.fragment.post.GroupListFragment
import com.umanji.umanjiapp.ui.fragment.post.PostListFragment
import com.umanji.umanjiapp.ui.fragment.setting.SettingFragment
import com.umanji.umanjiapp.ui.info.adapter.PlacePagerAdapter
import kotlinx.android.synthetic.main.activity_info.*
import javax.inject.Inject

class InfoActivity : BaseActivity<InfoComponent>(), HasComponent<InfoComponent>, InfoView {

    enum class InfoType(val value: String) {
        PORTAL("portal"),
        PLACE("place"),
        PROFILE("profile"),
        BUILDING("building")
    }

    companion object {
        val KEY_INFO_TITLE = "info_title".getBundleKey()
        val KEY_INFO_TYPE = "info_type".getBundleKey()
        val KEY_INFO_ID = "info_id".getBundleKey()
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = InfoActivity::class.java.simpleName

    @Inject
    lateinit var presenter: InfoPresenter

    private val pagerAdapter by lazy { PlacePagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return if (item.itemId == android.R.id.home) {
//            finish()
//            true
//        } else super.onOptionsItemSelected(item)
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        }  else if(id == R.id.action_info){
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.in_front, R.anim.slide_out_right)
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        if (pagerAdapter.fragments.isEmpty()) {
            val infoType = intent.getSerializableExtra(KEY_INFO_TYPE) as? InfoType
            val infoId = intent.getStringExtra(KEY_INFO_ID)
            val infoTitle = intent.getStringExtra(KEY_INFO_TITLE)

            if (infoType != null && infoId != null && infoTitle != null) {
                Log.d(TAG, "infoType: $infoType, infoId: $infoId")
                collapsing_toolbar.title = infoTitle

                pagerAdapter.fragments.add(Pair(getString(R.string.title_post), PostListFragment()))

                when (infoType) {
                    InfoType.PORTAL, InfoType.PLACE -> {
                        val params = GetPosts.Params3(type = infoType.value, id = infoId)
                        (pagerAdapter.fragments[0].second as PostListFragment).params = params

                        pagerAdapter.fragments.add(Pair(getString(R.string.title_community),
                                GroupListFragment()))
                        pagerAdapter.fragments.add(Pair(getString(R.string.title_people),
                                PostListFragment()))
                    }
                    InfoType.PROFILE -> {
                        val params = GetPosts.Params4(userId = infoId)
                        (pagerAdapter.fragments[0].second as PostListFragment).params = params

                        pagerAdapter.fragments.add(Pair(getString(R.string.title_property),
                                PostListFragment()))
                    }
                }

                when (infoType) {
                    InfoType.PORTAL -> {
                        pagerAdapter.fragments.add(1, Pair(getString(R.string.title_popular_place),
                                PostListFragment()))
                    }
                    InfoType.PLACE -> {
                        pagerAdapter.fragments.add(Pair(getString(R.string.title_detail_info),
                                SettingFragment()))
                    }
                    else -> Unit
                }
            }
        }
        viewpager.adapter = pagerAdapter
        tab.setupWithViewPager(viewpager)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String?) {
    }

    override fun createComponent(): InfoComponent =
            DaggerInfoComponent.builder()
                    .appComponent(appComponent)
                    .build()
}

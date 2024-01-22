package com.umanji.umanjiapp.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.umanji.umanjiapp.HasComponent
import com.umanji.umanjiapp.MyApplication
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.broadcastreceiver.LoginStateChangeReceiver
import com.umanji.umanjiapp.broadcastreceiver.NetworkChangeReceiver
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseActivity
import com.umanji.umanjiapp.ui.info.InfoActivity
import com.umanji.umanjiapp.ui.main.fragment.main.MainFragment
import com.umanji.umanjiapp.ui.main.fragment.map.MapFragment
import com.umanji.umanjiapp.ui.post.PostActivity
import com.umanji.umanjiapp.ui.profile.EditActivity
import com.umanji.umanjiapp.ui.sign.SigninActivity
import com.umanji.umanjiapp.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject


class MainActivity : BaseActivity<MainComponent>(), HasComponent<MainComponent>,
        MainView, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val KEY_LATITUDE = "latitude".getBundleKey()
        val KEY_LONGITUDE = "longitude".getBundleKey()
        val KEY_TAB_POSITION = "tab_position".getBundleKey()
        val KEY_PORTAL_LEVEL = "portal_level".getBundleKey()
        val KEY_COUNTRY_CODE = "country_code".getBundleKey()
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = MainActivity::class.java.simpleName

    @Inject lateinit var presenter: MainPresenter

    private val navView: NavigationView get() = nav_view
    private val drawerLayout: DrawerLayout get() = drawer_layout

    private var menu: Menu? = null
    private var userId: String? = null
    private var userName: String? = null

    private val mainFragment = MainFragment()
    private val mapFragment = MapFragment()

    private val loginStateChangeReceiver = LoginStateChangeReceiver()
    private val networkChangeReceiver = NetworkChangeReceiver()

    private var first: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit()
        }

        loginStateChangeReceiver.onChanged = {
            presenter.getMe(it)
        }

        networkChangeReceiver.onChanged = onChanged@ {
            if (first) {
                first = false
                return@onChanged
            }

            if (it) {
                (application as MyApplication).checkLogin()
            }
        }

        first = true
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        toolbar.setTitle(R.string.app_name)

        login.setOnClickListener {
            if (isLogin) {
                presenter.logout()
            } else {
                startActivity(Intent(this, SigninActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        presenter.getMe(isLogin)
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(loginStateChangeReceiver,
                        IntentFilter(LoginStateChangeReceiver.ACTION))
        registerReceiver(networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(loginStateChangeReceiver)
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val drawer: DrawerLayout = drawer_layout

        when (id) {
            R.id.navigation_item_home -> drawer.closeDrawer(GravityCompat.START)
            R.id.navigation_item_post -> {
                startActivity(Intent(this, InfoActivity::class.java).apply {
                    putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PROFILE)
                    putExtra(InfoActivity.KEY_INFO_ID, userId)
                    putExtra(InfoActivity.KEY_INFO_TITLE, userName)
                }, ActivityOptions.makeCustomAnimation(this,
                        R.anim.slide_in_right, R.anim.slide_out_left).toBundle())
            }
            R.id.navigation_item_wallet -> {
                startActivity(Intent(this, WalletActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.navigation_item_profile -> {
                startActivity(Intent(this, EditActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.navigation_item_setting -> {
                startActivity(Intent(this, InfoActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) ->
                drawer_layout.closeDrawer(GravityCompat.START)
            supportFragmentManager.findFragmentById(R.id.container) is MapFragment -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle -> {
                flipCard()
                updateMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        updateMenu()
        return true
    }

    private fun updateMenu() {
        when (presenter.viewState) {
            MainPresenter.ViewState.MAP ->
                menu?.findItem(R.id.action_toggle)?.setIcon(R.drawable.ic_forum)
            MainPresenter.ViewState.MAIN ->
                menu?.findItem(R.id.action_toggle)?.setIcon(R.drawable.ic_map_action)
        }
    }

    override fun showProgress() = Unit
    override fun hideProgress() = Unit

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")
        message?.let { coordinator_layout.showSnackBar(it) }
    }

    override fun showLoginState(user: UserModel) {
        Log.d(TAG, "showMe: $user")
        navView.getHeaderView(0).tv_name.text = user.userName
        navView.getHeaderView(0).tv_email.text = user.email
        userId = user.id
        userName = user.userName

        if (user.images.isNotEmpty()) {
            Glide.with(this)
                    .load(user.images[0])
                    .into(navView.getHeaderView(0).iv_navi_profile)
        }

        navView.menu.clear()
        navView.inflateMenu(R.menu.main_drawer_login)
        login.text = getString(R.string.logout)
    }

    override fun showLogoutState() {
        isLogin = false
        navView.getHeaderView(0).tv_name.text = "손님"
        navView.getHeaderView(0).tv_email.text = "로그인을 해주세요."
        navView.menu.clear()
        navView.inflateMenu(R.menu.main_drawer_logout)
        login.text = getString(R.string.login)
    }

    private fun flipCard() {
        TransitionManager.beginDelayedTransition(coordinator_layout, ChangeBounds())
        if (presenter.viewState == MainPresenter.ViewState.MAP) {
            supportFragmentManager.popBackStack()
            (fab.layoutParams as CoordinatorLayout.LayoutParams)
                    .gravity = Gravity.BOTTOM or Gravity.END
        } else {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out
                    )
                    .replace(R.id.container, mapFragment)
                    .addToBackStack(null)
                    .commit()
            (fab.layoutParams as CoordinatorLayout.LayoutParams)
                    .gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        }
        presenter.changeViewState()
    }

    override fun createComponent(): MainComponent =
            DaggerMainComponent.builder()
                    .appComponent(appComponent)
                    .build()
}

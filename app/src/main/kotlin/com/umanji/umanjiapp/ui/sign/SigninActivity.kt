package com.umanji.umanjiapp.ui.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.toolbar_progress.*
import javax.inject.Inject


class SigninActivity : BaseActivity<SignComponent>(), SignInView {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = SigninActivity::class.java.simpleName

    @Inject lateinit var presenter: SignInPresenter

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

    override fun initWidgets() {
        setContentView(R.layout.activity_signin)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.setTitle(R.string.activity_signin)

        signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        login.setOnClickListener {
            presenter.signIn(email.text.toString(), password.text.toString())
        }

        email.addTextChangedListener(FormTextWatcher(email))
        password.addTextChangedListener(FormTextWatcher(password))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun showProgress() {
        progress.bringToFront()
        progress.show()
    }

    override fun hideProgress() {
        progress.hide()
    }

    override fun showError(message: String?) {
        message?.let { constraint_layout.showSnackBar(it) }
    }

    override fun showError(message: String, type: SignInView.FormType) {
        when (type) {
            SignInView.FormType.EMAIL -> {
                email_layout.error = message
                requestFocus(email)
            }
            SignInView.FormType.PASSWORD -> {
                password_layout.error = message
                requestFocus(password)
            }
        }
    }

    override fun passValidate(type: SignInView.FormType) {
        when (type) {
            SignInView.FormType.EMAIL ->
                    email_layout.isErrorEnabled = false
            SignInView.FormType.PASSWORD ->
                    password_layout.isErrorEnabled = false
        }
    }

    // 로그인 성공했을 때
    override fun succeedLogin() {
        isLogin = true
        finish()
    }

    private fun requestFocus(view: View) {
        view.requestFocus()
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun createComponent(): SignComponent =
            DaggerSignComponent.builder()
                    .appComponent(appComponent)
                    .build()

    inner class FormTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable) {
            when (view.id) {
                R.id.email -> presenter.validate(s.toString(), SignInView.FormType.EMAIL)
                R.id.password -> presenter.validate(s.toString(), SignInView.FormType.PASSWORD)
            }
        }
    }
}

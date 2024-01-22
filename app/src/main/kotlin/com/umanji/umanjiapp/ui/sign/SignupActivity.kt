package com.umanji.umanjiapp.ui.sign

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.toolbar_progress.*
import javax.inject.Inject


class SignupActivity : BaseActivity<SignComponent>(), SignUpView {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = SignupActivity::class.java.simpleName

    @Inject
    lateinit var presenter: SignUpPresenter
    private val scrollview: ScrollView get() = sv_signup

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
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        scrollview.isVerticalScrollBarEnabled = false

        toolbar.setTitle(R.string.activity_signup)

        signup.setOnClickListener {
            presenter.signUp(
                    user_name.text.toString(),
                    email.text.toString(),
                    phone.text.toString(),
                    password.text.toString(),
                    password_confirm.text.toString()
            )
        }

        user_name.addTextChangedListener(FormTextWatcher(user_name))
        email.addTextChangedListener(FormTextWatcher(email))
        phone.addTextChangedListener(FormTextWatcher(phone))
        password.addTextChangedListener(FormTextWatcher(password))
        password_confirm.addTextChangedListener(FormTextWatcher(password_confirm))
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

    override fun showError(message: String, type: SignUpView.FormType) {
        when (type) {
            SignUpView.FormType.USER_NAME -> {
                user_name_layout.error = message
                requestFocus(user_name)
            }
            SignUpView.FormType.EMAIL -> {
                email_layout.error = message
                requestFocus(email)
            }
            SignUpView.FormType.PHONE -> {
                phone_layout.error = message
                requestFocus(phone)
            }
            SignUpView.FormType.PASSWORD -> {
                password_layout.error = message
                requestFocus(password)
            }
            SignUpView.FormType.PASSWORD_CONFIRM -> {
                password_confirm_layout.error = message
                requestFocus(password_confirm)
            }
        }
    }

    override fun passValidate(type: SignUpView.FormType) {
        when (type) {
            SignUpView.FormType.USER_NAME ->
                user_name_layout.isErrorEnabled = false
            SignUpView.FormType.EMAIL ->
                email_layout.isErrorEnabled = false
            SignUpView.FormType.PHONE ->
                phone_layout.isErrorEnabled = false
            SignUpView.FormType.PASSWORD ->
                password_layout.isErrorEnabled = false
            SignUpView.FormType.PASSWORD_CONFIRM ->
                password_confirm_layout.isErrorEnabled = false
        }
    }

    override fun succeedSignup() {
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
                R.id.user_name -> presenter.validate(s.toString(), SignUpView.FormType.USER_NAME)
                R.id.email -> presenter.validate(s.toString(), SignUpView.FormType.EMAIL)
                R.id.phone -> presenter.validate(s.toString(), SignUpView.FormType.PHONE)
                R.id.password -> presenter.validate(s.toString(), SignUpView.FormType.PASSWORD)
                R.id.password_confirm ->
                    presenter.validate(s.toString(), SignUpView.FormType.PASSWORD_CONFIRM)
            }
        }
    }
}

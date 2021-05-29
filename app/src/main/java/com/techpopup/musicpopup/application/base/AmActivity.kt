package com.techpopup.musicpopup.application.base

import android.content.Context
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.techpopup.musicpopup.R
import com.techpopup.musicpopup.util.common.AmLogs
import com.techpopup.musicpopup.util.common.AmToast
import com.techpopup.musicpopup.util.network.AmConnectionInterface
import com.techpopup.musicpopup.util.network.AmConnectionReceiver
import kotlinx.android.synthetic.main.component_toolbar.*

abstract class AmActivity (@LayoutRes var layout: Int, var title: Int? = null, var back: Boolean = false) : AppCompatActivity(),
    AmConnectionInterface, LifecycleOwner {

    private var connectionReceiver: AmConnectionReceiver? = null
    private var connectionRegistered: Boolean = false

    override fun onStop() {
        super.onStop()
        destroyConnection()
    }

    override fun onStart() {
        super.onStart()
        initConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout)

        data()
        view()
        listener()
        observer(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /*if (requestCode == LOCATION_PERMISSION_REQUEST) {
            locationInit()
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    /**
     * Observe View Model
     */
    protected open fun observer(activity: AmActivity) {}

    /**
     * Adding action or listener to any object
     */
    protected open fun listener() {}

    /**
     * Default constructor for AmActivity. to define onCreate
     */
    protected open fun view() {
        toolbar()
    }

    /**
     * Handling intent data
     */
    protected open fun data() {

    }

    private fun toolbar() {
        toolBar?.apply {
            setSupportActionBar(this)

            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(back)
                setHomeAsUpIndicator(ContextCompat.getDrawable(this@AmActivity, R.drawable.ic_back))
                labelTitle?.text = this@AmActivity.title?.let { id ->
                    this@AmActivity.getString(id)
                } ?: run {
                    title.toString()
                }
            }
        }
    }

    private fun initConnection(receiver: AmConnectionInterface) {
        if (connectionReceiver == null) {
            connectionReceiver = AmConnectionReceiver()
            connectionReceiver?.let {
                val mIntentFilter = IntentFilter()

                it.registerReceiver(receiver)
                mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")

                registerReceiver(connectionReceiver, mIntentFilter)
                connectionRegistered = true
            }

        }
    }

    private fun destroyConnection() {
        if (connectionRegistered) {
            unregisterReceiver(connectionReceiver)
            connectionRegistered = false
        }
    }

    /**
     * On connection change information
     */
    override fun onConnectionChange(message: String) {
        AmLogs.i(message)
    }

    /**
     * Showing toast message
     */
    fun message(message: String) {
        if (message.isNotBlank()) {
            AmToast(this, message).show()
        }
    }

    /**
     * Auto dismiss keyboard on touch view
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}
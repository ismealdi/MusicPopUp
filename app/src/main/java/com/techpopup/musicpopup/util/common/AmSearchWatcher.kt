package com.techpopup.musicpopup.util.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*

//TODO: string default value, please provide unit test
class AmSearchWatcher(
    val searchText: EditText? = null,
    val load: ((String) -> Unit)? = null,
    val afterTextChanged: ((Editable?) -> Unit)? = null,
    val empty: (() -> Unit)? = null
) : TextWatcher {

    private var timer: Timer? = null

    override fun afterTextChanged(edit: Editable?) {
        afterTextChanged?.invoke(edit)

        search()
    }

    private fun search(){
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                try {
                    val query = searchText?.text.toString().trim()
                    val length = query.length

                    if (length > 3) {
                        load?.invoke(query)
                    } else {
                        empty?.invoke()
                    }
                }catch (e: IndexOutOfBoundsException){
                    e.printStackTrace()
                }
            }
        }, 30000)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        timer?.cancel()
    }

}
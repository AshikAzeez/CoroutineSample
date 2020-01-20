package com.mobiotics.kotlincoroutinesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.btn_run
import kotlinx.android.synthetic.main.activity_main.txt_data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Scope -> group together
        //IO,Main,Default
        var count = 1
        btn_run?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest()
            }

            count++
        }
    }

    private suspend fun fakeApiRequest() {
        //Wait for next excution
        val result1 = getResultFromApi1()
        setTextMainThread(result1)
        var result2 = getResultFromApi2()
        setTextMainThread(result2)
    }

    private fun setText(input: String) {
        val textData = txt_data.text.toString() + "\n$input"
        txt_data.text = textData
    }

    private suspend fun setTextMainThread(input: String) {
        //Change to main thread
        withContext(Dispatchers.Main) {
            setText(input)
        }
    }
    /*
    * launch{} does not return
    * async{}returns an instance of Deferred<T>
    * */

    private suspend fun getResultFromApi1(): String {

        //Delay current coroutine
        delay(1000)
        logThread("getResultFromAPI")
        return "Result#1"
    }

    private suspend fun getResultFromApi2(): String {
        delay(2000)
        return "RESULT#2"
    }

    private fun logThread(methodName: String) {
        println("DEBUG_ MethodName:${methodName} ThreadName${Thread.currentThread().name})")
    }
}

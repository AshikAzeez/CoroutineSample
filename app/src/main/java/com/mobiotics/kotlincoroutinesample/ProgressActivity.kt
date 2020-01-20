package com.mobiotics.kotlincoroutinesample

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_progress.btn_start_job
import kotlinx.android.synthetic.main.activity_progress.progress_horizontal
import kotlinx.android.synthetic.main.activity_progress.txt_job_complete
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

class ProgressActivity : AppCompatActivity() {

    companion object {
        private const val PROGRESS_MAX = 100
        private const val PROGRESS_START = 0
        private const val JOB_TIME = 4000 //ms
        private val TAG = "ProgressActivity"
    }

    lateinit var job: CompletableJob
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        btn_start_job.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            progress_horizontal.startJobOrCancel(job)
        }
    }

    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            //Job already started
            Log.i(TAG, "startJobOrCancel: Job already started cancelling...")
            resetJob()
        } else {
            GlobalScope.launch(Main) {
                btn_start_job.text = "Cancel Job#1"
            }

            CoroutineScope(IO + job).launch {
                Log.i(TAG, "startJobOrCancel: Coroutine $this activated with job =$job")
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateTextView("\"Job complete\"")
            }
        }
    }

    private fun updateTextView(text: String) {
        GlobalScope.launch(Main) {
            txt_job_complete.text = text
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting Job..."))
        }
        initJob()
    }

    private fun initJob() {
        btn_start_job.text = "Start Job"
        updateTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error occured"
                } else {
                    Log.i(TAG, "initJob: Reason $msg")
                    toast(msg)
                }
            }
        }
        progress_horizontal.max = PROGRESS_MAX
        progress_horizontal.progress = PROGRESS_START
    }

    private fun toast(message: String? = "") {
        GlobalScope.launch(Main) {
            Toast.makeText(this@ProgressActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

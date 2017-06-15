package com.xhbb.qinzl.newsest.async;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.xhbb.qinzl.newsest.R;

import java.io.IOException;

public class DownloadTheLatestApkService extends IntentService {

    public static Intent newIntent(Context context) {
        return new Intent(context, DownloadTheLatestApkService.class);
    }

    public DownloadTheLatestApkService() {
        super("DownloadTheLatestApkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            MainTasks.downloadAndSetupApk(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.upgrade_failed_because_network_error_toast, Toast.LENGTH_SHORT).show();
        }
    }
}

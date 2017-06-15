package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.xhbb.qinzl.newsest.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by qinzl on 2017/6/15.
 */

public class FileUtils {

    public static boolean isExternalMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getExternalAvailableSize() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        StatFs statFs = new StatFs(path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getAvailableBytes();
        } else {
            long sizeOfOneBlock = statFs.getBlockSizeLong();
            @SuppressWarnings("deprecation")
            int blockCount = statFs.getAvailableBlocks();

            return blockCount * sizeOfOneBlock;
        }
    }

    public static File getTheLatestApkFile(Context context) throws IOException {
        File dir = context.getExternalCacheDir();
        String fileName = context.getString(R.string.apk_file_name);

        File file = new File(dir, fileName);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException();
            }
        }

        if (!file.createNewFile()) {
            throw new IOException();
        }

        return file;
    }
}

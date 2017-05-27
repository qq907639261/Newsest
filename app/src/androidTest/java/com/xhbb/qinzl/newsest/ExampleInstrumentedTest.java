package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        String newsResponse = NetworkUtils.getNewsResponse(appContext, "社会", 1);
        ContentValues[] newsValuesArray = JsonUtils.getNewsValuesArray(newsResponse);

        assertEquals("com.xhbb.qinzl.newsest", appContext.getPackageName());
    }
}

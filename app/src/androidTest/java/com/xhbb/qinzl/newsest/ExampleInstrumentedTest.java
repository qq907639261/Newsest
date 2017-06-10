package com.xhbb.qinzl.newsest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

//        new DbHelper(appContext).getWritableDatabase().execSQL("CREATE TABLE " + Contract.CommentEntry.TABLE_NAME + " ( " +
//                Contract.CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                Contract.CommentEntry._COMMENT_DATE + " LONG, " +
//                Contract.CommentEntry._COMMENT_CONTENT + " TEXT, " +
//                Contract.CommentEntry._NEWS_CODE + " TEXT " +
//                ")");

        assertEquals("com.xhbb.qinzl.newsest", appContext.getPackageName());
    }
}

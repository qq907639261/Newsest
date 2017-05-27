package com.xhbb.qinzl.newsest.server;

import android.content.ContentValues;
import android.content.Context;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.data.PreferencesUtils;
import com.xhbb.qinzl.newsest.server.JsonUtils.JsonNews.ShowApiResBodyObject.PageBeanObject.ContentListArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinzl on 2017/5/27.
 */

public class JsonUtils {

    public static List<ContentValues> getNewsValuesList(Context context, String jsonString)
            throws JSONException {
        JSONObject jsonNews = new JSONObject(jsonString);
        JSONObject showApiResBodyObject = jsonNews.getJSONObject(JsonNews.SHOW_API_RES_BODY);
        JSONObject pageBeanObject = showApiResBodyObject.getJSONObject(JsonNews.ShowApiResBodyObject.PAGE_BEAN);

        int totalPages = pageBeanObject.getInt(JsonNews.ShowApiResBodyObject.PageBeanObject.ALL_PAGES);
        PreferencesUtils.saveNewsTotalPages(context, totalPages);

        JSONArray contentListArray = pageBeanObject.getJSONArray(JsonNews.ShowApiResBodyObject.PageBeanObject.CONTENT_LIST);

        List<ContentValues> newsValuesList = new ArrayList<>();
        for (int i = 0; i < contentListArray.length(); i++) {
            JSONObject contentListObject = contentListArray.getJSONObject(i);

            boolean havePicture = contentListObject.getBoolean(ContentListArray.HAVE_PIC);
            if (!havePicture) {
                continue;
            }

            String title = contentListObject.getString(ContentListArray.TITLE);
            String publish_date = contentListObject.getString(ContentListArray.PUB_DATE);
            String source_web = contentListObject.getString(ContentListArray.SOURCE);
            String description = contentListObject.getString(ContentListArray.DESC);
            String content = contentListObject.getString(ContentListArray.CONTENT);

            JSONArray imageUrlsArray = contentListObject.getJSONArray(ContentListArray.IMAGE_URLS);
            JSONObject imageUrlsObject = imageUrlsArray.getJSONObject(0);

            String imageUrl = imageUrlsObject.getString(ContentListArray.ImageUrlsArray.URL);

            ContentValues newsValues = new ContentValues();

            newsValues.put(NewsEntry._TITLE, title);
            newsValues.put(NewsEntry._PUBLISH_DATE, publish_date);
            newsValues.put(NewsEntry._SOURCE_WEB, source_web);
            newsValues.put(NewsEntry._DESCRIPTION, description);
            newsValues.put(NewsEntry._CONTENT, content);
            newsValues.put(NewsEntry._IMAGE_URL, imageUrl);

            newsValuesList.add(newsValues);
        }

        return newsValuesList;
    }

    interface JsonNews {

        String SHOW_API_RES_BODY = "showapi_res_body";

        interface ShowApiResBodyObject {

            String PAGE_BEAN = "pagebean";

            interface PageBeanObject {

                String ALL_PAGES = "allPages";

                String CONTENT_LIST = "contentlist";

                interface ContentListArray {

                    String TITLE = "title";
                    String PUB_DATE = "pubDate";
                    String SOURCE = "source";
                    String DESC = "desc";
                    String CONTENT = "content";
                    String HAVE_PIC = "havePic";

                    String IMAGE_URLS = "imageurls";

                    interface ImageUrlsArray {

                        String URL = "url";
                    }
                }
            }
        }
    }
}
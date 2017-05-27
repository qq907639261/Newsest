package com.xhbb.qinzl.newsest.server;

import android.content.ContentValues;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtils.JsonNews.ShowApiResBodyObject.PageBeanObject.ContentListArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qinzl on 2017/5/27.
 */

public class JsonUtils {

    public static ContentValues[] getNewsValuesArray(String jsonString) throws JSONException {
        JSONObject jsonNews = new JSONObject(jsonString);
        JSONObject showApiResBodyObject = jsonNews.getJSONObject(JsonNews.SHOW_API_RES_BODY);
        JSONObject pageBeanObject = showApiResBodyObject.getJSONObject(JsonNews.ShowApiResBodyObject.PAGE_BEAN);

        int allPages = pageBeanObject.getInt(JsonNews.ShowApiResBodyObject.PageBeanObject.ALL_PAGES);

        JSONArray contentListArray = pageBeanObject.getJSONArray(JsonNews.ShowApiResBodyObject.PageBeanObject.CONTENT_LIST);

        int newsCount = contentListArray.length();
        ContentValues[] newsValuesArray = new ContentValues[newsCount];

        for (int i = 0; i < newsCount; i++) {
            JSONObject contentListObject = contentListArray.getJSONObject(i);

            String title = contentListObject.getString(ContentListArray.TITLE);
            String publist_date = contentListObject.getString(ContentListArray.PUB_DATE);
            String source_web = contentListObject.getString(ContentListArray.SOURCE);
            String description = contentListObject.getString(ContentListArray.DESC);
            String content = contentListObject.getString(ContentListArray.CONTENT);

            boolean havePicture = contentListObject.getBoolean(ContentListArray.HAVE_PIC);
            String imageUrl;
            if (havePicture) {
                JSONArray imageUrlsArray = contentListObject.getJSONArray(ContentListArray.IMAGE_URLS);
                JSONObject imageUrlsObject = imageUrlsArray.getJSONObject(0);

                imageUrl = imageUrlsObject.getString(ContentListArray.ImageUrlsArray.URL);
            } else {
                imageUrl = null;
            }

            newsValuesArray[i] = new ContentValues();

            newsValuesArray[i].put(NewsEntry._ALL_PAGES, allPages);
            newsValuesArray[i].put(NewsEntry._TITLE, title);
            newsValuesArray[i].put(NewsEntry._PUBLISH_DATE, publist_date);
            newsValuesArray[i].put(NewsEntry._SOURCE_WEB, source_web);
            newsValuesArray[i].put(NewsEntry._DESCRIPTION, description);
            newsValuesArray[i].put(NewsEntry._CONTENT, content);
            newsValuesArray[i].put(NewsEntry._IMAGE_URL, imageUrl);
        }

        return newsValuesArray;
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

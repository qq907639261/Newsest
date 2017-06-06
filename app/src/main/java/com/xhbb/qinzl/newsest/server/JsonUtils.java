package com.xhbb.qinzl.newsest.server;

import android.content.ContentValues;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtils.JsonNews.ShowApiResBodyObject.PageBeanObject.ContentListArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by qinzl on 2017/5/27.
 */

public class JsonUtils {

    public static int fillNewsValuesAndGetTotalPage(
            String jsonString, List<ContentValues> newsValuesList, String newsType)
            throws JSONException {
        JSONObject jsonNews = new JSONObject(jsonString);
        JSONObject showApiResBodyObject = jsonNews.getJSONObject(JsonNews.SHOW_API_RES_BODY);
        JSONObject pageBeanObject = showApiResBodyObject.getJSONObject(JsonNews.ShowApiResBodyObject.PAGE_BEAN);

        int totalPage = pageBeanObject.getInt(JsonNews.ShowApiResBodyObject.PageBeanObject.ALL_PAGES);

        JSONArray contentListArray = pageBeanObject.getJSONArray(JsonNews.ShowApiResBodyObject.PageBeanObject.CONTENT_LIST);

        for (int i = 0; i < contentListArray.length(); i++) {
            JSONObject contentListObject = contentListArray.getJSONObject(i);

            String newsCode = contentListObject.getString(ContentListArray.ID);
            String title = contentListObject.getString(ContentListArray.TITLE);
            String publish_date = contentListObject.getString(ContentListArray.PUB_DATE);
            String source_web = contentListObject.getString(ContentListArray.SOURCE);
            String content = contentListObject.getString(ContentListArray.CONTENT);

            JSONArray imageUrlsArray = contentListObject.getJSONArray(ContentListArray.IMAGE_URLS);

            String[] imageUrlStringArray = new String[3];
            for (int j = 0; j < imageUrlsArray.length() && j < 3; j++) {
                JSONObject imageUrlsObject = imageUrlsArray.getJSONObject(j);
                imageUrlStringArray[j] = imageUrlsObject.getString(ContentListArray.ImageUrlsArray.URL);
            }

            ContentValues newsValues = new ContentValues();
            newsValues.put(NewsEntry._NEWS_CODE, newsCode);
            newsValues.put(NewsEntry._TITLE, title);
            newsValues.put(NewsEntry._PUBLISH_DATE, publish_date);
            newsValues.put(NewsEntry._SOURCE_WEB, source_web);
            newsValues.put(NewsEntry._NEWS_CONTENT, content);
            newsValues.put(NewsEntry._IMAGE_URL_1, imageUrlStringArray[0]);
            newsValues.put(NewsEntry._IMAGE_URL_2, imageUrlStringArray[1]);
            newsValues.put(NewsEntry._IMAGE_URL_3, imageUrlStringArray[2]);
            newsValues.put(NewsEntry._NEWS_TYPE, newsType);

            newsValuesList.add(newsValues);
        }

        return totalPage;
    }

    interface JsonNews {

        String SHOW_API_RES_BODY = "showapi_res_body";

        interface ShowApiResBodyObject {

            String PAGE_BEAN = "pagebean";

            interface PageBeanObject {

                String ALL_PAGES = "allPages";

                String CONTENT_LIST = "contentlist";

                interface ContentListArray {

                    String ID = "id";
                    String TITLE = "title";
                    String PUB_DATE = "pubDate";
                    String SOURCE = "source";
                    String CONTENT = "content";

                    String IMAGE_URLS = "imageurls";

                    interface ImageUrlsArray {

                        String URL = "url";
                    }
                }
            }
        }
    }
}

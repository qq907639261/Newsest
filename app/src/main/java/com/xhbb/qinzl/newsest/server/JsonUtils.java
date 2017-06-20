package com.xhbb.qinzl.newsest.server;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.xhbb.qinzl.newsest.data.Contract.CommentEntry;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qinzl on 2017/5/27.
 */

public class JsonUtils {

    public static ContentValues[] getNewsValueses(String json, String newsType) {
        JsonNews jsonNews = new Gson().fromJson(json, JsonNews.class);

        JsonNews.ShowapiResBody.PageBean.Content[] newsPrimaries =
                jsonNews.showapi_res_body.pagebean.contentlist;

        ContentValues[] newsValueses = new ContentValues[newsPrimaries.length];
        for (int i = 0; i < newsPrimaries.length; i++) {
            JsonNews.ShowapiResBody.PageBean.Content newsPrimary = newsPrimaries[i];

            String[] imageUrls = new String[3];
            for (int j = 0; j < newsPrimary.imageurls.length && j < 3; j++) {
                imageUrls[j] = newsPrimary.imageurls[j].url;
            }

            ContentValues newsValues = new ContentValues();

            newsValues.put(NewsEntry._NEWS_TYPE, newsType);
            newsValues.put(NewsEntry._TOTAL_PAGE_BY_TYPE, jsonNews.showapi_res_body.pagebean.allPages);

            newsValues.put(NewsEntry._NEWS_CODE, newsPrimary.id);
            newsValues.put(NewsEntry._TITLE, newsPrimary.title);
            newsValues.put(NewsEntry._PUBLISH_DATE, newsPrimary.pubDate);
            newsValues.put(NewsEntry._SOURCE_WEB, newsPrimary.source);
            newsValues.put(NewsEntry._NEWS_CONTENT, newsPrimary.content);
            newsValues.put(NewsEntry._IMAGE_URL_1, imageUrls[0]);
            newsValues.put(NewsEntry._IMAGE_URL_2, imageUrls[1]);
            newsValues.put(NewsEntry._IMAGE_URL_3, imageUrls[2]);

            newsValueses[i] = newsValues;
        }
        return newsValueses;
    }

    public static ContentValues getCommentValues(String commentJson) {
        ContentValues commentValues = null;
        try {
            JSONObject jsonObject = new JSONObject(commentJson);

            long commentDate = jsonObject.getLong(JsonComment.DATE);
            String commentContent = jsonObject.getString(JsonComment.CONTENT);
            String newsCode = jsonObject.getString(JsonComment.CODE);

            commentValues = new ContentValues();

            commentValues.put(CommentEntry._COMMENT_CONTENT, commentContent);
            commentValues.put(CommentEntry._COMMENT_DATE, commentDate);
            commentValues.put(CommentEntry._NEWS_CODE, newsCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentValues;
    }

    private class JsonNews {

        ShowapiResBody showapi_res_body;

        class ShowapiResBody {

            PageBean pagebean;

            class PageBean {

                int allPages;

                Content[] contentlist;

                class Content {

                    String id;
                    String title;
                    String pubDate;
                    String source;
                    String content;

                    Image[] imageurls;

                    class Image {

                        String url;
                    }
                }
            }
        }
    }

    interface JsonComment {

        String DATE = "date";
        String CONTENT = "content";
        String CODE = "code";
    }
}

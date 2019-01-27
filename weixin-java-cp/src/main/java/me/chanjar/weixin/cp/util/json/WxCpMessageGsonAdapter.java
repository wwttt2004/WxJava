/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package me.chanjar.weixin.cp.util.json;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.chanjar.weixin.common.api.WxConsts.KefuMsgType;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.article.MpnewsArticle;
import me.chanjar.weixin.cp.bean.article.NewArticle;

/**
 * @author Daniel Qian
 */
public class WxCpMessageGsonAdapter implements JsonSerializer<WxCpMessage> {

  @Override
  public JsonElement serialize(WxCpMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();
    messageJson.addProperty("agentid", message.getAgentId());
    if (StringUtils.isNotBlank(message.getToUser())) {
      messageJson.addProperty("touser", message.getToUser());
    }
    messageJson.addProperty("msgtype", message.getMsgType());

    if (StringUtils.isNotBlank(message.getToParty())) {
      messageJson.addProperty("toparty", message.getToParty());
    }
    if (StringUtils.isNotBlank(message.getToTag())) {
      messageJson.addProperty("totag", message.getToTag());
    }

    if (StringUtils.isNotBlank(message.getSafe())) {
      messageJson.addProperty("safe", message.getSafe());
    }

    switch (message.getMsgType()) {
      case KefuMsgType.TEXT: {
        JsonObject text = new JsonObject();
        text.addProperty("content", message.getContent());
        messageJson.add("text", text);
        break;
      }
      case KefuMsgType.MARKDOWN: {
        JsonObject text = new JsonObject();
        text.addProperty("content", message.getContent());
        messageJson.add("markdown", text);
        break;
      }
      case KefuMsgType.TEXTCARD: {
        JsonObject text = new JsonObject();
        text.addProperty("title", message.getTitle());
        text.addProperty("description", message.getDescription());
        text.addProperty("url", message.getUrl());
        text.addProperty("btntxt", message.getBtnTxt());
        messageJson.add("textcard", text);
        break;
      }
      case KefuMsgType.IMAGE: {
        JsonObject image = new JsonObject();
        image.addProperty("media_id", message.getMediaId());
        messageJson.add("image", image);
        break;
      }
      case KefuMsgType.FILE: {
        JsonObject image = new JsonObject();
        image.addProperty("media_id", message.getMediaId());
        messageJson.add("file", image);
        break;
      }
      case KefuMsgType.VOICE: {
        JsonObject voice = new JsonObject();
        voice.addProperty("media_id", message.getMediaId());
        messageJson.add("voice", voice);
        break;
      }
      case KefuMsgType.VIDEO: {
        JsonObject video = new JsonObject();
        video.addProperty("media_id", message.getMediaId());
        video.addProperty("thumb_media_id", message.getThumbMediaId());
        video.addProperty("title", message.getTitle());
        video.addProperty("description", message.getDescription());
        messageJson.add("video", video);
        break;
      }
      case KefuMsgType.NEWS: {
        JsonObject newsJsonObject = new JsonObject();
        JsonArray articleJsonArray = new JsonArray();
        for (NewArticle article : message.getArticles()) {
          JsonObject articleJson = new JsonObject();
          articleJson.addProperty("title", article.getTitle());
          articleJson.addProperty("description", article.getDescription());
          articleJson.addProperty("url", article.getUrl());
          articleJson.addProperty("picurl", article.getPicUrl());
          articleJsonArray.add(articleJson);
        }
        newsJsonObject.add("articles", articleJsonArray);
        messageJson.add("news", newsJsonObject);
        break;
      }
      case KefuMsgType.MPNEWS: {
        JsonObject newsJsonObject = new JsonObject();
        if (message.getMediaId() != null) {
          newsJsonObject.addProperty("media_id", message.getMediaId());
        } else {
          JsonArray articleJsonArray = new JsonArray();
          for (MpnewsArticle article : message.getMpnewsArticles()) {
            JsonObject articleJson = new JsonObject();
            articleJson.addProperty("title", article.getTitle());
            articleJson.addProperty("thumb_media_id", article.getThumbMediaId());
            articleJson.addProperty("author", article.getAuthor());
            articleJson.addProperty("content_source_url", article.getContentSourceUrl());
            articleJson.addProperty("content", article.getContent());
            articleJson.addProperty("digest", article.getDigest());
            articleJson.addProperty("show_cover_pic", article.getShowCoverPic());
            articleJsonArray.add(articleJson);
          }

          newsJsonObject.add("articles", articleJsonArray);
        }
        messageJson.add("mpnews", newsJsonObject);
        break;
      }
      default: {
        // do nothing
      }
    }

    return messageJson;
  }

}

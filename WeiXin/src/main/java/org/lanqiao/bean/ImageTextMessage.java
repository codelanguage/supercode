package org.lanqiao.bean;

import java.util.List;

public class ImageTextMessage extends BaseMessage {

  private int ArticleCount;

  private List<Item> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Item> getArticles() {
        return Articles;
    }

    public void setArticles(List<Item> articles) {
        Articles = articles;
    }
}

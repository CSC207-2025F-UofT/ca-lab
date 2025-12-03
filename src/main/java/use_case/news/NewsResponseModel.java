package use_case.news;

import entity.NewsArticle;
import java.util.List;

/**
 * Data holder for the presenter to build the View
 * Created by the Interactor and passed to OutputBoundary
 */
public class NewsResponseModel {

    private final List<NewsArticle> articles;

    public NewsResponseModel(List<NewsArticle> articles) {
        this.articles = articles;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }
}

package interface_adapter.news;

import entity.NewsArticle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * The Presenter can update the states, so the View will changer correspondingly
 */
public class NewsViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // UI state fields (were in NewsState before)
    private List<NewsArticle> articles;
    private String errorMessage;

    // listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        // property name can be anything, keep "state" to avoid changing other code too much
        support.firePropertyChange("state", null, this);
    }


    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


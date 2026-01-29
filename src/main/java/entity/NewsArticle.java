package entity;

public class NewsArticle {

    // Fields from Finnhub API
    private final long id;          // unique article id
    private final String category;  // e.g. "general", "crypto", etc.
    private final long datetime;    // UNIX timestamp (seconds)
    private final String headline;  // article title
    private final String image;     // image URL
    private final String related;   // related stock and companies, e.g. "AAPL,Apple.Inc"
    private final String source;    // e.g., "Bloomberg"
    private final String summary;   // short description
    private final String url;       // link to full article

    public NewsArticle(long id,
                       String category,
                       long datetime,
                       String headline,
                       String image,
                       String related,
                       String source,
                       String summary,
                       String url) {
        this.id = id;
        this.category = category;
        this.datetime = datetime;
        this.headline = headline;
        this.image = image;
        this.related = related;
        this.source = source;
        this.summary = summary;
        this.url = url;
    }

    public long getId() {return id;}

    public String getCategory() {return category;}

    public long getDatetime() {return datetime;}

    public String getHeadline() {return headline;}

    public String getImage() {return image;}

    public String getRelated() {return related;}

    public String getSource() {return source;}

    public String getSummary() {return summary;}

    public String getUrl() {return url;}
}
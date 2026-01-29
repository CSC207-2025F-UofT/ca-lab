package view;

import interface_adapter.news.NewsController;
import interface_adapter.news.NewsViewModel;
import interface_adapter.ViewManagerModel;
import entity.NewsArticle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTextArea;


public class NewsView extends JPanel implements PropertyChangeListener {

    private ViewManagerModel viewManagerModel;
    private String homeViewName;

    private final String viewName = "news";

    private final NewsController controller;
    private final NewsViewModel viewModel;

    private final JButton backToHomeButton;
    private final JButton marketNewsButton;
    private final JButton companyNewsButton;
    private final JTextField symbolField;
    private final JTextField fromDateField;
    private final JTextField toDateField;
    private final JTable newsTable;
    private final DefaultTableModel tableModel;
    private final JLabel errorLabel;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // column indices for later use
    private static final int COL_DATE    = 0;
    private static final int COL_SYMBOL  = 1;
    private static final int COL_TITLE   = 2;
    private static final int COL_SUMMARY = 3;
    private static final int COL_SOURCE  = 4;
    private static final int COL_LINK    = 5;
    private static final int COL_IMAGE   = 6;

    public NewsView(NewsController controller, NewsViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());

        // Left: Back to Home button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backToHomeButton = new JButton("Back to Home");
        leftPanel.add(backToHomeButton);
        topPanel.add(leftPanel, BorderLayout.WEST);

        // Right: symbol/date filters and news buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        symbolField = new JTextField(8);
        fromDateField = new JTextField(10);
        toDateField = new JTextField(10);
        companyNewsButton = new JButton("Company News");
        marketNewsButton = new JButton("Market News");

        rightPanel.add(new JLabel("Symbol:"));
        rightPanel.add(symbolField);
        rightPanel.add(new JLabel("From (YYYY-MM-DD):"));
        rightPanel.add(fromDateField);
        rightPanel.add(new JLabel("To (YYYY-MM-DD):"));
        rightPanel.add(toDateField);
        rightPanel.add(companyNewsButton);
        rightPanel.add(marketNewsButton);

        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTRE TABLE =====
        String[] columnNames = {"Date", "Symbol", "Title", "Summary", "Source", "Link", "Image"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the NewsTable right here
        newsTable = new JTable(tableModel);

        // set renderer for headline column to enable wrapping
        newsTable.getColumnModel()
                .getColumn(COL_SUMMARY)
                .setCellRenderer(new TextAreaRenderer());

        newsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = newsTable.rowAtPoint(e.getPoint());
                int col = newsTable.columnAtPoint(e.getPoint());

                if (row < 0 || currentArticles == null || row >= currentArticles.size()) {
                    return;
                }

                NewsArticle article = currentArticles.get(row);

                try {
                    if (col == COL_LINK) { // column index for "Link"
                        String url = article.getUrl();
                        if (url != null && !url.isBlank()) {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                        }
                    } else if (col == COL_IMAGE) { // column index for "Image"
                        String imgUrl = article.getImage();
                        if (imgUrl != null && !imgUrl.isBlank()) {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(imgUrl));
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            NewsView.this,
                            "Unable to open link: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // turn off auto-resize of chart
        newsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //set preferred width
        newsTable.getColumnModel().getColumn(COL_DATE).setPreferredWidth(130);
        newsTable.getColumnModel().getColumn(COL_SYMBOL).setPreferredWidth(80);
        newsTable.getColumnModel().getColumn(COL_TITLE).setPreferredWidth(160);
        newsTable.getColumnModel().getColumn(COL_SUMMARY).setPreferredWidth(360);
        newsTable.getColumnModel().getColumn(COL_SOURCE).setPreferredWidth(100);
        newsTable.getColumnModel().getColumn(COL_LINK).setPreferredWidth(60);
        newsTable.getColumnModel().getColumn(COL_IMAGE).setPreferredWidth(60);

        JScrollPane scrollPane = new JScrollPane(newsTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM ERROR LABEL ==============================================
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS ==================================================

        // Back to Home – placeholder for now
        backToHomeButton.addActionListener(e -> {
            if (viewManagerModel != null && homeViewName != null) {
                viewManagerModel.setState(homeViewName);
                viewManagerModel.firePropertyChange();
            }
        });

        // Market news – load top market news into centre table
        marketNewsButton.addActionListener(e -> controller.loadMarketNews());

        // Company news – use filters and show results in centre table
        companyNewsButton.addActionListener(e -> {
            String symbol = symbolField.getText().trim();
            String from = fromDateField.getText().trim();
            String to = toDateField.getText().trim();
            controller.loadCompanyNews(symbol, from, to);
        });

        // automatically show top market news when this view is created
        controller.loadMarketNews();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        updateTable(viewModel.getArticles());
        updateError(viewModel.getErrorMessage());
    }

    private java.util.List<NewsArticle> currentArticles;

    private void updateTable(List<NewsArticle> articles) {
        tableModel.setRowCount(0);  // clear
        currentArticles = articles;

        if (articles == null) {
            return;
        }

        for (NewsArticle article : articles) {
            String dateStr = formatDate(article.getDatetime());
            String symbol = article.getRelated();
            String title = article.getHeadline();
            String summary = article.getSummary();
            String source = article.getSource();

            String linkLabel = (article.getUrl() == null || article.getUrl().isBlank()) ? "" : "Open";
            String imageLabel = (article.getImage() == null || article.getImage().isBlank()) ? "" : "View";

            tableModel.addRow(new Object[]{dateStr, symbol, title, summary, source, linkLabel, imageLabel});
        }
    }

    private void updateError(String errorMessage) {
        if (errorMessage == null || errorMessage.isBlank()) {
            errorLabel.setText(" ");
        } else {
            errorLabel.setText(errorMessage);
        }
    }

    private String formatDate(long unixSeconds) {
        if (unixSeconds <= 0) {
            return "";
        }
        LocalDateTime dt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixSeconds),
                ZoneId.systemDefault()
        );
        return dt.format(DATE_FORMATTER);
    }

    private static class TextAreaRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {

            setText(value == null ? "" : value.toString());

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
            int preferredHeight = getPreferredSize().height;
            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            return this;
        }
    }

    public String getViewName(){
        return this.viewName;
    }

    public void setBackNavigation(ViewManagerModel viewManagerModel, String homeViewName) {
        this.viewManagerModel = viewManagerModel;
        this.homeViewName = homeViewName;
    }
}

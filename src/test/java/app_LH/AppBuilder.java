//package app_LH;
//
//import data_access.FinnhubEarningsDataAccessObject;
//import interface_adapter.earnings_history.EarningsHistoryController;
//import interface_adapter.earnings_history.EarningsHistoryPresenter;
//import interface_adapter.earnings_history.EarningsHistoryViewModel;
//import use_case.earnings_history.EarningsDataAccessInterface;
//import use_case.earnings_history.GetEarningsHistoryInputBoundary;
//import use_case.earnings_history.GetEarningsHistoryInteractor;
//import use_case.earnings_history.GetEarningsHistoryOutputBoundary;
//import view.EarningsHistoryView;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * Builds a JFrame with the EarningsHistoryView wired to its use case.
// */
//public class AppBuilder {
//
//    private final JFrame frame;
//    private final CardLayout cardLayout;
//    private final JPanel viewsPanel;
//    private static final String EARNINGS_HISTORY_VIEW = "earnings history";
//
//    public AppBuilder() {
//        frame = new JFrame("Earnings History – Test App");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        cardLayout = new CardLayout();
//        viewsPanel = new JPanel(cardLayout);
//        frame.setContentPane(viewsPanel);
//    }
//
//    /**
//     * Add the EarningsHistory view, wires controller,interactor, and DAO.
//     */
//    public AppBuilder addEarningsHistoryView() {
//        EarningsHistoryViewModel earningsVM = new EarningsHistoryViewModel();
//        EarningsDataAccessInterface earningsDAO = new FinnhubEarningsDataAccessObject();
//        GetEarningsHistoryOutputBoundary presenter = new EarningsHistoryPresenter(earningsVM);
//        GetEarningsHistoryInputBoundary interactor = new GetEarningsHistoryInteractor(earningsDAO, presenter);
//        EarningsHistoryController controller = new EarningsHistoryController(interactor, earningsVM);
//        EarningsHistoryView earningsView = new EarningsHistoryView(controller, earningsVM);
//
//        viewsPanel.add(earningsView, EARNINGS_HISTORY_VIEW);
//
//        return this;
//    }
//
//    /**
//     * Finalizes the window and returns the JFrame.
//     */
//    public JFrame build() {
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        cardLayout.show(viewsPanel, EARNINGS_HISTORY_VIEW); //show the earnings view by default
//
//        return frame;
//    }
//}

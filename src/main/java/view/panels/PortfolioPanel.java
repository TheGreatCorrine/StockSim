package view.panels;

import entity.Portfolio;
import entity.UserStock;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.TableComponent;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

public class PortfolioPanel extends JPanel implements IComponent {
    private static final String TITLE = "Portfolio Overview";
    private static final String[] COLUMN_NAMES = {
        "Ticker", "Company", "Quantity", "Avg Cost", "Market Price", "Total Value", "Profit"
    };
    private static final double[] COLUMN_PROPORTIONS = {
        0.10, // Ticker
        0.25, // Company
        0.10, // Quantity
        0.15, // Avg Cost
        0.15, // Market Price
        0.15, // Total Value
        0.10 // Profit
    };
    private static final int HEADER_HEIGHT = 40;
    private static final int PADDING = 20;

    private final TableComponent portfolioTable;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    public PortfolioPanel() {
        ViewManager.Instance().registerComponent(this);

        // Initialize table
        DefaultTableModel tableModel = createTableModel();
        portfolioTable = new TableComponent(tableModel, COLUMN_PROPORTIONS);
        FontManager.Instance().useRegular(portfolioTable, 14f);
        rowSorter = new TableRowSorter<>(tableModel);
        portfolioTable.setRowSorter(rowSorter);
        setupNumericComparators();
        rowSorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        // Set up panel layout
        setLayout(new BorderLayout(0, PADDING));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add header
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));
        add(headerPanel, BorderLayout.NORTH);

        // Add table
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Add resize listener
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                portfolioTable.adjustColumnWidths();
            }
        });
    }

    private double parsePrice(String priceStr) {
        // Removes decorative parts of the string to sort numerically
        try {
            return Double.parseDouble(priceStr.replace("$", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void setupNumericComparators() {
        // Sorts the quantity column numerically
        rowSorter.setComparator(2, (qty1Str, qty2Str) -> {
            int qty1 = Integer.parseInt(qty1Str.toString());
            int qty2 = Integer.parseInt(qty2Str.toString());
            return Integer.compare(qty1, qty2);
        });

        // Sorts the average price per share column numerically
        rowSorter.setComparator(3, (cost1Str, cost2Str) -> {
            double cost1 = parsePrice(cost1Str.toString());
            double cost2 = parsePrice(cost2Str.toString());
            return Double.compare(cost1, cost2);
        });

        // Sorts the market price per share column numerically
        rowSorter.setComparator(4, (price1Str, price2Str) -> {
            double price1 = parsePrice(price1Str.toString());
            double price2 = parsePrice(price2Str.toString());
            return Double.compare(price1, price2);
        });

        // Sorts the total value column numerically
        rowSorter.setComparator(5, (total1Str, total2Str) -> {
            double total1 = parsePrice(total1Str.toString());
            double total2 = parsePrice(total2Str.toString());
            return Double.compare(total1, total2);
        });

        // Sorts the profit column numerically
        rowSorter.setComparator(6, (profit1Str, profit2Str) -> {
            double profit1 = parsePrice(profit1Str.toString());
            double profit2 = parsePrice(profit2Str.toString());
            return Double.compare(profit1, profit2);
        });
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel createHeaderPanel() {
        // Create main header panel with fixed height
        JPanel headerPanel = new JPanel(new BorderLayout(PADDING, 0));
        headerPanel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));

        // Title with vertical centering
        JLabel titleLabel = new JLabel(TITLE);
        FontManager.Instance().useBold(titleLabel, 18f);

        // Center the title vertically
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalGlue());

        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private void updateTableData(Portfolio portfolio) {
        DefaultTableModel model = (DefaultTableModel) portfolioTable.getModel();
        model.setRowCount(0);

        if (portfolio != null) {
            for (UserStock userStock : portfolio.getAllUserStocks()) {
                double marketValue = userStock.getMarketValue();
                double totalCost = userStock.getTotalCost();
                double totalProfit = marketValue - totalCost;

                model.addRow(new Object[] {
                    userStock.getStock().getTicker(),
                    userStock.getStock().getCompany(),
                    userStock.getQuantity(),
                    String.format("$%.2f", userStock.getAvgCost()),
                    String.format("$%.2f", userStock.getStock().getMarketPrice()),
                    String.format("$%.2f", marketValue),
                    String.format("$%.2f", totalProfit)
                });
            }
        }

        // Reset table sorting to default sort by date
        rowSorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent assetEvent) {
            updateTableData(assetEvent.getPortfolio());
        }
    }
}

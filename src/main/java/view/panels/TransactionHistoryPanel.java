package view.panels;

import entity.Transaction;
import entity.TransactionHistory;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.ButtonComponent;
import view.components.TableComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateTransactionHistoryEvent;
import view.view_events.ViewEvent;

public class TransactionHistoryPanel extends JPanel implements IComponent {
    private static final int PADDING = 20;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private static final String[] COLUMN_NAMES = {"Date", "Ticker", "Action", "Price", "Quantity", "Total Price"};
    private static final double[] COLUMN_PROPORTIONS = {
        0.25, // Date
        0.15, // Ticker
        0.10, // Action
        0.15, // Price
        0.15, // Quantity
        0.20 // Total Price
    };

    private final TableComponent historyTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<TableModel> rowSorter;

    public TransactionHistoryPanel() {
        ViewManager.Instance().registerComponent(this);

        // Set up main panel
        setLayout(new BorderLayout(0, PADDING));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Create table model and table
        tableModel = createTableModel();
        historyTable = new TableComponent(tableModel, COLUMN_PROPORTIONS);
        FontManager.Instance().useRegular(historyTable, 14f);

        // Enable sorting
        rowSorter = new TableRowSorter<>(tableModel);
        historyTable.setRowSorter(rowSorter);
        setupNumericComparators();
        rowSorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        // Create and add header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Create and add table panel
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add resize listener
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                historyTable.adjustColumnWidths();
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
        // Sorts the price per stock column numerically
        rowSorter.setComparator(3, (price1Str, price2Str) -> {
            double price1 = parsePrice(price1Str.toString());
            double price2 = parsePrice(price2Str.toString());
            return Double.compare(price1, price2);
        });

        // Sorts the quantity column numerically
        rowSorter.setComparator(4, (qty1Str, qty2Str) -> {
            int qty1 = Integer.parseInt(qty1Str.toString());
            int qty2 = Integer.parseInt(qty2Str.toString());
            return Integer.compare(qty1, qty2);
        });

        // Sorts the total price column numerically
        rowSorter.setComparator(5, (total1Str, total2Str) -> {
            double total1 = parsePrice(total1Str.toString());
            double total2 = parsePrice(total2Str.toString());
            return Double.compare(total1, total2);
        });
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Title on the left
        JLabel titleLabel = new JLabel("Transaction History");
        FontManager.Instance().useBold(titleLabel, 24f);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button on the right
        ButtonComponent backButton = new ButtonComponent("Back to Home");
        FontManager.Instance().useRegular(backButton, 14f);
        backButton.addActionListener(
                e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel")));
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void updateTableData(TransactionHistory history) {
        tableModel.setRowCount(0);
        if (history != null) {
            for (Transaction transaction : history.getTransactions()) {
                double totalPrice = transaction.executionPrice() * transaction.quantity();
                tableModel.addRow(new Object[] {
                    DATE_FORMAT.format(transaction.timestamp()),
                    transaction.ticker(),
                    transaction.type(),
                    String.format("$%.2f", transaction.executionPrice()),
                    transaction.quantity(),
                    String.format("$%.2f", totalPrice)
                });
            }
        }

        // Reset table sorting to default sort by date
        rowSorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateTransactionHistoryEvent historyEvent) {
            updateTableData(historyEvent.getTransactionHistory());
        }
    }
}

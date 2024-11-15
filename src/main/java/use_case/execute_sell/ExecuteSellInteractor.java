package use_case.execute_sell;

import entity.*;
import utility.exceptions.ValidationException;

import java.util.Date;

/**
 * The Execute Sell Interactor.
 */
public class ExecuteSellInteractor implements ExecuteSellInputBoundary {

    private final ExecuteSellDataAccessInterface dataAccess;
    private final ExecuteSellOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteSellInteractor class.
     * It instantiates a new Execute Sell Interactor.
     * @param dataAccess the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteSellInteractor(ExecuteSellDataAccessInterface dataAccess, ExecuteSellOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    /**
     * This method executes the sell transaction.
     * It includes both sell and short sell.
     * @param data the input data
     */
    @Override
    public void execute(ExecuteSellInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            // Get stock and quantity
            String ticker = data.ticker();
            int quantity = data.quantity();
            Stock stock = StockMarket.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);

            // make sure that the user has enough money to buy the stock back in the future
            double currentPrice = stock.getPrice();
            // this is the total cost to buy the stock back in the future,
            // but also the total revenue from selling the stock now
            double totalCost = currentPrice * quantity;
            double totalAsset = currentUser.getBalance() + currentUser.getPortfolio().getTotalValue();
            // the user can not sell/short sell as many shares as he wants
            if (totalCost <= totalAsset) {
                // Increase balance
                currentUser.addBalance(totalCost);
                // Update portfolio (quantity)
                Portfolio portfolio = currentUser.getPortfolio();
                updateOrDeleteStockFromPortfolio(portfolio, stock, quantity, currentPrice);
                // Add transaction to transaction history
                // TODO: timestamp synchronization
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, currentPrice, "buy");
                currentUser.getTransactionHistory().addTransaction(transaction);
                // Prepare success view
                outputPresenter.prepareSuccessView(new ExecuteSellOutputData(
                        currentUser.getBalance(),
                        currentUser.getPortfolio()
                ));
            } else {
                throw new InsufficientMarginCallException();
            }
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundExceptionView();
        } catch (InsufficientMarginCallException e) {
            outputPresenter.prepareInsufficientMarginCallExceptionView();
        }
    }

    /**
     * This method updates the stock in the portfolio or deletes a stock to the user's portfolio.
     * @param portfolio the portfolio of the user
     * @param stock the stock the user sells or short sells
     * @param quantity the quantity the user sells or short sells
     * @param currentPrice the current price of the stock
     */
    private void updateOrDeleteStockFromPortfolio(Portfolio portfolio, Stock stock, int quantity, double currentPrice) {
        portfolio.getUserStock(stock.getTicker())
                .ifPresentOrElse(
                        existingStock -> existingStock.updateUserStock(currentPrice, -quantity),
                        () -> portfolio.addStock(new UserStock(stock, currentPrice, -quantity))
                );
    }

    static class InsufficientMarginCallException extends Exception {
    }

    static class StockNotFoundException extends Exception {
    }
}
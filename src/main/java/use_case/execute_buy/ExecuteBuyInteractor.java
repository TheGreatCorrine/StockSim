package use_case.execute_buy;

import entity.*;
import utility.MarketTracker;
import utility.ServiceManager;
import utility.exceptions.DocumentParsingException;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;
import java.util.Date;

/**
 * The interactor for the Buy Stock use case
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    private final ExecuteBuyDataAccessInterface dataAccess;
    private final ExecuteBuyOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteBuyInteractor class.
     * It instantiates a new Execute Buy Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteBuyInteractor(ExecuteBuyDataAccessInterface dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
        ServiceManager.Instance().registerService(ExecuteBuyInputBoundary.class, this);
    }

    /**
     * This method executes the buy transaction.
     *
     * @param data the input data
     */
    @Override
    public void execute(ExecuteBuyInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            // Get stock and quantity
            String ticker = data.ticker();
            int quantity = data.quantity();
            Stock stock = MarketTracker.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);

            // Calculate some values for this transaction
            double currentPrice = stock.getMarketPrice();
            double totalCost = currentPrice * quantity;

            if (currentUser.getBalance() >= totalCost) {
                // Deduct balance
                currentUser.deductBalance(totalCost);

                // Update portfolio
                Portfolio portfolio = currentUser.getPortfolio();
                portfolio.updatePortfolio(stock, quantity, currentPrice);

                // Add transaction
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, currentPrice, "BUY");
                currentUser.getTransactionHistory().addTransaction(transaction);

                // update user data
                dataAccess.updateUserData(currentUser);

                // Prepare success view
                outputPresenter.prepareSuccessView(new ExecuteBuyOutputData(
                        currentUser.getBalance(),
                        currentUser.getPortfolio(),
                        currentUser.getTransactionHistory()
                ));
            } else {
                throw new InsufficientBalanceException();
            }
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundExceptionView();
        } catch (InsufficientBalanceException e) {
            outputPresenter.prepareInsufficientBalanceExceptionView();
        } catch (ServerException e) {
            outputPresenter.prepareServerErrorView();
        }
    }


    static class InsufficientBalanceException extends Exception {
    }

    static class StockNotFoundException extends Exception {
    }

    static class InvalidQuantityException extends Exception {
        public InvalidQuantityException(String message) {
            super(message);
        }
    }
}

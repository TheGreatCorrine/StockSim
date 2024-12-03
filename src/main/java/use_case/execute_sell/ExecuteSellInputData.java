package use_case.execute_sell;

/**
 * This class records the input data for the ExecuteBuy use case
 *
 * @param credential the credential of the user
 * @param ticker the ticker of the stock
 * @param quantity the quantity of the stock to buy
 */
public record ExecuteSellInputData(String credential, String ticker, int quantity) {}
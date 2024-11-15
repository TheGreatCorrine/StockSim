package use_case.execute_sell;

import entity.Portfolio;

/**
 * This class represents the output data for the ExecuteSell use case.
 * @param newBalance the new balance of the user
 * @param newPortfolio the new portfolio of the user
 */
public record ExecuteSellOutputData(
        double newBalance,
        Portfolio newPortfolio
) {
}

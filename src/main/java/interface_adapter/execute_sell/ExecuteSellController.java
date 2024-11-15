package interface_adapter.execute_sell;

import use_case.execute_sell.ExecuteSellInputBoundary;
import use_case.execute_sell.ExecuteSellInputData;
import utility.ClientSessionManager;

public class ExecuteSellController {

    private final ExecuteSellInputBoundary interactor;

    /**
     * Constructor
     * @param interactor
     */
    public ExecuteSellController(ExecuteSellInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Execute sell
     * @param ticker
     * @param quantity
     */
    public void execute(String ticker, String quantity) {
        final ExecuteSellInputData data = new ExecuteSellInputData(
                ClientSessionManager.Instance().getCredential(),
                ticker,
                Integer.parseInt(quantity));

        interactor.execute(data);
    }
}

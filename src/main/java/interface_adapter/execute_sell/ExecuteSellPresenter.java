package interface_adapter.execute_sell;

import use_case.execute_sell.ExecuteSellOutputBoundary;
import use_case.execute_sell.ExecuteSellOutputData;
import utility.ViewManager;
import view.view_events.UpdateAssetEvent;

public class ExecuteSellPresenter implements ExecuteSellOutputBoundary {

    @Override
    public void prepareSuccessView(ExecuteSellOutputData outputData) {
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(
                        outputData.newPortfolio(),
                        outputData.newBalance()
                )
        );
    }

    @Override
    public void prepareInsufficientMarginCallExceptionView() {
    }

    @Override
    public void prepareStockNotFoundExceptionView() {
    }

    @Override
    public void prepareValidationExceptionView() {
    }
}

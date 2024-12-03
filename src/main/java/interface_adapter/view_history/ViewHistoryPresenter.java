package interface_adapter.view_history;

import use_case.view_history.ViewHistoryOutputBoundary;
import use_case.view_history.ViewHistoryOutputData;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.DialogEvent;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateTransactionHistoryEvent;

/** Presenter for the ViewHistory Use Case */
public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {

    public ViewHistoryPresenter() {
        ServiceManager.Instance().registerService(ViewHistoryOutputBoundary.class, this);
    }

    /**
     * Prepares the success view of the ViewHistory use case
     *
     * @param outputData the required display output data
     */
    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        ViewManager.Instance().broadcastEvent(new UpdateTransactionHistoryEvent(outputData.transactionHistory()));
        ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TransactionHistoryPanel"));
    }

    /** Prepares the ValidationException view for the ViewHistory use case */
    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You are not authorized to do this."));
    }
}

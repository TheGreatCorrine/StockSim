package use_case.execute_sell;

public interface ExecuteSellOutputBoundary {

    void prepareSuccessView(ExecuteSellOutputData outputData);

    void prepareInsufficientMarginCallExceptionView();

    void prepareStockNotFoundExceptionView();

    void prepareValidationExceptionView();
}

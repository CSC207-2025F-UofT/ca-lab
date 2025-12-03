package interface_adapter.akinator;

import interface_adapter.ViewModel;

/**
 * The view model for the Akinator use case.
 */
public class AkinatorViewModel extends ViewModel<AkinatorState>{

    public AkinatorViewModel() {
        super("akinator");
        setState(new AkinatorState());
    }
}

package interface_adapter.create_group;

import interface_adapter.ViewModel;

public class InGroupViewModel extends ViewModel<UserGroupState> {

    public InGroupViewModel() {
        super("in group");
        setState(new UserGroupState());
    }
}

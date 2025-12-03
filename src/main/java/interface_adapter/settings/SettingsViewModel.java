package interface_adapter.settings;

import interface_adapter.ViewModel;

/**
 * The view model for resetting Settings.
 */
public class SettingsViewModel extends ViewModel<SettingsState> {

    public static final String VIEW_NAME = "settings";

    public SettingsViewModel() {
        super(VIEW_NAME);
        this.setState(new SettingsState());
    }

    @Override
    public SettingsState getState() {
        return super.getState();
    }

    @Override
    public void setState(SettingsState state) {
        super.setState(state);
    }

}

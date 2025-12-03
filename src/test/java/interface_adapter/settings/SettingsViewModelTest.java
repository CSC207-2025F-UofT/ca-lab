package interface_adapter.settings;

import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SettingsViewModelTest {

    @Test
    void setAndGetState_andFireProperty() {
        SettingsViewModel vm = new SettingsViewModel();
        AtomicReference<SettingsState> received = new AtomicReference<>();
        vm.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                received.set((SettingsState) evt.getNewValue());
            }
        });

        SettingsState newState = new SettingsState();
        newState.setTheme("dark");
        vm.setState(newState);
        vm.firePropertyChange();

        assertNotNull(received.get());
        assertEquals("dark", received.get().getTheme());
    }
}

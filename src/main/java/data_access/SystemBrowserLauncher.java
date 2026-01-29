package data_access;

import use_case.spotify_auth.BrowserLauncher;
import java.awt.Desktop;
import java.net.URI;

/**
 * Opens URLs using the system's default browser
 */
public class SystemBrowserLauncher implements BrowserLauncher {

    @Override
    public void openUrl(String url) throws Exception {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI.create(url));
        } else {
            throw new UnsupportedOperationException("Desktop browsing not supported");
        }
    }
}
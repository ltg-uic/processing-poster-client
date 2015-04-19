package ltg.evl.uic.poster.listeners;

/**
 * Created by aperritano on 4/18/15.
 */
public interface LoginCollectionListener {

    void loadPosterEvent(LoginDialogEvent loginDialogEvent);

    void loadUserEvent(LoginDialogEvent loginDialogEvent);

    void loadClassEvent(LoginDialogEvent loginDialogEvent);

    void logoutDoneEvent();

    void initializationDone();
}

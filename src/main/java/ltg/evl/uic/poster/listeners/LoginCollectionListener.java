package ltg.evl.uic.poster.listeners;

import ltg.evl.uic.poster.json.mongo.ObjectEvent;

/**
 * Created by aperritano on 4/18/15.
 */
public interface LoginCollectionListener {

    void loadPosterEvent(LoginDialogEvent loginDialogEvent);

    void loadUserEvent(LoginDialogEvent loginDialogEvent);

    void loadClassEvent(LoginDialogEvent loginDialogEvent);

    void logoutDoneEvent();

    void initializationDone();

    void updatePosterItem(ObjectEvent objectEvent);

    void deletePosterItem(ObjectEvent objectEvent);

    void refreshEventReceived();
}

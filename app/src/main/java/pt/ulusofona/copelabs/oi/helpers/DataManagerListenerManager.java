package pt.ulusofona.copelabs.oi.helpers;

import android.util.Log;

import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a abstract class used to save the listeners of the DataManager in lists java components in order to
 * notify them when the interfaces are called.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public abstract class DataManagerListenerManager {

    private static final String TAG = DataManagerListenerManager.class.getSimpleName();

    /**
     * List of DataManagerInterface listeners.
     */
    private static List<DataManager.DataManagerInterface> listeners = new ArrayList<>();

    /**
     * List of DataManagerEmergencyInterface listeners.
     */
    private static List<DataManager.DataManagerEmergencyInterface> emergencyListeners = new ArrayList<>();

    /**
     * List of PushData listeners.
     */
    private static List<DataManager.PushData> pushDataListeners = new ArrayList<>();

    /**
     * List of Invitation listeners.
     */
    private static List<DataManager.Invitations> invitationsListeners = new ArrayList<>();

    /**
     * Registers an Invitation listener.
     *
     * @param invitations DataManager.Invitation listener.
     */
    public static void registerInvitationListener(DataManager.Invitations invitations) {
        invitationsListeners.add(invitations);
    }

    /**
     * Unregisters Invitation listener.
     *
     * @param invitations DataManager.invitation listener.
     */
    public static void unRegisterInvitationListener(DataManager.Invitations invitations) {
        invitationsListeners.remove(invitations);
    }

    /**
     * Registers PushData listener.
     *
     * @param pushDataInterface DataManager.PushData listener.
     */
    public static void registerPushDataListener(DataManager.PushData pushDataInterface) {
        pushDataListeners.add(pushDataInterface);
    }

    /**
     * Unregisters PushData listener.
     *
     * @param pushDataInterface DataManager.PushData listener.
     */
    public static void unRegisterPushDataListener(DataManager.PushData pushDataInterface) {
        pushDataListeners.remove(pushDataInterface);
    }

    /**
     * Registers Emergency listener.
     *
     * @param emergencyInterface DataManager.emergencyInterface listener.
     */
    public static void registerEmergencyListeners(DataManager.DataManagerEmergencyInterface emergencyInterface) {
        emergencyListeners.add(emergencyInterface);
    }

    /**
     * Unregisters Emergency listener.
     *
     * @param emergencyInterface DataManager.emergencyInterface listener.
     */
    public static void unRegisterEmergencyListeners(DataManager.DataManagerEmergencyInterface emergencyInterface) {
        emergencyListeners.remove(emergencyInterface);
    }

    /**
     * Registers DataManager listener.
     *
     * @param dataManagerInterface DataManager.dataManagerInterface listener.
     */
    public static void registerListener(DataManager.DataManagerInterface dataManagerInterface) {
        Log.d(TAG, "Registering a listener");
        listeners.add(dataManagerInterface);
    }

    /**
     * Unregisters DataManager listener.
     *
     * @param dataManagerInterface DataManager.dataManagerInterface listener.
     */
    public static void unRegisterListener(DataManager.DataManagerInterface dataManagerInterface) {
        Log.d(TAG, "UnRegistering a listener");
        listeners.remove(dataManagerInterface);
    }

    /**
     * Notifies new invitation to listeners.
     *
     * @param contact Contact requester.
     */
    static void notifyInvitationListeners(Contact contact) {
        for (DataManager.Invitations listeners : invitationsListeners)
            listeners.InvitationInConing(contact);
    }

    /**
     * Notifies new PushData to listeners.
     *
     * @param message New message (push data).
     */
    static void notiyPushDataListeners(Message message) {
        for (DataManager.PushData listener : pushDataListeners)
            listener.pushDataArrives(message);
    }

    /**
     * Notifies new Message arrives.
     *
     * @param message New message.
     */
    static void notifyData(Message message) {
        for (DataManager.DataManagerInterface listener : listeners) {
            if (listener instanceof DataManager.DataManagerInterface)
                listener.dataInComing(message);
        }
    }

    /**
     * Notifies when a message was sent.
     *
     * @param isSent Boolean message status.
     * @param id     String id of the message.
     */
    static void notifyDataSent(boolean isSent, String id) {
        for (DataManager.DataManagerInterface listener : listeners) {
            if (listener instanceof DataManager.DataManagerInterface)
                listener.dataSent(isSent, id);
        }
    }

    /**
     * Notifies new message to listeners
     *
     * @param message new message received.
     */
    static void notifyDataEmergencyInComing(Message message) {
        for (DataManager.DataManagerEmergencyInterface listener : emergencyListeners) {
            if (listener instanceof DataManager.DataManagerEmergencyInterface)
                listener.dataInComing(message);
        }
    }
}

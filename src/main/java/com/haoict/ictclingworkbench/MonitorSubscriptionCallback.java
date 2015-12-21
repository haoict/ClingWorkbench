/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;

/**
 *
 * @author Hao
 */
public class MonitorSubscriptionCallback extends SubscriptionCallback {

    private IOnDeviceMonitorEvent iEvent;
            
    public MonitorSubscriptionCallback(Service service) {
        super(service);
    }
    
    public MonitorSubscriptionCallback(Service service, IOnDeviceMonitorEvent iEvent) {
        super(service);
        this.iEvent = iEvent;
    }

    public void eventReceived(final GENASubscription subscription) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                List<StateVariableValue> values = new ArrayList<>();
                for (Map.Entry<String, StateVariableValue> entry
                        : ((Map<String, StateVariableValue>) subscription.getCurrentValues()).entrySet()) {
                    values.add(entry.getValue());
                }

                iEvent.onEventReceived(values);
            }
        });

        System.out.println("Event received: " + new Date());
    }

    public void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //view.setStartStopEnabled(false, true);
            }
        });
        System.out.println("Events missed: " + numberOfMissedEvents);
    }

    @Override
    protected void failed(final GENASubscription subscription,
            final UpnpResponse responseStatus,
            final Exception exception,
            final String defaultMsg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                String failureMessage;
                if (responseStatus == null && exception == null) {
                    failureMessage = "Subscription failed: No response and no exception received";
                } else {
                    failureMessage = responseStatus != null
                            ? "Subscription failed: " + responseStatus.getResponseDetails()
                            : "Subscription failed: " + exception.toString();
                }

                System.out.println(failureMessage);
                // view.setStartStopEnabled(true, false);

            }
        });
    }

    @Override
    public void established(GENASubscription subscription) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //view.setStartStopEnabled(false, true);
            }
        });
        System.out.println(
                "Subscription established for seconds: " + subscription.getActualDurationSeconds()
        );
    }

    @Override
    public void ended(GENASubscription subscription, final CancelReason reason, UpnpResponse responseStatus) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //view.setStartStopEnabled(true, false);
            }
        });
        System.out.println("Subscription ended" + (reason != null ? ": " + reason : ""));
    }
}

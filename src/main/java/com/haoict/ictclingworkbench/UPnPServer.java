/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

/**
 *
 * @author Hao
 */
public class UPnPServer {

    private UpnpService upnpService;
    private IOnDeviceEvent onDeviceEvent;

    public UpnpService getUpnpService() {
        return upnpService;
    }
    // UPnP discovery is asynchronous, we need a callback
    private RegistryListener listener = new RegistryListener() {

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry,
                RemoteDevice device) {
            System.out.println(
                    "Discovery started: " + device.getDisplayString()
            );
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry,
                RemoteDevice device,
                Exception ex) {
            System.out.println(
                    "Discovery failed: " + device.getDisplayString() + " => " + ex
            );
        }

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device available: " + device.getDisplayString()
            );
            // call method throgh interface IOnDeviceEvent on main frame
            onDeviceEvent.onDeviceAdded(device);
        }

        @Override
        public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device updated: " + device.getDisplayString()
            );
            // call method throgh interface IOnDeviceEvent on main frame
            onDeviceEvent.onDeviceUpdated(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device removed: " + device.getDisplayString()
            );
            // call method throgh interface IOnDeviceEvent on main frame
            onDeviceEvent.onDeviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            System.out.println(
                    "Local device added: " + device.getDisplayString()
            );
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            System.out.println(
                    "Local device removed: " + device.getDisplayString()
            );
        }

        @Override
        public void beforeShutdown(Registry registry) {
            System.out.println(
                    "Before shutdown, the registry has devices: "
                    + registry.getDevices().size()
            );
        }

        @Override
        public void afterShutdown() {
            System.out.println("Shutdown of registry complete!");

        }
    };

    public UPnPServer(IOnDeviceEvent onDeviceEvent) {
        try {
            this.onDeviceEvent = onDeviceEvent;
            // This will create necessary network resources for UPnP right away
            upnpService = new UpnpServiceImpl(listener);
            // Send a search message to all devices and services, they should respond soon
            upnpService.getControlPoint().search(new STAllHeader());
        } catch (Exception ex) {
            System.err.println("Exception: " + ex);
            System.exit(1);
        }

    }

    public void shutdown() {
        upnpService.shutdown();
    }
}

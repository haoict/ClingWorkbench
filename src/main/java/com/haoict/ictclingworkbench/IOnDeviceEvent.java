/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

import org.fourthline.cling.model.meta.Device;

/**
 *
 * @author Hao
 */
public interface IOnDeviceEvent {
    public void onDeviceUpdated(Device device);
    public void onDeviceRemoved(Device device); 
    public void onDeviceAdded(Device device);
}

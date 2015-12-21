/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

import java.util.List;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.state.StateVariableValue;

/**
 *
 * @author Hao
 */
public interface IOnDeviceMonitorEvent {
    public void onEventReceived(List<StateVariableValue> values);
    public void onEventsMissed(); 
    public void onFailed();
    public void onEstablished();
    public void onEnded();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.Service;

/**
 *
 * @author Hao
 */
public class MainFrame extends javax.swing.JFrame implements IOnDeviceEvent {

    private final UPnPServer upnpServer;
    private final DefaultListModel myDeviceListModel;
    private final ArrayList<Device> myDeviceList;
    private final DefaultTreeModel myTreeModel;

    public MainFrame() {
        initComponents();

        System.out.println("Starting Cling...");
        // create new service
        // we pass 'this' frame to server class to get event when device detect 
        // or remove through interface IOnDeviceEvent
        upnpServer = new UPnPServer(this);
        
        // array to store all detected devices, real device instance
        myDeviceList = new ArrayList<>();
        // array list model to display to list (in main fram, this is list of detected device)
        myDeviceListModel = new DefaultListModel();
        jListDevices.setModel(myDeviceListModel);
        // handerler when select a device from list
        jListDevices.addListSelectionListener(new MyListSelectionHandler()); 
        
        // tree model to display device detail in tree
        myTreeModel = new DefaultTreeModel(null);
        jTreeDeviceDetail.setModel(myTreeModel);
        // handerler when select detail from tree
        jTreeDeviceDetail.getSelectionModel().addTreeSelectionListener(new MyTreeSelectionListener());
    }

    // handerler when select detail from tree
    class MyTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            System.out.println(e.getPath());
            // Get selected node to check if it is Service type,
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeDeviceDetail.getLastSelectedPathComponent();
            if (selectedNode != null) {
                DeviceDetail dd = (DeviceDetail) selectedNode.getUserObject();
                System.out.println(dd.getName());
                
                // Neu selected node la dang SERVICE thi enable button use service
                if (dd.getType() == DeviceDetailType.SERVICE) {
                    jButtonUseService.setEnabled(true);
                } else {
                    jButtonUseService.setEnabled(false);
                }
                
                // Neu selected node la dang ACTION thi enable button Invoke Action
                if (dd.getType() == DeviceDetailType.ACTION) {
                    jButtonInvokeAction.setEnabled(true);
                } else {
                    jButtonInvokeAction.setEnabled(false);
                }
                
                
            }
        }

    }

    // handerler when select a device from list
    class MyListSelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {
                // Get selected item in device list
                Device selectedDevice = myDeviceList.get(jListDevices.getSelectedIndex());
                
                // add detail to tree
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(new DeviceDetail(selectedDevice.getDisplayString(), "", "", DeviceDetailType.ROOT));
                //DefaultMutableTreeNode root = new DefaultMutableTreeNode(selectedDevice.getDetails().getFriendlyName());
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("UDN" + selectedDevice.getIdentity().getUdn().toString(), "", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Device Type: " + selectedDevice.getType().toString(), "", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Descriptor URL: " + selectedDevice.getDetails().getBaseURL().toString(), "", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Manufacturer: " + selectedDevice.getDetails().getManufacturerDetails().getManufacturer(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Manufacturer URL/URI: " + selectedDevice.getDetails().getManufacturerDetails().getManufacturerURI(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Model Name: " + selectedDevice.getDetails().getModelDetails().getModelName(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Model #: " + selectedDevice.getDetails().getModelDetails().getModelNumber(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Model Despription: " + selectedDevice.getDetails().getModelDetails().getModelDescription(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Model URL/URI: " + selectedDevice.getDetails().getModelDetails().getModelURI(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                
                try {
                    DefaultMutableTreeNode detailNode1 = new DefaultMutableTreeNode(new DeviceDetail("Presentation URL/URI: " + selectedDevice.getDetails().getPresentationURI(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode1);
                } catch (Exception ex) {}
                                
                for (Icon i : selectedDevice.getIcons()) {
                    DefaultMutableTreeNode detailNode11 = new DefaultMutableTreeNode(new DeviceDetail(i.toString(),"", "", DeviceDetailType.INFO));
                    root.add(detailNode11);
                }
                

                // add service of this device to tree, each service has it own action, 
                // each action has argument input and output, add they also
                for (Service service : selectedDevice.getServices()) {
                    DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode(new DeviceDetail(service.getServiceType().getType(), "", "", DeviceDetailType.SERVICE));
                    for (Action action : service.getActions()) {
                        DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(new DeviceDetail(action.getName(), "", "", DeviceDetailType.ACTION));
                        // add input argument
                        for (ActionArgument aa : action.getInputArguments()) {
                            DefaultMutableTreeNode actionArgumentNode = new DefaultMutableTreeNode(new DeviceDetail(aa.getName(), "", "", DeviceDetailType.ACTION_ARGUMENT_IN));
                            DefaultMutableTreeNode actionArgumentDirection = new DefaultMutableTreeNode(new DeviceDetail("Direction: " + aa.getDirection().name(), "", "", DeviceDetailType.INFO));
                            DefaultMutableTreeNode actionArgumentRelateState = new DefaultMutableTreeNode(new DeviceDetail("Related State Variable: " + aa.getRelatedStateVariableName(), "", "", DeviceDetailType.INFO));
                            DefaultMutableTreeNode actionArgumentDataType = new DefaultMutableTreeNode(new DeviceDetail("Datatype: " + aa.getDatatype().getDisplayString(), "", "", DeviceDetailType.INFO));

                            actionArgumentNode.add(actionArgumentDirection);
                            actionArgumentNode.add(actionArgumentRelateState);
                            actionArgumentNode.add(actionArgumentDataType);
                            actionNode.add(actionArgumentNode);
                        }
                        // add output argument
                        for (ActionArgument aa : action.getOutputArguments()) {
                            DefaultMutableTreeNode actionArgumentNode = new DefaultMutableTreeNode(new DeviceDetail(aa.getName(), "", "", DeviceDetailType.ACTION_ARGUMENT_IN));
                            DefaultMutableTreeNode actionArgumentDirection = new DefaultMutableTreeNode(new DeviceDetail("Direction: " + aa.getDirection().name(), "", "", DeviceDetailType.INFO));
                            DefaultMutableTreeNode actionArgumentRelateState = new DefaultMutableTreeNode(new DeviceDetail("Related State Variable: " + aa.getRelatedStateVariableName(), "", "", DeviceDetailType.INFO));
                            DefaultMutableTreeNode actionArgumentDataType = new DefaultMutableTreeNode(new DeviceDetail("Datatype: " + aa.getDatatype().getDisplayString(), "", "", DeviceDetailType.INFO));

                            actionArgumentNode.add(actionArgumentDirection);
                            actionArgumentNode.add(actionArgumentRelateState);
                            actionArgumentNode.add(actionArgumentDataType);
                            actionNode.add(actionArgumentNode);
                        }
                        serviceNode.add(actionNode);
                    }
                    root.add(serviceNode);
                }

                myTreeModel.setRoot(root);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jListDevices = new javax.swing.JList();
        jButtonShutdown = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTreeDeviceDetail = new javax.swing.JTree();
        jButtonUseService = new javax.swing.JButton();
        jButtonInvokeAction = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jListDevices.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListDevices);

        jButtonShutdown.setText("Shutdown");
        jButtonShutdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShutdownActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTreeDeviceDetail);

        jButtonUseService.setText("Use Service");
        jButtonUseService.setEnabled(false);
        jButtonUseService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUseServiceActionPerformed(evt);
            }
        });

        jButtonInvokeAction.setText("Invoke Action");
        jButtonInvokeAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInvokeActionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShutdown))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonUseService)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonInvokeAction)
                        .addGap(0, 269, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUseService)
                    .addComponent(jButtonInvokeAction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonShutdown))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonShutdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShutdownActionPerformed
        // Shutdown the service
        upnpServer.shutdown();
        System.exit(0);
    }//GEN-LAST:event_jButtonShutdownActionPerformed

    private void jButtonUseServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUseServiceActionPerformed
        // get selected node in tree
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeDeviceDetail.getLastSelectedPathComponent();
        if (selectedNode != null) {
            DeviceDetail dd = (DeviceDetail) selectedNode.getUserObject();
            // Check if this selected node is SERVICE type, so we enable button use service
            if (dd.getType() == DeviceDetailType.SERVICE) {
                // TODO: To use more service, implement UI to use
                //  for example, if you want to use service set volume of CD Player, 
                //  you have to implement an UI to adjust volume
                // at first, we can only use service "SwitchPower", 
                if (dd.getName().contains("SwitchPower")) {
                    Device selectedDevice = myDeviceList.get(jListDevices.getSelectedIndex());
                    // create a UI Frame to use service
                    SwitchPowerFrame spf = new SwitchPowerFrame(upnpServer.getUpnpService(), selectedDevice);
                    spf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    spf.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jButtonUseServiceActionPerformed

    private void jButtonInvokeActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInvokeActionActionPerformed
        // get selected node in tree
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTreeDeviceDetail.getLastSelectedPathComponent();
                        
        if (selectedNode != null) {
            DeviceDetail dd = (DeviceDetail) selectedNode.getUserObject();
            // Check if this selected node is ACTION type, so we enable button use service
            if (dd.getType() == DeviceDetailType.ACTION) {
                Device selectedDevice = myDeviceList.get(jListDevices.getSelectedIndex());
                // create a UI Frame to use service
                
                // seach for action and service
                for (Service service : selectedDevice.getServices()) {
                    DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode(new DeviceDetail(service.getServiceType().getType(), "", "", DeviceDetailType.SERVICE));
                    for (Action action : service.getActions()) {
                        if (selectedNode.getParent().toString().contains(service.getServiceType().getType())
                            && selectedNode.toString().contains(action.getName())) {
                            
                            InvokeActionFrame aif = new InvokeActionFrame(upnpServer.getUpnpService(), selectedDevice, action);
                            aif.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            aif.setVisible(true);
                        }
                    }
                }
                
                
            }
        }
    }//GEN-LAST:event_jButtonInvokeActionActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInvokeAction;
    private javax.swing.JButton jButtonShutdown;
    private javax.swing.JButton jButtonUseService;
    private javax.swing.JList jListDevices;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTreeDeviceDetail;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onDeviceUpdated(Device device) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeviceRemoved(Device device) {
        try {
            for (int i = 0; i < myDeviceList.size(); i++) {
                if (myDeviceList.get(i).equals(device)) {
                    myDeviceList.remove(i);
                    myDeviceListModel.remove(i);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ex.getMessage());
        }
    }

    @Override
    public void onDeviceAdded(Device device) {
        try {
            myDeviceList.add(device);
            myDeviceListModel.addElement(device.getDisplayString());
        } catch (Exception ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
}

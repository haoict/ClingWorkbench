/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haoict.ictclingworkbench;

/**
 *
 * @author Hao
 */

public class DeviceDetail {
        private String name;
        private String description;
        private String image;
        private DeviceDetailType type;

        public DeviceDetail(String name, String description, String image, DeviceDetailType type) {
            this.name = name;
            this.description = description;
            this.image = image;
            this.type = type;
        }

        @Override
        public String toString() {
            return getName();
        }
        
        
        public DeviceDetail() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public DeviceDetailType getType() {
            return type;
        }

        public void setType(DeviceDetailType type) {
            this.type = type;
        }     
    }
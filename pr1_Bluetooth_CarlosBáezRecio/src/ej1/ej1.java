/*
 * Student Carlos Báez Recio
 * Exercise 1 from Bluetooth
 * Year 2020/2021
 */
package ej1;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

public class ej1 {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		try {
			
			//I'm creating an instance of my bluetooth in ld
			LocalDevice ld = LocalDevice.getLocalDevice();
			
			//From here I'm just using the different get methods to gather information from my bluetooth device 
			System.out.println("Bluetooth Address: "+ld.getBluetoothAddress());
			System.out.println("Friendly Name: "+ld.getFriendlyName());
			
			if (ld.isPowerOn()) {
				System.out.println("The device is ON");
			}
			else {
				System.out.println("The device is OFF");
			}
			
			if (ld.getDiscoverable()!=0) {
				System.out.println("Discoverable status is ON in mode "+ld.getDiscoverable());
			}
			else {
				System.out.println("Discoverable status is OFF "+ld.getDiscoverable());
			}
			System.out.println("Discovery agent: "+ld.getDiscoveryAgent());
			
			
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}

	}

}

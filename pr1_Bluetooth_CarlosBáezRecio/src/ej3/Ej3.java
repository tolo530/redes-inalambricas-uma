/*
 * Student Carlos Báez Recio
 * Exercise 3 from Bluetooth
 * Course 2020/2021
 */
package ej3;

import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

public class Ej3 {
	
	public static void main (String args[]) {
		
		final Object inquiryCompletedEvent = new Object();
		
		try {
			LocalDevice ld = LocalDevice.getLocalDevice();
			DiscoveryAgent da = ld.getDiscoveryAgent();
		}
		catch (BluetoothStateException e) {
			
		}
	}
}

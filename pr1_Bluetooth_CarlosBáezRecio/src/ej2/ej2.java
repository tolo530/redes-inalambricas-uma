/*
 * Student Carlos Báez Recio
 * Exercise 2 from Bluetooth
 * Year 2020/2021
 */
package ej2;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

public class ej2 { //Descubrimiento de dispositivos remotos
	
	
	
	public static void main(String[] args) {
		
		final Object inquiryCompletedEvent = new Object(); //creo un objeto que tiene que morir al final. Es Object porque es una clase generica y necesito usarla para crear una hebra
		
		try {
			LocalDevice ld = LocalDevice.getLocalDevice();  //Gano el control del dispositivo local
			DiscoveryAgent da = ld.getDiscoveryAgent();     //Consigo el agente de descubrir para poder buscar
			DiscoveryL dl = new DiscoveryL(inquiryCompletedEvent);				//Creo una interfaz discoveryListener
			
			
			
			synchronized (inquiryCompletedEvent) {
				try {
					boolean start = da.startInquiry(da.GIAC, dl); //Comienzo a buscar
					if (start) {
						System.out.println("Search begins...");
						inquiryCompletedEvent.wait();
					}
					else {
						System.out.println("Mmmm something went wrong: I couldn't begin any search");
					}
					
				}
				catch (Exception e) {
				}
				
			}
			
			
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

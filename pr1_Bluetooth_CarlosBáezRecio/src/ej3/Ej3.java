/*
 * Student Carlos Báez Recio
 * Exercise 3 from Bluetooth
 * Course 2020/2021
 */
package ej3;

import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;

public class Ej3 {
	
	public static void main (String args[]) {
		
		//Lo uso para crear un hilo extra
		final Object inquiryCompletedEvent = new Object();
		
		try {
			LocalDevice ld = LocalDevice.getLocalDevice();   //Ganamos control del dispositivo
			DiscoveryAgent da = ld.getDiscoveryAgent();		//Obtenemos discovery agent del dispositivo
			DiscoveryL dl = new DiscoveryL(inquiryCompletedEvent, null, null);				//Aqui creamos discovery listener al que le pasaremos los parametros que nos interesan, en este caso direccion y friendlyname
			
			//Ahora comienza la fiesta, sincronizamos el objeto para tener un hilo extra
			synchronized (inquiryCompletedEvent) {
				try {
					boolean start = da.startInquiry(da.GIAC, dl); //Comienzo a buscar. Ojo, startInquiry devuelve 0 en caso de que no haya podido buscar
					if (start) {
						System.out.println("Search begins, Mode "+dl.getMode()+"...");
						inquiryCompletedEvent.wait();
					}
					else {
						System.out.println("Mmmm something went wrong: I couldn't begin any search");
					}
					
				}
				catch (Exception e) {
				}
								
			}
		}
		catch (BluetoothStateException e) {
			
		}
	}
}

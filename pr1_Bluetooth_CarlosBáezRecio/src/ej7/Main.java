/*
 * Estudiante: Carlos Báez Recio
 * Práctica: Bluetooth Ej7
 * Asignatura: Redes Inalámbricas
 * Curso: 2020/2021
 */


package ej7;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

/*
 * Utlizando lo utlizado en los anteriores ejercicios, ahora implementaremos un chat
 * que mediante bluetooth, permita el envio de mensajes entre un cliente y un servidor.
 * El ejercicio 7 representa el cliente que se conecta al servidor.
 */

public class Main {

	public static void main(String[] args) {
		
		final Object synchro = new Object();
		
		try {
			LocalDevice ld = LocalDevice.getLocalDevice();
			DiscoveryAgent da = ld.getDiscoveryAgent();
			MyDiscoveryListener dl = new MyDiscoveryListener(synchro, "001A7DDA7111");
			UUID uuids[] = new UUID[1];
			int attridset[] = new int[1];
			
			
			synchronized (synchro) {
				//Buscamos el servidor con startInquiry
				boolean start = da.startInquiry(da.GIAC, dl);
				
				if (start) {
					System.out.println("Looking for server...");
					synchro.wait();
					
					//Si he encontrado el dispositivo que busco continuo
					if(dl.getDeviceFound()) {
						RemoteDevice rd = dl.getDevice();
						uuids[0] = new UUID(0x1002);
						attridset[0] = 0x0100;
						
						System.out.println("Looking for service...");
						da.searchServices(attridset, uuids, rd, dl);
						synchro.wait();
						
						//Si encuentro el servicio, tenemos que iniciar el chat con el servidor.
						if(dl.getServiceFound()) {
							
						}
						else {
							System.out.println("Error: the server does not offer the service you are looking for");
						}
					}
					else {
						System.out.println("Error: server not found");
					}
					
					
				}
				else {
					System.out.println("Error: the inquiry service could not start");
				}
			}
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

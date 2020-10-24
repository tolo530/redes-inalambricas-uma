/* Student: Carlos Báez Recio
 * Practice: Bluetooth
 * Course: Redes Inalámbricas - Universidad de Málaga
 * Year: 2020/2021
 */
package ej4_5_6;

import java.io.IOException;
import java.util.List;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;


/* La estrategia a utilizar en estos ejercicios es distinta a los anteriores.
 * En este necesitaremos crear una lista de dispositivos (devices) que iremos añadiendo 
 * conforme los descubrimos, es decir, como antes pero en lugar de mostrarlos, añadirlos
 * a la lista. Una vez hemos terminado la busqueda comenzaría la diversión. Tenemos que
 * recorrer la lista mostrando los servicios de cada dispositivo (ej4) o de un dispositivo 
 * concreto (ej5) o un servicio concreto de un dispositivo concreto (ej6)
 */

public class Main {

	public static void main(String[] args) {
		
		final Object syncItem = new Object();
		
		try {
			LocalDevice ld = LocalDevice.getLocalDevice();
			DiscoveryAgent da = ld.getDiscoveryAgent();
			MyDiscoveryListerner dl = new MyDiscoveryListerner(syncItem); //true dir, false name
			UUID uuids[] = new UUID[1];
			int attridset[] = new int[1];
			
			/* Comenzamos la busqueda de dispositivos creando un nuevo hilo de ejecución (vamos sincronizamos un objeto)
			 * Notese que todo lo que va dentro de las llaves de synchronize está siendo ejecutado en otro thread de 
			 * forma concurrente.
			 */
			synchronized (syncItem) {
				boolean start = da.startInquiry(da.GIAC, dl); //Este metodo devuelve TRUE si la busqueda comienza
				
				if (start) {
					//Si llego aquí, significa que la busqueda ha comenzado correctamente.
					System.out.println("Starting inquiry...");
					
					//Esperamos que termine la busqueda de dispositivos
					syncItem.wait();
					
					/*Ahora tenemos una lista de dispositivos con la que trabajar, esta lista la importamos a una variable en la clase Main
					 * Tambien asignamos la UUID del servicio que queremos buscar (Public Browse Group)
					 */
					List <RemoteDevice> remoteList = dl.getDevices();
					uuids[0] = new UUID(0x1002);
					attridset[0] = 0x0100;
					
					
					//Con nuestra lista ahora podemos empezar a buscar servicios en los dispositivos remotos que hemos encontrado
					for (RemoteDevice rd : remoteList) {
						try {
							System.out.println("Name: "+rd.getFriendlyName(true)+" Address: "+rd.getBluetoothAddress());
							da.searchServices(attridset, uuids, rd, dl);
							syncItem.wait();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					
					
				}
				else {
					//Si llego aquí la busqueda ha fallado y devolvemos un mensaje de error al usuario.
					System.out.println("ERROR: the inquiry service was unable to start");
				}
				
			}
			
			
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

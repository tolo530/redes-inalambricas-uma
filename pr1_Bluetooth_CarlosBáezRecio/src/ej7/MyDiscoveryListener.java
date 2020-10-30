package ej7;

import java.io.IOException;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class MyDiscoveryListener implements DiscoveryListener {
	
	private String address = null;
	private String service = null;
	private String url = null;
	private Object synchro = null;
	private RemoteDevice remdev = null;
	private boolean dvFound;
	private boolean svFound;

//Constructores----------------------------------------------------------------------
	
	public MyDiscoveryListener(Object synchro, String address) {
		this.synchro = synchro;
		this.address = address;
		this.service = "chat";
		this.dvFound = false;
		this.svFound = false;
	}
	
	public MyDiscoveryListener(Object synchro, String address, String service) {
		this.synchro = synchro;
		this.address = address;
		this.service = service;
		this.dvFound = false;
		this.svFound = false;
	}
	
//Funciones y Metodos-----------------------------------------------------------------	

	/*
	 * En esta función vamos a buscar el servidor al que estamos intentando conectarnos,
	 * en caso de encontrarlo, lo salvamos en nuestra variable RemoteDevice para poder tratar
	 * con él mas tarde. También, se avisa al usuario de que lo hemos encontrado. Luego
	 * habrá que buscar si ofrece el servicio de chat...
	 */
	@Override
	public void deviceDiscovered(RemoteDevice rm, DeviceClass dc) {
		if (rm.getBluetoothAddress().equals(address)) {
			remdev = rm;
			dvFound = true;
			try {
				if (rm.getFriendlyName(true) != null) {
					System.out.println(rm.getFriendlyName(true)+" ["+rm.getBluetoothAddress()+"] found!");
				}
				else {
					System.out.println("Unamed-device "+"["+rm.getBluetoothAddress()+"] found!");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void inquiryCompleted(int arg0) {
		synchronized(synchro) {
			synchro.notifyAll();
		}
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		synchronized(synchro) {
			synchro.notifyAll();
		}
	}
	
	/* Aquí vamos a buscar por el servicio chat. Si lo encontramos, tendremos que
	 * construir la URL del servicio para poder conectarnos. Lo que estoy haciendo es
	 * ir comparando uno a uno los NOMBRES de servicio con el servicio "chat" que estoy
	 * buscando, aunque el nombre puede cambiar en mi implementación.
	 * Si lo encuentro, guardo la url y digo que me es indiferente ser master o esclavo y
	 * el modo 0 (mirar comentario)
	 * Si no lo encuentro, voy al siguiente servicio.
	*/
	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] recordList) {
		boolean found = false;
		int i = 0;
		DataElement data = null;
		
		while ((!found)&&(i < recordList.length)) {
			data = recordList[i].getAttributeValue(0x100);
			
			if ( (data!=null) && (data.getValue().equals(service)) ) {
				found = true;
				//Construir URL
				url = recordList[i].getConnectionURL(0, false); //0-> NOAUTHENTICATE_NOENCRYPT
				System.out.println("Service found!");
			}
			
			i++;
		}
		svFound = found;
	}
	
	public RemoteDevice getDevice() {
		return remdev;
	}
	
	public String getURL() {
		return url;
	}
	
	public boolean getDeviceFound() {
		return dvFound;
	}

	public boolean getServiceFound() {
		return svFound;
	}

}

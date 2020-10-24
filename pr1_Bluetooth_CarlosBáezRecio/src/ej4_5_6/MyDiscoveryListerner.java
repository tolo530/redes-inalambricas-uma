package ej4_5_6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;




public class MyDiscoveryListerner implements DiscoveryListener {

	private Object syncItem = null;
	private List<RemoteDevice> deviceList = null;
	private String dFN = null;
	private String dAdr = null;
	private String dSer = null;
	private int mode =  -1;
	
	public MyDiscoveryListerner(Object syncItem) {
		this.syncItem = syncItem;
		deviceList = new ArrayList<RemoteDevice>();
		this.mode = 0;
	}
	
	public MyDiscoveryListerner(Object syncItem, String name, boolean select, String dSer) { //Boolean select-> address -> true, FName -> false
		this.syncItem = syncItem;
		deviceList = new ArrayList<RemoteDevice>();
		
		if (select) {
			this.dAdr = name;
			this.mode = 1;
			this.dSer = dSer;
		}
		else {
			this.dFN = name;
			this.mode = 2;
			this.dSer = dSer;
		}
	}
	
	public MyDiscoveryListerner(Object syncItem, String dAdr, String dFN, String dSer) {
		this.syncItem = syncItem;
		deviceList = new ArrayList<RemoteDevice>();
		this.dAdr = dAdr;
		this.dFN = dFN;
		this.mode = 3;
		this.dSer = dSer;
	}
	
	@Override
	public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
		if (mode==0) {
			DeviceDiscMode0(rd, dc); //Todos
		}
		else if (mode==1) {
			DeviceDiscMode1(rd, dc); //Filtro por dir
		}
		else if (mode==2) {
			DeviceDiscMode2(rd, dc); //Filtro por name
		}
		else if (mode==3) {
			DeviceDiscMode3(rd, dc); //Filtro por dir && name
		}
		else {
			System.out.println("ERROR: device discovery mode not supported ["+this.mode+"]");
		}
	}

	@Override
	public void inquiryCompleted(int c) {
		System.out.println("Device inquiry completed");
		synchronized (syncItem) {
			syncItem.notifyAll();
		}
		
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		synchronized (syncItem) {
			syncItem.notifyAll();
		}
		
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] recordList) {
		
		for (ServiceRecord record : recordList) {
			DataElement d = record.getAttributeValue(0x100); //Consigue el nombre del servicio
			
			if(dSer==null) {
				if (d!=null) {
					System.out.println((String)d.getValue());
				}
				else {
					System.out.println("Unnamed Service");
				}
			}
			else if (dSer!=null){
				if (d!=null) {
					if(dSer.equals((String)d.getValue())) {
						System.out.println((String)d.getValue());
					}
				}
			}	
		}
		
	}
	
//-------------------------------------------------------------------------------------------------------------------
	//Necesitaré devolver mi lista de dispositivos
	public List<RemoteDevice> getDevices(){
		return deviceList;
	}
	
	//Modo 0-> Busqueda de todos los dispositivos
	private void DeviceDiscMode0 (RemoteDevice rd, DeviceClass dc){
		deviceList.add(rd);
	}
	
	//Modo 1-> Busqueda filtrando por dirección
	private void DeviceDiscMode1 (RemoteDevice rd, DeviceClass dc){
		if (dAdr.equals(rd.getBluetoothAddress())) {
			deviceList.add(rd);
		}
	}
	
	//Modo 2-> Busqueda filtrando por friendly name
	private void DeviceDiscMode2 (RemoteDevice rd, DeviceClass dc){
		try {
			if (dFN.equals(rd.getFriendlyName(true))) {
				deviceList.add(rd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	//Modo 3-> Busqueda filtrando por dirección y friendly name
	private void DeviceDiscMode3 (RemoteDevice rd, DeviceClass dc){
		try {
			if ((dAdr.equals(rd.getBluetoothAddress()))&&(dFN.equals(rd.getFriendlyName(true)))) {
				deviceList.add(rd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

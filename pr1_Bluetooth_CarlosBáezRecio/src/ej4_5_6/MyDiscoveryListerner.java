package ej4_5_6;

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
	
	public MyDiscoveryListerner(Object syncItem) {
		this.syncItem = syncItem;
		deviceList = new ArrayList<RemoteDevice>();
	}
	
	@Override
	public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
		deviceList.add(rd);
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
			
			if (d!=null) {
				System.out.println((String)d.getValue());
			}
			else {
				System.out.println("Unnamed Service");
			}
			//System.out.println(record.getConnectionURL(arg0, arg1));
		}
		
	}
	
//-------------------------------------------------------------------------------------------------------------------
	//Necesitaré devolver mi lista de dispositivos
	public List<RemoteDevice> getDevices(){
		return deviceList;
	}
	
	

}

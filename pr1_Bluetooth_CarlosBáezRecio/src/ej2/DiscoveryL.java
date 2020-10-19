package ej2;

import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class DiscoveryL implements javax.bluetooth.DiscoveryListener {
	
	private Object remote; 
	
	public DiscoveryL (Object remote) {
		this.remote = remote;
	}

	@Override
	public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
		
		try {
			System.out.println(rd.getBluetoothAddress()+" "+rd.getFriendlyName(true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void inquiryCompleted(int arg0) {
		System.out.println("Search completed");
		synchronized(remote) {
			remote.notifyAll();
		}
		
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		
		
	}

	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		
		
	}

}

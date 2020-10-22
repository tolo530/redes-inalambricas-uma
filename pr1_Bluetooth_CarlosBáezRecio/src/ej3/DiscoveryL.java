package ej3;

import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class DiscoveryL implements javax.bluetooth.DiscoveryListener {
	
	private Object remote=null; //Efectivamente, este es el objeto a sincronizar
	private String lf_dir=null; //Estamos buscando por esta dirección
	private String lf_name=null; //Estamos buscando por este friendly name
	private int mode=-1; //Voy a tener 4 posibilidades-> 0->no dir no name, 1->dir no name, 2-> no dir name, 3-> dir name, otherwise-> error
	

	public DiscoveryL(Object remote, String lf_dir, String lf_name) {
		this.remote = remote;
		this.lf_dir = lf_dir;
		this.lf_name = lf_name;
		
		if ((this.lf_dir==null)&&(this.lf_name==null)) {
			this.mode=0;
		}
		if ((this.lf_dir!=null)&&(this.lf_name==null)) {
			this.mode=1;
		}
		if ((this.lf_dir==null)&&(this.lf_name!=null)) {
			this.mode=2;
		}
		if ((this.lf_dir!=null)&&(this.lf_name!=null)) {
			this.mode=3;
		}
	}
	

	@Override
	public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
		
		if (mode==0) {
			//System.out.println("Mode: "+this.mode+ " Default mode");
			defaultDiscovery(rd, dc);
		}
		else if (mode==1) {
			//System.out.println("Mode: "+this.mode+ " Address search");
			discByDir(rd, dc);
		}
		else if (mode==2) {
			//System.out.println("Mode: "+this.mode+ " Name search");
			discByName(rd, dc);
		}
		else if (mode==3) {
			//System.out.println("Mode: "+this.mode+ " Address + Name search");
			discByDirName(rd, dc);
		}
		else {
			
		}
	}

	@Override
	public void inquiryCompleted(int arg0) {
		System.out.println("Search Completed");
		synchronized (remote) {
			remote.notifyAll();
		}
		
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub
		
	}

//--------------------------------------------------------------------------------------------
	
	//Mode 0: Buscamos todos los dispositivos. No tenemos filtro
	private void defaultDiscovery (RemoteDevice rd, DeviceClass dc) {
		try {
			System.out.println("Device Adress-> "+rd.getBluetoothAddress() + " Friendly Name-> "+rd.getFriendlyName(true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Mode 1: Filtramos por dirección
	private void discByDir (RemoteDevice rd, DeviceClass dc) {
		
			if (lf_dir.equals(rd.getBluetoothAddress())) {
				try {
					System.out.println("Device Adress-> "+rd.getBluetoothAddress() + " Friendly Name-> "+rd.getFriendlyName(true));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			
		
	}
	
	//Mode 2: Filtramos por Friendly Name
	private void discByName (RemoteDevice rd, DeviceClass dc) {
		try {
			if (lf_name.equals(rd.getFriendlyName(true))) {
				System.out.println("Device Adress-> "+rd.getBluetoothAddress() + " Friendly Name-> "+rd.getFriendlyName(true));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Mode 3: Filtramos por dirección y Friendly Name
	private void discByDirName (RemoteDevice rd, DeviceClass dc) {
		try {
			if ((lf_name.equals(rd.getFriendlyName(true)))&&(lf_dir.equals(rd.getBluetoothAddress()))) {
				System.out.println("Device Adress-> "+rd.getBluetoothAddress() + " Friendly Name-> "+rd.getFriendlyName(true));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getMode() {
		return this.mode;
	}

}

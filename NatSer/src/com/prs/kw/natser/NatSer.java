package com.prs.kw.natser;

import com.prs.kw.aidl.INativeService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class NatSer extends Service {

	static {
		System.loadLibrary("Natser");
	}

	private native void initNative();

	private native boolean startServerNative(int port);

	private native boolean stopServerNative();

	@Override
	public void onCreate() {
		super.onCreate();
		initNative();
		new Thread(){
			public void run() {
				while(true){
					Log.i("NatSer", "Running...");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final INativeService.Stub mBinder = new INativeService.Stub() {

		@Override
		public boolean stopServer() throws RemoteException {
			return stopServerNative();
		}

		@Override
		public boolean startServer(int port) throws RemoteException {
			return startServerNative(port);
		}
	};

	private void onNativeMessage(final String message) {
		Log.d("NatSer", "Message received from native: " + message);
	}

}

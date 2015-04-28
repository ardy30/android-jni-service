package com.prs.kw.natser.servercontrol;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.prs.kw.aidl.*;

public class NatSerApp extends Application {
	
	private static NativeServiceManager mNatSetManager = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNatSetManager = new NativeServiceManager();
		mNatSetManager.initConnection();
	}
	
	public static NativeServiceManager getNativeServiceManager(){
		return mNatSetManager;
	}
	
	public class NativeServiceManager {
		private String TAG = "NativeServiceManager";

		INativeService mINativeService = null;
		
		private ServiceConnection mConnection = new ServiceConnection() {

		    public void onServiceConnected(ComponentName className, IBinder service) {
		    	mINativeService = INativeService.Stub.asInterface(service);
		    }

		    public void onServiceDisconnected(ComponentName className) {
		        Log.e(TAG, "Service has unexpectedly disconnected");
		        mINativeService = null;
		    }
		};

		private void initConnection(){
			//Send broadcast to start remote service
			Intent startServiceIntent = new Intent("com.prs.kw.natser.START_SERVICE");
			sendBroadcast(startServiceIntent);
			
			//Bind to remote service
			Intent intent = new Intent();
			intent.setClassName("com.prs.kw.natser", "com.prs.kw.natser.NatSer");
			bindService(intent, mConnection, BIND_AUTO_CREATE);
		}
		
		public boolean startServer(int port){
			if(mINativeService!=null){
				try {
					return mINativeService.startServer(port);
				} catch (RemoteException e) {
					Toast.makeText(NatSerApp.this.getApplicationContext(), "Remote exception in calling Native Service startServer", Toast.LENGTH_LONG).show();
					
				}
			}
			Toast.makeText(getApplicationContext(), "Connection to remote Native Service is broken", Toast.LENGTH_LONG).show();
			return false;
		}
		
		public boolean stopServer(){
			if(mINativeService!=null){
				try {
					return mINativeService.stopServer();
				} catch (RemoteException e) {
					Toast.makeText(getApplicationContext(), "Remote exception in calling Native Service stopServer", Toast.LENGTH_LONG).show();
					
				}
			}
			Toast.makeText(getApplicationContext(), "Connection to remote Native Service is broken", Toast.LENGTH_LONG).show();
			return false;
		}
	}
}

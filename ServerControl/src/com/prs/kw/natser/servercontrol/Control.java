package com.prs.kw.natser.servercontrol;

import com.prs.kw.natser.servercontrol.R;
import com.prs.kw.natser.servercontrol.NatSerApp.NativeServiceManager;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Control extends Activity {
	
	Button mCtrlBtn;
	EditText mPortEt;
	TextView mStatusTv;
	TextView mIpTv;
	NativeServiceManager mNatSerMan;
	boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        mCtrlBtn = (Button) findViewById(R.id.ctrl_btn);
        mPortEt = (EditText) findViewById(R.id.port_et);
        mStatusTv = (TextView) findViewById(R.id.status_tv);
        mIpTv = (TextView) findViewById(R.id.ip_tv);
        mCtrlBtn.setOnClickListener(mBtnClickListener);
        
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        mIpTv.setText(mIpTv.getText() + ip);
        
        mNatSerMan = NatSerApp.getNativeServiceManager();
    }
    
    View.OnClickListener mBtnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String portStr = mPortEt.getText().toString();
			if(!started){
				if(!portStr.isEmpty()){
					started = mNatSerMan.startServer(Integer.parseInt(portStr));
					if(started)
						mStatusTv.setText("Started");
					else
						mStatusTv.setText("Stopped");
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a port number", Toast.LENGTH_LONG).show();
				}
			} else {
				boolean stopStatus = mNatSerMan.stopServer();
				if(stopStatus) {
					started = false;
					mStatusTv.setText("Stopped");
				}
			}
		}
	};
	
	
}

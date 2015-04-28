package com.prs.kw.natser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
		Intent i = new Intent();
		i.setClassName("com.tpv.knowhow.natser", "com.tpv.knowhow.natser.NatSer");
        context.startService(i);
        Log.d("StartServiceReceiver", "starting service...");
    }
}
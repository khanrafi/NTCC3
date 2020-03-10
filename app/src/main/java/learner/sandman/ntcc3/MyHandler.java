package learner.sandman.ntcc3;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public  class MyHandler extends Handler {
	public static MyHandler handler;
	public static MyHandler  getHandler(){
		return handler;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Log.d("TAG1","I have received messages");
	}
}

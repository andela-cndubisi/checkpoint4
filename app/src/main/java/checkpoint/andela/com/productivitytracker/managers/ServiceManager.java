package checkpoint.andela.com.productivitytracker.managers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by andela-cj on 22/10/2015.
 */
public class ServiceManager {

    private ServiceManagerDelegate managerDelegate;
    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            managerDelegate.showTime(intent);
        }
    };

    public void  setDelegate(ServiceManagerDelegate delegate){
        this.managerDelegate = delegate;
    }

    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            managerDelegate.onServiceConnected(name,service);

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public BroadcastReceiver getBroadCastReceiver (){
        return mBroadCastReceiver;
    }

    public ServiceConnection getServiceConnection(){
        return mConnection;
    }

    public interface ServiceManagerDelegate{
        void showTime(Intent intent);
        void onServiceConnected(ComponentName name, IBinder service);
    }
}

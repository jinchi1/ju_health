package globesoft.ju_heath.FCM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by system777 on 2018-02-07.
 */
public abstract class MyReceiver extends BroadcastReceiver {
    public boolean isRegistered;

    /**
     * register receiver
     * @param context - Context
     * @param filter - Intent Filter
     * @return see Context.registerReceiver(BroadcastReceiver,IntentFilter)
     */
    public Intent register(Context context, IntentFilter filter) {
        try {
            // ceph3us note:
            // here I propose to create
            // a isRegistered(Contex) method
            // as you can register receiver on different context
            // so you need to match against the same one :)
            // example  by storing a list of weak references
            // see LoadedApk.class - receiver dispatcher
            // its and ArrayMap there for example
            return !isRegistered
                    ? context.registerReceiver(this, filter)
                    : null;
        } finally {
            isRegistered = true;
        }
    }

    /**
     * unregister received
     * @param context - context
     * @return true if was registered else false
     */
    public boolean unregister(Context context) {
        // additional work match on context before unregister
        // eg store weak ref in register then compare in unregister
        // if match same instance
        return isRegistered
                && unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;
        return true;
    }

    // rest implementation  here
    // or make this an abstract class as template :)

}
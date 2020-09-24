package com.dee.rentalmanagement.WearOS;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReciver {
    public class WearActionReceiver extends BroadcastReceiver {

        public static final String NOTIFICATION_ID_STRING = "NotificationId";
        public static final String WEAR_ACTION = "WearAction";
        public static final int SNOOZE_NOTIFICATION = 1;
        public static final int CANCEL_TICKET = 2;

        @Override
        public void onReceive (Context context, Intent intent) {

            if (intent != null) {

                int notificationId = intent.getIntExtra(NOTIFICATION_ID_STRING, 0);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationId);

                int action = intent.getIntExtra(WEAR_ACTION, 0);

                switch (action) {
                    case SNOOZE_NOTIFICATION:
                        //Code for notification snooze
                        break;
                    case CANCEL_TICKET:
                        //code for removing the user from the queue
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

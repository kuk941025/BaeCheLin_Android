package com.leaderpower.baechelin_owner_android.fcm;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.activity.FCMActivity;
import com.leaderpower.baechelin_owner_android.activity.LoginActivity;
import com.leaderpower.baechelin_owner_android.activity.MainActivity;
import com.leaderpower.baechelin_owner_android.activity.SettingActivity;
import com.leaderpower.baechelin_owner_android.dialog.OrderReceivedDialog;
import com.leaderpower.baechelin_owner_android.dialog.RejectOrderDialog;

import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String shop_name = data.get("shop_name");

            String channelId = "channel";
            String channelName = "Channel Name";

            NotificationManager notifManager
                    = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notifManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), channelId);


            Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            int requestID = (int) System.currentTimeMillis();

            PendingIntent pendingIntent
                    = PendingIntent.getActivity(getApplicationContext()
                    , requestID
                    , notificationIntent
                    , PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle("새로운 주문") // required
                    .setContentText(shop_name + "에 새로운 주문이 들어왔습니다.")  // required
                    .setDefaults(android.app.Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                    .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                    .setSound(RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources()
                            , R.drawable.baechelin))
                    .setBadgeIconType(R.drawable.baechelin)
                    .setContentIntent(pendingIntent);

            notifManager.notify((int) System.currentTimeMillis(), builder.build());


//            AlertDialog.Builder testBuilder = new AlertDialog.Builder(this);
//            testBuilder.setTitle("hey").setMessage("messagE").sho


            Intent intent = new Intent(this, FCMActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


    }
}

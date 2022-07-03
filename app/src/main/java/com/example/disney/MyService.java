package com.example.disney;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {
   private MediaPlayer player;
   public MyService() {
   }

   @Override
   public void onCreate() {
      super.onCreate();
      this.player = MediaPlayer.create(this, R.raw.bb);
      this.player.setLooping(true);
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      this.player.start();
      return super.onStartCommand(intent, flags, startId);
   }

   @Override
   public void onDestroy() {
      this.player.stop();
      super.onDestroy();
   }

   @Override
   public IBinder onBind(Intent intent) {
      // TODO: Return the communication channel to the service.
      throw new UnsupportedOperationException("Not yet implemented");
   }
}
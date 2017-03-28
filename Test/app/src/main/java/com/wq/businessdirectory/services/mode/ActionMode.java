package com.wq.businessdirectory.services.mode;

import android.content.Intent;

/**
 * Created by WQ on 2017/3/28.
 */

public interface ActionMode {
     void execute(Intent intent);
     public static void execute(Intent intent,Class<? extends ActionMode> clazz){
          if(clazz!=null){
               try {
                    clazz.newInstance().execute(intent);
               } catch (InstantiationException e) {
                    e.printStackTrace();
               } catch (IllegalAccessException e) {
                    e.printStackTrace();
               }
          }
     }
}

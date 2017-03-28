package com.wq.businessdirectory.services.mode;

import android.content.Intent;

/**
 * Created by WQ on 2017/3/28.
 */

public interface ActionMode {
     void execute(Intent intent);
     void close();
}

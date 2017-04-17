package com.wq.businessdirectory.common.db.mode;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by WQ on 2017/4/17.
 */

public class BaseRealmBean extends RealmObject implements RealmSerializable {
    @PrimaryKey
    protected int _id;
    protected long add_time=System.currentTimeMillis();
    protected long update_time;
    protected boolean is_delete;
    @Override
    public int getID() {
        return _id;
    }


    public void saveSelf() {
        Realm mRealm = Realm.getDefaultInstance();
        if (_id == 0) {//没有id则尝试去生成一个
            int currentId = 1;
            RealmResults<? extends BaseRealmBean> tmpBean = mRealm.where(this.getClass()).findAllSorted("_id", Sort.DESCENDING);
            if (tmpBean.size() != 0) {
                currentId = tmpBean.get(0).getID() + 1;
            }
            _id = currentId;
        }
        update_time=System.currentTimeMillis();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(this);
        mRealm.commitTransaction();
    }
    public void deleteSelf(){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        deleteFromRealm();
        mRealm.commitTransaction();
    }

    public long getAddTime() {
        return add_time;
    }

    public long getUpdateTime() {
        return update_time;
    }

    public boolean isDelete() {
        return is_delete;
    }

    public void setID(int _id) {
        this._id = _id;
    }
}

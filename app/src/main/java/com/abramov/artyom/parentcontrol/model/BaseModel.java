package com.abramov.artyom.parentcontrol.model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

public class BaseModel {
    private Realm mRealmInstance;

    public BaseModel() {
        mRealmInstance = Realm.getDefaultInstance();
    }

    public <E extends RealmObject> List<E> getItems(Class<E> clazz) {
        mRealmInstance.beginTransaction();
        List<E> items = mRealmInstance.copyFromRealm(mRealmInstance.where(clazz).findAll());
        mRealmInstance.commitTransaction();

        return items;
    }

    public <E extends RealmObject> Observable<List<E>> getItemsObservable(Class<E> clazz) {
        return mRealmInstance.where(clazz)
                .findAll()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .map(items -> mRealmInstance.copyFromRealm(items));
    }

    public <E extends RealmObject> void saveItems(List<E> items) {
        if (items == null || items.size() == 0) {
            return;
        }

        mRealmInstance.beginTransaction();
        mRealmInstance.copyToRealm(items);
        mRealmInstance.commitTransaction();
    }

    public void destroy() {
        try {
            mRealmInstance.close();
            mRealmInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

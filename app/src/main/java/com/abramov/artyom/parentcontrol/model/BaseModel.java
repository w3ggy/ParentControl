package com.abramov.artyom.parentcontrol.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

public class BaseModel {

    public BaseModel() {
    }

    public <E extends RealmObject> List<E> getItems(Class<E> clazz) {
        List<E> items = new ArrayList<E>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            items = realm.copyFromRealm(realm.where(clazz).findAll());
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null && !realm.isClosed()) {
                try {
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return items;
    }

    public <E extends RealmObject> Observable<List<E>> getItemsObservable(Class<E> clazz) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(clazz)
                .findAll()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .doOnUnsubscribe(() -> {
                    try {
                        if (!realm.isClosed()) {
                            realm.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .map(realm::copyFromRealm);
    }

    public <E extends RealmObject> void saveItems(List<E> items) {
        if (items == null || items.size() == 0) {
            return;
        }

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(items);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null && !realm.isClosed()) {
                try {
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

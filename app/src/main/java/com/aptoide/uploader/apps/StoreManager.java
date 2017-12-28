package com.aptoide.uploader.apps;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

public class StoreManager {

  private final InstalledAppsProvider installedAppsProvider;
  private final StoreNameProvider storeNameProvider;
  private final UploadManager uploadManager;
  private final LanguageManager languageManager;

  public StoreManager(InstalledAppsProvider installedAppsProvider, StoreNameProvider storeNameProvider,
      UploadManager uploadManager, LanguageManager languageManager) {
    this.installedAppsProvider = installedAppsProvider;
    this.storeNameProvider = storeNameProvider;
    this.uploadManager = uploadManager;
    this.languageManager = languageManager;
  }

  public Single<Store> getStore() {
    return Single.zip(getNonSystemApps(), storeNameProvider.getStoreName(),
        (apps, storeName) -> new Store(storeName, apps));
  }

  public Completable upload(List<InstalledApp> apps) {
    return storeNameProvider.getStoreName()
        .flatMapCompletable(storeName -> languageManager.getCurrentLanguageCode()
            .flatMapCompletable(languageCode -> Observable.fromIterable(apps)
                .flatMapCompletable(app -> uploadManager.upload(storeName, languageCode, app))));
  }

  private Single<List<InstalledApp>> getNonSystemApps() {
    return installedAppsProvider.getInstalledApps()
        .flatMapObservable(apps -> Observable.fromIterable(apps))
        .filter(app -> !app.isSystem())
        .toList();
  }
}
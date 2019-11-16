package ru.sovcombank.iotmerahackathon.transformers

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PresentationSingleTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
    }
}

class PresentationObservableTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
    }
}

class PresentationCompletableTransformer() : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
    }
}
package com.boreal.commonutils.utils;


import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()
    private val changesWithoutObservers = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        if(changesWithoutObservers.get()){
            observers.forEach { it.newValue() }
            changesWithoutObservers.set(false)
        }
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove(observer as? ObserverWrapper<in T>)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        if(observers.isEmpty()){
            changesWithoutObservers.set(true)
        } else {
            changesWithoutObservers.set(false)
            observers.forEach { it.newValue() }
        }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}
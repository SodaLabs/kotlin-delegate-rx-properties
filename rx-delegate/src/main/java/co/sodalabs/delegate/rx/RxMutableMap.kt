// Copyright Sep 2018-present SodaLabs
//
// Author: tc@sodalabs.co
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
// DEALINGS IN THE SOFTWARE.

package co.sodalabs.delegate.rx

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RxMutableMap<K : Any, V : Any>(actual: MutableMap<K, V> = mutableMapOf())
    : ReadWriteProperty<Any, MutableMap<K, V>>{

    private val lock = Any()

    @Volatile
    private var actualWrapper = RxMutableMapWrapper(actual = actual)

    val itemAdded: Observable<Pair<K, V>> get() = actualWrapper.itemAdded
    val itemRemoved: Observable<Pair<K, V>> get() = actualWrapper.itemRemoved

    private val changedSignal = BehaviorRelay.createDefault(actualWrapper as MutableMap<K, V>).toSerialized()
    val changed: Observable<MutableMap<K, V>> get() = changedSignal.hide()

    override fun setValue(thisRef: Any,
                          property: KProperty<*>,
                          value: MutableMap<K, V>) {
        val newOne = synchronized(lock) {
            actualWrapper = RxMutableMapWrapper(actual = value)
            actualWrapper
        }
        changedSignal.accept(newOne)
    }

    override fun getValue(thisRef: Any,
                          property: KProperty<*>): MutableMap<K, V> {
        return synchronized(lock) { actualWrapper }
    }

}

// Copyright Sep 2018-present SodaLabs & CardinalBlue
//
// Author: jaime@cardinalblue.com
//         tc@sodalabs.co
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

class RxValue<T : Any>(default: T) : ReadWriteProperty<Any, T> {

    private val lock = Any()

    private val signal = BehaviorRelay.createDefault(default)

    val changed: Observable<T>? get() = signal.hide()

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return synchronized(lock) { signal.value!! }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        synchronized(lock) {
            signal.accept(value)
        }
    }
}

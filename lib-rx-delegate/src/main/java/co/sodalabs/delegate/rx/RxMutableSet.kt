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
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RxMutableSet<T : Any>(actual: MutableSet<T>) : ReadOnlyProperty<Any, MutableSet<T>>{

    private val actualWrapper = RxSetWrapper(actual = actual)

    val itemAdded: Observable<T>? get() = actualWrapper.addedSignal.hide()
    val itemRemoved: Observable<T>? get() = actualWrapper.removedSignal.hide()

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableSet<T> {
        return actualWrapper
    }

    class RxSetWrapper<T>(private val actual: MutableSet<T>) : MutableSet<T> {

        val addedSignal = BehaviorRelay.create<T>()!!
        val removedSignal = BehaviorRelay.create<T>()!!

        private val lock = Any()

        override val size: Int
            get() = synchronized(lock) { actual.size }

        override fun contains(element: T): Boolean {
            return synchronized(lock) {
                actual.contains(element)
            }
        }

        override fun containsAll(elements: Collection<T>): Boolean {
            return synchronized(lock) {
                actual.containsAll(elements)
            }
        }

        override fun isEmpty(): Boolean {
            return synchronized(lock) {
                actual.isEmpty()
            }
        }

        override fun iterator(): MutableIterator<T> {
            // TODO: Fix the addition and removal observable
            return actual.iterator()
        }

        override fun add(element: T): Boolean {
            return synchronized(lock) {
                if (actual.add(element)) {
                    addedSignal.accept(element)
                    true
                } else {
                    false
                }
            }
        }

        override fun addAll(elements: Collection<T>): Boolean {
            return synchronized(lock) {
                if (actual.addAll(elements)) {
                    elements.forEach { addedSignal.accept(it) }
                    true
                } else {
                    false
                }
            }
        }

        override fun clear() {
            synchronized(lock) {
                val items = actual.toList()
                actual.clear()
                items.forEach { removedSignal.accept(it) }
            }
        }

        override fun remove(element: T): Boolean {
            return synchronized(lock) {
                if (actual.remove(element)) {
                    removedSignal.accept(element)
                    true
                } else {
                    false
                }
            }
        }

        override fun removeAll(elements: Collection<T>): Boolean {
            return synchronized(lock) {
                if (actual.removeAll(elements)) {
                    elements.forEach { removedSignal.accept(it) }
                    true
                } else {
                    false
                }
            }
        }

        override fun retainAll(elements: Collection<T>): Boolean {
            // TODO: Fix the addition and removal observable
            return actual.retainAll(elements)
        }
    }
}

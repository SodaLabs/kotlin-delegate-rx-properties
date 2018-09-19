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

package co.sodalabs

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

/**
 * Encapsulate [MutableSet] and add ReactiveX observable functionality in the
 * wrapper class.
 */
class RxMutableSetWrapper<T>(private val actual: MutableSet<T>) : MutableSet<T> {

    private val addedSignal = BehaviorRelay.create<T>()?.toSerialized()!!
    private val removedSignal = BehaviorRelay.create<T>()?.toSerialized()!!

    val itemAdded: Observable<T> get() = addedSignal.hide()
    val itemRemoved: Observable<T> get() = removedSignal.hide()

    override val size: Int
        get() = actual.size

    override fun contains(element: T): Boolean {
        return actual.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return actual.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return actual.isEmpty()
    }

    override fun iterator(): MutableIterator<T> {
        // TODO: Fix the addition and removal observable
        return actual.iterator()
    }

    override fun add(element: T): Boolean {
        return if (actual.add(element)) {
            addedSignal.accept(element)
            true
        } else {
            false
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return if (actual.addAll(elements)) {
            elements.forEach { addedSignal.accept(it) }
            true
        } else {
            false
        }
    }

    override fun clear() {
        val items = actual.toList()
        actual.clear()
        items.forEach { removedSignal.accept(it) }
    }

    override fun remove(element: T): Boolean {
        return if (actual.remove(element)) {
            removedSignal.accept(element)
            true
        } else {
            false
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return if (actual.removeAll(elements)) {
            elements.forEach { removedSignal.accept(it) }
            true
        } else {
            false
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        // TODO: Fix the addition and removal observable
        return actual.retainAll(elements)
    }
}

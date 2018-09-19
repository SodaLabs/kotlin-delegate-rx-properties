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

class RxMutableMapWrapper<K, V>(private val actual: MutableMap<K, V>) : MutableMap<K, V> {

    private val addedSignal = BehaviorRelay.create<Pair<K, V>>()?.toSerialized()!!
    private val removedSignal = BehaviorRelay.create<Pair<K, V>>()?.toSerialized()!!

    val itemAdded: Observable<Pair<K, V>> get() = addedSignal.hide()
    val itemRemoved: Observable<Pair<K, V>> get() = removedSignal.hide()

    override val size: Int
        get() = actual.size

    override fun containsKey(key: K): Boolean {
        return actual.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return actual.containsValue(value)
    }

    override fun get(key: K): V? {
        return actual[key]
    }

    override fun isEmpty(): Boolean {
        return actual.isEmpty()
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = actual.entries
    override val keys: MutableSet<K>
        get() = actual.keys
    override val values: MutableCollection<V>
        get() = actual.values

    override fun clear() {
        val removed = actual.toList()

        actual.clear()

        removed.forEach { removedSignal.accept(it) }
    }

    override fun put(key: K, value: V): V? {
        val previous = actual.put(key, value)

        previous?.let { removedSignal.accept(Pair(key, it)) }
        addedSignal.accept(Pair(key, value))

        return previous
    }

    override fun putAll(from: Map<out K, V>) {
        actual.putAll(from)
        from.forEach { k, v -> addedSignal.accept(Pair(k, v)) }
    }

    override fun remove(key: K): V? {
        val previous = actual.remove(key)

        previous?.let { removedSignal.accept(Pair(key, it)) }

        return previous
    }
}

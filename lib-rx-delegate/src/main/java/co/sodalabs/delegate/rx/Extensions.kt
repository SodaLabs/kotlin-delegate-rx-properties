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

import io.reactivex.Observable
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
fun <T : Any> KProperty0<T>.changed(): Observable<T> {
    // Use "isAccessible = true" to make the property accessible
    isAccessible = true

    val delegate = this.getDelegate() as? RxValue<T>

    return delegate?.changed ?: Observable.just(this as T)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KProperty0<MutableSet<T>>.itemAdded(): Observable<T> {
    // Use "isAccessible = true" to make the property accessible
    isAccessible = true

    val delegate = this.getDelegate() as? RxMutableSet<T>

    return delegate?.itemAdded ?: Observable.empty()
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KProperty0<MutableSet<T>>.itemRemoved(): Observable<T> {
    // Use "isAccessible = true" to make the property accessible
    isAccessible = true

    val delegate = this.getDelegate() as? RxMutableSet<T>

    return delegate?.itemRemoved ?: Observable.empty()
}

@Suppress("UNCHECKED_CAST")
fun <K : Any, V : Any> KProperty0<MutableMap<K, V>>.tupleAdded(): Observable<Pair<K, V>> {
    // Use "isAccessible = true" to make the property accessible
    isAccessible = true

    val delegate = this.getDelegate() as? RxMutableMap<K, V>

    return delegate?.itemAdded ?: Observable.empty()
}

@Suppress("UNCHECKED_CAST")
fun <K : Any, V : Any> KProperty0<MutableMap<K, V>>.tupleRemoved(): Observable<Pair<K, V>> {
    // Use "isAccessible = true" to make the property accessible
    isAccessible = true

    val delegate = this.getDelegate() as? RxMutableMap<K, V>

    return delegate?.itemRemoved ?: Observable.empty()
}

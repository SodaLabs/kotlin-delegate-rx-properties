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

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class RxMutableSetTest {

    private val mutableSetActual by RxMutableSet(mutableSetOf<Int>())

    @Test
    fun `observe item addition`() {
        // Reflect the prop$delegate
        val tester = this::mutableSetActual
            .itemAdded()
            .test()

        mutableSetActual.add(5)
        mutableSetActual.add(6)
        mutableSetActual.add(7)

        System.out.println("size=${mutableSetActual.size}")

        tester.assertValueCount(3)
    }

    @Test
    fun `observe item removal`() {
        // Reflect the prop$delegate
        val tester = this::mutableSetActual
            .itemRemoved()
            .test()

        mutableSetActual.add(5)
        mutableSetActual.add(6)
        mutableSetActual.add(7)
        mutableSetActual.remove(5)
        mutableSetActual.remove(6)
        mutableSetActual.remove(7)

        System.out.println("size=${mutableSetActual.size}")

        tester.assertValueCount(3)
    }
}

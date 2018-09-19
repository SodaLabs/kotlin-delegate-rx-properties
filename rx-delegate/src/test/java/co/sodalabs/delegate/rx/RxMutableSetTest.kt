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

import co.sodalabs.changed
import co.sodalabs.itemAdded
import co.sodalabs.itemRemoved
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class RxMutableSetTest {

    private var mutableSet by RxMutableSet(mutableSetOf<Int>())

    @Test
    fun `observe value replaced`() {
        // Reflect the prop$delegate
        val tester = this::mutableSet
            .changed()
            .test()

        mutableSet = mutableSetOf(5, 6)

        System.out.println("size=${mutableSet.size}")

        Assert.assertEquals(2, mutableSet.size)
        tester.assertValueCount(2)
    }

    @Test
    fun `observe item added`() {
        // Reflect the prop$delegate
        val tester = this::mutableSet
            .itemAdded()
            .test()

        mutableSet.add(5)
        mutableSet.add(6)
        mutableSet.add(7)

        System.out.println("size=${mutableSet.size}")

        tester.assertValueCount(3)
    }

    @Test
    fun `observe item removed`() {
        // Reflect the prop$delegate
        val tester = this::mutableSet
            .itemRemoved()
            .test()

        mutableSet.add(5)
        mutableSet.add(6)
        mutableSet.add(7)
        mutableSet.remove(5)
        mutableSet.remove(6)
        mutableSet.remove(7)

        System.out.println("size=${mutableSet.size}")

        tester.assertValueCount(3)
    }
}

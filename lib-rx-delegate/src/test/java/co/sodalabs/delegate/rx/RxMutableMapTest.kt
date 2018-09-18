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

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class RxMutableMapTest {

    private var mutableMap by RxMutableMap(mutableMapOf<Int, String>())

    @Test
    fun `observe value replacement`() {
        // Reflect the prop$delegate
        val tester = this::mutableMap
            .changed()
            .test()

        mutableMap = mutableMapOf(Pair(0, "000"),
                                  Pair(1, "111"))

        Assert.assertEquals(2, mutableMap.size)
        tester.assertValueCount(1)
    }

    @Test
    fun `observe item added`() {
        // Reflect the prop$delegate
        val tester = this::mutableMap
            .tupleAdded()
            .test()

        mutableMap[5] = "555"
        mutableMap[6] = "666"
        mutableMap[7] = "777"

        System.out.println("size=${mutableMap.size}")

        Assert.assertEquals(3, mutableMap.size)
        tester.assertValueCount(3)
    }

    @Test
    fun `observe item removed`() {
        // Reflect the prop$delegate
        val tester = this::mutableMap
            .tupleRemoved()
            .test()

        mutableMap[5] = "555"
        mutableMap[6] = "666"
        mutableMap[7] = "777"
        mutableMap.remove(5)
        mutableMap.remove(6)
        mutableMap.remove(7)

        System.out.println("size=${mutableMap.size}")

        Assert.assertEquals(0, mutableMap.size)
        tester.assertValueCount(3)
    }

    @Test
    fun `observe item replaced`() {
        // Reflect the prop$delegate
        val additionTester = this::mutableMap
            .tupleAdded()
            .test()
        val removalTester = this::mutableMap
            .tupleRemoved()
            .test()

        mutableMap[5] = "555"
        mutableMap[5] = "555-new"

        System.out.println("size=${mutableMap.size}")

        Assert.assertEquals(1, mutableMap.size)
        additionTester.assertValueCount(2)
        removalTester.assertValueCount(1)
    }
}

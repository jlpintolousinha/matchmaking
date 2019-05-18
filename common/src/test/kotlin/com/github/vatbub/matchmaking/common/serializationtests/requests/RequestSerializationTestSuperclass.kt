/*-
 * #%L
 * matchmaking.common
 * %%
 * Copyright (C) 2016 - 2019 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.vatbub.matchmaking.common.serializationtests.requests

import com.github.vatbub.matchmaking.common.Request
import com.github.vatbub.matchmaking.common.serializationtests.ServerInteractionSerializationTestSuperclass
import com.github.vatbub.matchmaking.testutils.TestUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

abstract class RequestSerializationTestSuperclass<T : Request>(clazz: Class<T>) : ServerInteractionSerializationTestSuperclass<T>(clazz) {
    @Test
    fun copyTest() {
        val request = newObjectUnderTest()
        val copy = request.copy()
        Assertions.assertEquals(request, copy)
        Assertions.assertNotSame(request, copy)
    }

    @Test
    fun connectionIdNotEqualTest() {
        val request1 = newObjectUnderTest()
        val request2 = Request(TestUtils.getRandomHexString(request1.connectionId), request1.password, request1.className, request1.requestId)
        Assertions.assertNotEquals(request1, request2)
    }

    @Test
    fun classNameNotEqualTest() {
        val request1 = newObjectUnderTest()
        val request2 = Request(request1.connectionId, request1.password, TestUtils.getRandomHexString(request1.className), request1.requestId)
        Assertions.assertNotEquals(request1, request2)
    }

    @Test
    fun requestIdNotEqualTest() {
        val request1 = newObjectUnderTest()
        val request2 = Request(request1.connectionId, request1.password, request1.className, TestUtils.getRandomHexString(request1.requestId))
        Assertions.assertNotEquals(request1, request2)
    }
}

class RequestSerializationTest : RequestSerializationTestSuperclass<Request>(Request::class.java) {
    override fun newObjectUnderTest() = Request(TestUtils.defaultConnectionId, TestUtils.defaultPassword, Request::class.qualifiedName!!, TestUtils.getRandomHexString())

    // Already tested in the super class
    override fun notEqualsTest() {}
}
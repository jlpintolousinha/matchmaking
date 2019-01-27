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
package com.github.vatbub.matchmaking.common.serializationtests

import com.github.vatbub.matchmaking.testutils.KotlinTestSuperclass
import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.jupiter.api.Test
import kotlin.random.Random

abstract class SerializationTestSuperclass<T : Any>(private val clazz: Class<T>) :
    KotlinTestSuperclass() {
    abstract fun newObjectUnderTest(): T

    val defaultConnectionId = getRandomHexString()
    val defaultPassword = getRandomHexString()

    fun getRandomHexString(): String {
        return Random.nextInt().toString(16)
    }

    @Test
    fun serializationTest() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val originalObject = newObjectUnderTest()
        val json = gson.toJson(originalObject)
        println("Generated json:\n$json")
        val deserializedObject: T = gson.fromJson<T>(json, clazz)
        Assert.assertEquals(originalObject, deserializedObject)
        Assert.assertEquals(originalObject.hashCode(), deserializedObject.hashCode())
    }
}

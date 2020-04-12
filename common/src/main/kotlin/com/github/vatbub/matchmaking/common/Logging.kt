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
package com.github.vatbub.matchmaking.common

import com.esotericsoftware.minlog.Log
import mu.KLogger
import mu.KotlinLogging
import org.slf4j.LoggerFactory

private object GetLoggerLock
private object RedirectInitializerLock

private var minLogRedirectInitialized = false

val Any.logger
    get() = KotlinLogging.logger(LoggerFactory.getLogger(this.javaClass)!!)

private val loggerMap = mutableMapOf<String, KLogger>()

fun logger(name: String): KLogger {
    val cachedLogger1 = loggerMap[name]
    if (cachedLogger1 != null) return cachedLogger1
    synchronized(GetLoggerLock) {
        val cachedLogger2 = loggerMap[name]
        if (cachedLogger2 != null) return cachedLogger2
        val logger = KotlinLogging.logger(name)
        loggerMap[name] = logger
        return logger
    }
}

private class MinLogRedirect : Log.Logger() {
    override fun log(level: Int, category: String?, message: String?, ex: Throwable?) {
        val finalCategory = category ?: "unknown category"

        with(logger(finalCategory)) {
            when (level) {
                Log.LEVEL_TRACE -> trace(ex) { message }
                Log.LEVEL_DEBUG -> debug(ex) { message }
                Log.LEVEL_INFO -> info(ex) { message }
                Log.LEVEL_WARN -> warn(ex) { message }
                Log.LEVEL_ERROR -> error(ex) { message }
            }
        }
    }
}

fun initializeMinLogRedirect() {
    if (minLogRedirectInitialized) return
    synchronized(RedirectInitializerLock) {
        if (minLogRedirectInitialized) return
        Log.setLogger(MinLogRedirect())
        minLogRedirectInitialized = true
    }
}

/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (c) 2020 Jonathan Bisson
 */

package net.nprod.wikidataLotusExporter.helpers

import kotlin.reflect.KClass

typealias Milliseconds = Long
/**
 * This is used to retry a block of code a number of times if it files with the given exceptions.
 * Once it reaches the maximum of retries, it will throw the last exception received.
 */
// On purpose we catch and throw it back
// We also remove the NestedBlockDepth as it is a helper function
@Suppress("TooGenericExceptionThrown", "TooGenericExceptionCaught", "NestedBlockDepth")
inline fun <U> tryCount(
    listExceptions: List<KClass<out Exception>>,
    maxRetries: Int = 3,
    delayMilliSeconds: Milliseconds = 0,
    f: () -> U
): U {
    var retries = 0

    while ((retries < maxRetries)) {
        try {
            return f()
        } catch (e: Exception) {
            if (listExceptions.any { e::class == it }) {
                retries += 1
                if (retries != maxRetries) {
                    if (delayMilliSeconds > 0) Thread.sleep(delayMilliSeconds)
                    continue
                }
            }
            throw e
        }
    }

    throw RuntimeException("This is not accessible")
}

// SPDX-License-Identifier: AGPL-3.0-or-later
/**
 * Copyright (c) 2020 Jonathan Bisson.  All rights reserved.
 */

package net.nprod.wikidataLotusExporter

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import net.nprod.wikidataLotusExporter.modes.mirror.mirror
import org.eclipse.rdf4j.model.IRI
import org.slf4j.LoggerFactory
import java.io.File

fun IRI.getIDfromIRI(): String = this.stringValue().split("/").last()

@ExperimentalCli
fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("net.nprod.wikidataLotusExporter")

    val parser = ArgParser("lotus_exporter")
    val store by parser.option(
        ArgType.String, "store", "s", "Where the data is going to be stored"
    ).default("data/local_rdf")

    var commandRun = false

    class Mirror : Subcommand("mirror", "Mirror Wikidata entries related to LOTUS locally") {
        override fun execute() {
            val storeFile = File(store)
            storeFile.mkdirs()
            logger.info("Starting in mirroring mode into the repository: $store")
            mirror(storeFile)
            commandRun = true
        }
    }

    parser.subcommands(Mirror())
    parser.parse(args)

    if (!commandRun) logger.error("Please use at least one of the commands, check the help: -h")
    logger.info("We are done, go Kotlin \\o/")
}

package net.nprod.wikidataLotusExporter.rdf

import org.eclipse.rdf4j.repository.sail.SailRepository
import org.eclipse.rdf4j.sail.nativerdf.NativeStore
import org.slf4j.LoggerFactory
import java.io.File

/**
 * A local RDFRepository to store all the acquired SPARQL data
 */
class RDFRepository(val location: File) {
    val repository: SailRepository
    val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("Loading old data")
        val file = location.also {
            if (!it.canWrite() || !it.canRead()) throw AccessDeniedException(it)
        }
        repository = SailRepository(NativeStore(location))
    }
}
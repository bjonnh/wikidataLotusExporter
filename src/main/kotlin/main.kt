// SPDX-License-Identifier: AGPL-3.0-or-later
/**
 * Copyright (c) 2020 Jonathan Bisson.  All rights reserved.
 */

package net.nprod.wikidataLotusExporter

import net.nprod.wikidataLotusExporter.rdf.RDFRepository
import net.nprod.wikidataLotusExporter.sparql.GetTaxonPairs
import net.nprod.wikidataLotusExporter.sparql.SparqlRepository
import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Statement
import java.io.File

fun main() {
    val sparqlRepository = SparqlRepository("https://query.wikidata.org/sparql")
    val rdfRepository = RDFRepository(File("data/local_rdf"))

    val wdt = "http://www.wikidata.org/prop/direct/"

    rdfRepository.repository.connection.use { rdfConnection ->
        val vf = rdfConnection.valueFactory
        val presentInTaxon = vf.createIRI(wdt, "P703")
        val instanceOf = vf.createIRI(wdt, "P31")

        val entries = mutableListOf<Statement>()
        val getTaxonPairs = GetTaxonPairs()
        sparqlRepository.query(getTaxonPairs.queryIds) {
            it.map { bindingSet ->
                val compoundID: IRI = bindingSet.getBinding("compound_id").value as IRI
                val taxonID: IRI = bindingSet.getBinding("taxon_id").value as IRI
                val type: IRI = bindingSet.getBinding("type").value as IRI
                entries.add(vf.createStatement(compoundID, presentInTaxon, taxonID))
                entries.add(vf.createStatement(compoundID, instanceOf, type))
            }
        }
        println("Done querying the compound-taxo couples")

        entries.chunked(100).map {
            val listOfCompounds = it.map { "wd:${it.subject.stringValue().split("/").last()}" }.joinToString(" ")
            val taxoQuery = getTaxonPairs.queryTaxo.replace("%%IDS%%", listOfCompounds)
            sparqlRepository.query(taxoQuery) {
                it.map { bindingSet ->
                    val compoundID: IRI = bindingSet.getBinding("compound_id").value as IRI
                    val taxonID: IRI = bindingSet.getBinding("taxon_id").value as IRI
                    val type: IRI = bindingSet.getBinding("type").value as IRI
                    entries.add(vf.createStatement(compoundID, presentInTaxon, taxonID))
                    entries.add(vf.createStatement(compoundID, instanceOf, type))
                }
            }
        }
        println("Done querying the compounds")
        rdfConnection.add(entries)

        println(rdfConnection.size())
    }
}

/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (c) 2021
 */

package net.nprod.wikidataLotusExporter.modes.export.types

import net.nprod.wikidataLotusExporter.lotus.models.Compound
import net.nprod.wikidataLotusExporter.rdf.vocabulary.WikidataChemistry
import org.eclipse.rdf4j.IsolationLevels
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.RepositoryConnection

fun doWithEachCompound(repository: Repository, f: (Compound) -> Unit) {
    repository.connection.use { conn: RepositoryConnection ->
        conn.begin(IsolationLevels.NONE) // We are not writing anything
        conn.prepareTupleQuery(
            """
            SELECT ?compound_id ?inchikey ?inchi ?canonicalSmiles ?isomericSmiles {
              ?compound_id <${WikidataChemistry.Properties.inchiKey}> ?inchikey.
              OPTIONAL { ?compound_id <${WikidataChemistry.Properties.inchi}> ?inchi. }
              OPTIONAL { ?compound_id <${WikidataChemistry.Properties.canonicalSmiles}> ?canonicalSmiles. }
              OPTIONAL { ?compound_id <${WikidataChemistry.Properties.isomericSmiles}> ?isomericSmiles. }
            }
            """.trimIndent()
        ).evaluate().groupBy { it.getValue("compound_id").stringValue() }.forEach { (key, value) ->
            f(
                Compound(
                    wikidataId = key,
                    inchiKeys = value.map { it.getValue("inchikey").stringValue() },
                    inchis = value.mapNotNull { it.getValue("inchi")?.stringValue() },
                    canonicalSmiles = value.mapNotNull { it.getValue("canonicalSmiles")?.stringValue() },
                    isomericSmiles = value.mapNotNull { it.getValue("isomericSmiles")?.stringValue() },
                )
            )
        }
        conn.commit()
    }
}

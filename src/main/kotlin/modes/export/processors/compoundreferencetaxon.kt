/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (c) 2021
 */
package net.nprod.wikidataLotusExporter.modes.export.types

import net.nprod.wikidataLotusExporter.lotus.models.CompoundReferenceTaxon
import net.nprod.wikidataLotusExporter.sparql.LOTUSQueries
import org.eclipse.rdf4j.IsolationLevels
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.RepositoryConnection

val queryForCompoundsRefsAndTaxa =
    """
  ${LOTUSQueries.prefixes}
  SELECT DISTINCT ?compound_id ?taxon_id ?reference_id
  WHERE {
    ?compound_id     p:P703 ?pp703.
    ?pp703           ps:P703 ?taxon_id;
                     prov:wasDerivedFrom/pr:P248 ?reference_id.
  }
    """.trimIndent()

fun doWithEachCompoundReferenceTaxon(
    repository: Repository,
    f: (CompoundReferenceTaxon) -> Unit
) {
    repository.connection.use { conn: RepositoryConnection ->
        conn.begin(IsolationLevels.NONE) // We are not writing anything

        conn.prepareTupleQuery(queryForCompoundsRefsAndTaxa).evaluate().map {
            val compoundId = it.getValue("compound_id").stringValue()
            val taxonId = it.getValue("taxon_id").stringValue()
            val referenceId = it.getValue("reference_id").stringValue()
            f(CompoundReferenceTaxon(compoundId, taxonId, referenceId))
        }
        conn.commit()
    }
}

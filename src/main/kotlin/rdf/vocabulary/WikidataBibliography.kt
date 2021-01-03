/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (c) 2021
 */

package net.nprod.wikidataLotusExporter.rdf.vocabulary

object WikidataBibliography {
    object Properties {
        val doi = Wikidata.wdt("P356")
    }

    val scholarlyArticle = Wikidata.wd("Q13442814")
    val scholarlyPublication = Wikidata.wd("Q591041")
    val article = Wikidata.wd("Q191067")
    val publication = Wikidata.wd("Q732577")
}

package net.nprod.wikidataLotusExporter.sparql

import org.slf4j.LoggerFactory

object LOTUSQueries {
    /**
     * Import all the wikidata taxa into a TDB2 database
     * it will ADD to the existing database, so you probably
     * want to delete the database first and recreate it
     * just in case some entries were changed or deleted.
     */

    val prefixes = """
PREFIX wd: <http://www.wikidata.org/entity/>
PREFIX wdt: <http://www.wikidata.org/prop/direct/>
PREFIX wikibase: <http://wikiba.se/ontology#>
PREFIX p: <http://www.wikidata.org/prop/>
PREFIX prov: <http://www.w3.org/ns/prov#>
PREFIX ps: <http://www.wikidata.org/prop/statement/>
PREFIX pq: <http://www.wikidata.org/prop/qualifier/>
PREFIX pr: <http://www.wikidata.org/prop/reference/>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bd: <http://www.bigdata.com/rdf#> 
""".trimIndent()

    val queryCompoundTaxonRef = """$prefixes
CONSTRUCT {
  ?compound_id wdt:P31 ?type;
               wdt:P703 ?taxon_id;
               p:P703 ?p703.
  ?p703 prov:wasDerivedFrom ?derived.
  ?derived pr:P248 ?reference_id.
}
WHERE {
VALUES ?type { wd:Q43460564 wd:Q59199015 } # chemical entity or group of stereoisomers 
  ?compound_id wdt:P31 ?type; 
      wdt:P703 ?taxon_id;
      p:P703 ?p703.
  ?p703 prov:wasDerivedFrom ?derived.
  ?derived pr:P248 ?reference_id.
}"""

    val queryIdsLocal = """$prefixes
SELECT
  ?compound_id ?taxon_id ?reference_id
WHERE {
    VALUES ?type { wd:Q43460564 wd:Q59199015 } # chemical entity or group of stereoisomers 
    ?compound_id wdt:P31 ?type; 
    wdt:P703 ?taxon_id;
    p:P703/prov:wasDerivedFrom/pr:P248 ?reference_id.
}"""

    val queryTaxonParents = """$prefixes
SELECT
  ?parenttaxon_id
WHERE {
    VALUES ?id { %%IDS%% } 
    ?id wdt:P171* ?parenttaxon_id.
}"""

    val queryTaxo = """$prefixes
SELECT
  ?id ?taxo ?canonicalSmiles ?isomericSmiles ?inchi ?inchiKey ?reference
WHERE { 
  VALUES ?id { %%IDS%% }
  ?id wdt:P31 ?thingy; 
      wdt:P703 ?taxo;
      wdt:P233 ?canonicalSmiles;
      wdt:P2017 ?isomericSmiles;
      wdt:P234 ?inchi;
      wdt:P235 ?inchiKey;
      p:P703/prov:wasDerivedFrom/pr:P248 ?reference.
}
"""

    val mirrorQuery = """$prefixes
CONSTRUCT {
  ?id ?p ?o
}
WHERE { 
  VALUES ?id { %%IDS%% }
  ?id ?p ?o.
}
"""
}
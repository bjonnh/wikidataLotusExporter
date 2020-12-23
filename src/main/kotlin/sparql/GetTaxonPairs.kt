package net.nprod.wikidataLotusExporter.sparql

import org.slf4j.LoggerFactory

class GetTaxonPairs {
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

    val queryIds = """$prefixes
SELECT
  ?compound_id ?taxon_id ?type
WHERE {
VALUES ?type { wd:Q43460564 wd:Q59199015 } # chemical entity or group of stereoisomers 
  ?compound_id wdt:P31 ?type; 
      wdt:P703 ?taxon_id. 
} LIMIT 10"""

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

    fun main() {
        val log = LoggerFactory.getLogger("wikidata.compoundsintaxon")
/*

        log.info("Creating a local TDB2 store for compounds in taxon")
        val ds = TDB2Factory.connectDataset("./data/wd_compoundsintaxon")

        log.info("Connecting to wikidata")
        val conn = RDFConnectionFactory.connect("https://query.wikidata.org/sparql")
        log.info("Making a query to get all the IDs (this should not timeout, if it does just restart)")
        val wdIds = mutableListOf<String>()
        conn.querySelect(queryIds) { qs ->
            wdIds.add(qs.getResource("id").localName)
        }
        log.info("We have ${wdIds.size} compounds that are found in a taxon")
        var count = 0
        val chunkSize = 10
        wdIds.take(20).chunked(chunkSize) { idsList ->
            val ids = idsList.joinToString(" ") { "wd:$it" }
            //ds.begin(ReadWrite.WRITE)
            //val model = ds.defaultModel
            println(queryTaxo)
            conn.querySelect(queryTaxo.replace("%%IDS%%", ids)) { qs ->
                println(qs)
            }
            //model.add(qModel)
            //ds.commit()
            //ds.end()
            count += chunkSize
            println("${100 * count / wdIds.size}%")

            return@chunked
        }
        conn.close()
        ds.close()*/
    }
}
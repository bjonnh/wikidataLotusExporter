package net.nprod.wikidataLotusExporter.sparql

import net.nprod.wikidataLotusExporter.helpers.tryCount
import org.eclipse.rdf4j.query.QueryEvaluationException
import org.eclipse.rdf4j.query.TupleQueryResult
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository

class SparqlRepository(url: String) {
    private val repository: Repository

    init {
        repository = SPARQLRepository(url)
    }

    fun <T> query(query: String, function: (TupleQueryResult) -> T): T {
        return repository.connection.use {
            tryCount<TupleQueryResult>(
                listOf(QueryEvaluationException::class),
                delayMilliSeconds = 10_000L
            ) {
                it.prepareTupleQuery(query).evaluate()
            }.use { result ->
                function(result)
            }
        }
    }
}
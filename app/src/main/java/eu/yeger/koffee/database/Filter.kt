package eu.yeger.koffee.database

/**
 * Utility class for creating and passing database [String] filters.
 *
 * @constructor
 * Creates a [Filter] from the query [String].
 *
 * @param query The query [String].
 *
 * @author Jan Müller
 */
class Filter(query: String) {
    val nameFragment = "%$query%"
}

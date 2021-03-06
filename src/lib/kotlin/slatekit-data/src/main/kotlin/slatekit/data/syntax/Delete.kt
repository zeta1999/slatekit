package slatekit.data.syntax

import slatekit.common.data.*
import slatekit.data.core.Meta

/**
 * Used to build the syntax for delete statements
 * @param info: Meta info to know about the table (name, primary key ) and model id
 * @param mapper: Mapper that converts a model T into its values for a table
 */
open class Delete<TId, T>(val info: Meta<TId, T>, val mapper: Mapper<TId, T>, val filters: Filters = Filters()) : Statement<TId, T> where TId : kotlin.Comparable<TId>, T : Any {
    /**
     * Builds the full SQL statement
     * e.g. "delete from movies where id = 1;"
     */
    open fun stmt(id: TId): String {
        val name = encode(info.pkey.name, info.table.encodeChar)
        return "${prefix()} where $name = $id;"
    }

    /**
     * Builds sql statement to remove multiple items by ids
     * e.g.
     * "delete from `movies` where id in (?);"
     */
    open fun stmt(ids:List<TId>): String {
        val name = encode(info.pkey.name, info.table.encodeChar)
        val delimited = ids.joinToString(",")
        val sql = "${prefix()} where $name in ($delimited);"
        return sql
    }

    /**
     * Builds sql statement with values as placeholders for prepared statements
     * e.g.
     * "delete from `movies` where id = ?;"
     */
    open fun prep(id: TId): Command {
        val name = encode(info.pkey.name, info.table.encodeChar)
        val sql = "${prefix()} where $name = ?;"
        return Command(sql, listOf(Value(name, id)), listOf(id))
    }

    /**
     * Builds the values to be inserted as a list of Pair(name:String, value:Any?)
     * e.g. listOf(
     *      Value("id"    , 1")
     * )
     */
    open fun data(id: TId): Values {
        val name = encode(info.pkey.name, info.table.encodeChar)
        return listOf<Value>(Value(name, id))
    }

    /**
     * Truncate the table to delete all records
     */
    open fun drop(): String = "${prefix()};"


    /**
     * builds a select based on filters
     */
    open fun filter(filters: List<Filter>, logical:Logical): Command {
        val prefix = prefix()
        val values = filters.map { Encoding.convertVal(it.value) }
        val op = if(logical == Logical.And) "and" else "or"
        val conditions = filters.joinToString(" $op ", transform = { f ->
            this.filters.build(f.name, f.op, f.value, surround = true, placehoder = true)
        })
        val sql = "$prefix where ${conditions};"
        return Command(sql, emptyValues, values)
    }

    /**
     * basic syntax for common to both stmt/prep
     */
    fun prefix(): String = "delete from " + encode(info.name, info.table.encodeChar)


    private val emptyValues:List<Value> = listOf()
}

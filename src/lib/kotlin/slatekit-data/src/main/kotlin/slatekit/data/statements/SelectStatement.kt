package slatekit.data.statements

import slatekit.common.data.Mapper
import slatekit.common.data.Value
import slatekit.common.data.Values
import slatekit.data.core.Meta

open class SelectStatement<TId, T>(val info: Meta<TId, T>, val mapper: Mapper<TId, T>) : Statement<TId, T> where TId : kotlin.Comparable<TId>, T : Any {
    /**
     * Builds the full SQL statement
     * e.g. "select * from `movies` where id = 1;"
     */
    open fun stmt(id: TId): String {
        val name = encode(info.pkey.name, info.table.encodeChar)
        return "${prefix()} where $name = $id;"
    }

    /**
     * Builds sql statement with values as placeholders for prepared statements
     * e.g. "select * from `movies` where id = ?;"
     */
    open fun prep(id: TId): StatementData {
        val name = encode(info.pkey.name, info.table.encodeChar)
        val sql = "${prefix()} where $name = ?;"
        return StatementData(sql, listOf(Value(name, id)), listOf(id))
    }

    /**
     * Builds sql statement to get multiple items by ids
     * e.g.
     * "select * from `movies` where id in (?);"
     */
    open fun prep(ids:List<TId>): StatementData {
        val name = encode(info.pkey.name, info.table.encodeChar)
        val sql = "${prefix()} where $name in (?);"
        val delimited = ids.joinToString(",")
        return StatementData(sql, listOf(Value(name, delimited)), listOf(delimited))
    }

    /**
     * Builds the values to be inserted as a list of Pair(name:String, value:Any?)
     * e.g. listOf(
     *      Value("id", 2)
     * )
     */
    open fun data(id: TId): Values {
        val name = encode(info.pkey.name, info.table.encodeChar)
        return listOf<Value>(Value(name, id))
    }

    /**
     * Load all records
     */
    open fun load(): String = "${prefix()};"

    /**
     * basic syntax for common to both stmt/prep
     */
    fun prefix(): String = "select * from " + encode(info.name, info.table.encodeChar)

    /**
     * basic syntax getting count
     */
    fun count(): String = "select count(*) from " + encode(info.name, info.table.encodeChar)
}
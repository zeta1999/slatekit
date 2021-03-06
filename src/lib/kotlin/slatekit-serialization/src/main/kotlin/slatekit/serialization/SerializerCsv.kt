/**
 * <slate_header>
 * url: www.slatekit.com
 * git: www.github.com/code-helix/slatekit
 * org: www.codehelix.co
 * author: Kishore Reddy
 * copyright: 2016 CodeHelix Solutions Inc.
 * license: refer to website and/or github
 * about: A tool-kit, utility library and server-backend
 * mantra: Simplicity above all else
 * </slate_header>
 */

package slatekit.serialization

import slatekit.common.newline

/**
 * Created by kishorereddy on 6/3/17.
 */

class SerializerCsv(
    objectSerializer: ((Serializer, Any, Int) -> Unit)? = null,
    isoDates: Boolean = false
)
    : Serializer(objectSerializer, isoDates) {

    /**
     * handler for when a container item has started
     */
    override fun onContainerStart(item: Any, type: ParentType, depth: Int) {
    }

    /**
     * handle for when a container item has ended
     */
    override fun onContainerEnd(item: Any, type: ParentType, depth: Int) {
        if (depth <= 2) {
            buff.append(newline)
        }
    }

    override fun onMapItem(item: Any, depth: Int, pos: Int, key: String, value: Any?) {
        if (pos > 0 && depth <= 2) {
            buff.append(", ")
        }
        serializeValue(value, depth)
    }

    override fun onListItem(item: Any, depth: Int, pos: Int, value: Any?) {
        if (pos > 0 && depth <= 0) {
            buff.append(", ")
        }
        serializeValue(value, depth)
    }
}

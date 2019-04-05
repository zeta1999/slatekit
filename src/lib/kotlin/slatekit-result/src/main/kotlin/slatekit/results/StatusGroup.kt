/**
<slate_header>
url: www.slatekit.com
git: www.github.com/code-helix/slatekit
org: www.codehelix.co
author: Kishore Reddy
copyright: 2016 CodeHelix Solutions Inc.
license: refer to website and/or github
about: A Kotlin utility library, tool-kit and server backend.
philosophy: Simplicity above all else
</slate_header>
 */

package slatekit.results

/**
 * Default implementations of status codes with logical groups of [Status]
 *
 * 1. [Succeeded] : Used for any successful codes/scenarios
 * 2. [Pending]   : Success group for pending/queued results
 * 2. [Invalid]   : Err group to represent a bad request / inputs
 * 4. [Ignored]   : Err group to represent ignored / filtered cases
 * 3. [Denied]    : Err group to represent denied requests
 * 5. [Errored]   : Err group to represent any error/failure scenarios known at compile time
 * 6. [Unhandled] : Err group to represent any unhandled exceptions
 */
sealed class StatusGroup : Status {
    data class Succeeded  (override val code: Int, override val msg:String) :  StatusGroup()
    data class Pending    (override val code: Int, override val msg:String) :  StatusGroup()
    data class Denied     (override val code: Int, override val msg:String) :  StatusGroup()
    data class Ignored    (override val code: Int, override val msg:String) :  StatusGroup()
    data class Invalid    (override val code: Int, override val msg:String) :  StatusGroup()
    data class Errored    (override val code: Int, override val msg:String) :  StatusGroup()
    data class Unhandled  (override val code: Int, override val msg:String) :  StatusGroup()

    fun copyAll(msg:String, code:Int): StatusGroup {
        return when(this){
            is Succeeded -> this.copy(code = code, msg = msg)
            is Pending -> this.copy(code = code, msg = msg)
            is Denied -> this.copy(code = code, msg = msg)
            is Invalid -> this.copy(code = code, msg = msg)
            is Ignored -> this.copy(code = code, msg = msg)
            is Errored -> this.copy(code = code, msg = msg)
            is Unhandled -> this.copy(code = code, msg = msg)
        }
    }

    fun copyMsg(msg:String): StatusGroup {
        return when(this){
            is Succeeded -> this.copy(msg = msg)
            is Pending -> this.copy(msg = msg)
            is Denied -> this.copy(msg = msg)
            is Invalid -> this.copy(msg = msg)
            is Ignored -> this.copy(msg = msg)
            is Errored -> this.copy(msg = msg)
            is Unhandled -> this.copy(msg = msg)
        }
    }
}

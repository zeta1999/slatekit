package slatekit.jobs

import slatekit.common.Identity
import slatekit.common.Status

/**
 * Used to send notifications to subscribers/listeners of the job/workers on state changes.
 * E.g. running -> paused
 */
sealed class Event {
    abstract val status: Status

    /**
     * @param id: Identity of the job, see [slatekit.common.Identity]
     * @param status: Status of the job
     * @param queue: Optional name of associated queue
     */
    data class JobEvent(val id: Identity, override val status: Status, val queue:String?) : Event()

    /**
     * @param id: Identity of the worker, see [slatekit.common.Identity]
     * @param status: Status of the worker
     * @param info: Info/diagnostics provided by the worker
     */
    data class WorkerEvent(val id: Identity, override val status: Status, val info:List<Pair<String, String>>) : Event ()
}

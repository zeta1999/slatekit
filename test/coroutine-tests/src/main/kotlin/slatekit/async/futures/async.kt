/**
<slate_header>
url: www.slatekit.com
git: www.github.com/code-helix/slatekit
org: www.codehelix.co
author: Kishore Reddy
copyright: 2016 CodeHelix Solutions Inc.
license: refer to website and/or github
about: A Kotlin utility library, tool-kit and server backend.
mantra: Simplicity above all else
</slate_header>
 */
package slatekit.async.futures

import slatekit.async.AsyncContext
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService



class AsyncContextFuture(val scope:ExecutorService = Executors.newSingleThreadExecutor()): AsyncContext

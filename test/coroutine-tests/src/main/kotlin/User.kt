import slatekit.entities.core.Entity

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


data class User(override val id: Long, val name: String) : Entity<Long> {
    override fun withId(id: Long): Entity<Long> {
        return this.copy(id = id)
    }

}

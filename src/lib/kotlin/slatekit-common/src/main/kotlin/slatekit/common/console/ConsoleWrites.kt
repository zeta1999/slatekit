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

package slatekit.common.console

import slatekit.common.IO
import slatekit.common.Strings

interface ConsoleWrites {

    val settings: ConsoleSettings


    val TAB: String get() = "    "


    /**
     * IO abstraction for println.
     * Assists with testing and making code a bit more "purely functional"
     * This is a simple, custom alternative to the IO Monad.
     * Refer to IO.scala for details.
     */
    val _io: IO<Any, Unit>


    /**
     * Map the text type to functions that can implement it.
     */
    val lookup: Map<TextType, (String) -> Unit> get() = mapOf(
            Title to { it -> title(it) },
            Subtitle to { it -> subTitle(it) },
            Url to { it -> url(it) },
            Important to { it -> important(it) },
            Highlight to { it -> highlight(it) },
            Success to { it -> success(it) },
            Error to { it -> error(it) },
            Text to { it -> text(it) }
    )


    /**
     * Write many items based on the semantic modes
     *
     * @param items
     */
    fun writeItems(items: List<ConsoleItem>) {
        items.forEach { item -> writeItem(item.textType, item.msg, item.endLine) }
    }


    /**
     * Write many items based on the semantic modes
     *
     * @param items
     */
    fun writeItemsByText(items: List<ConsoleItem>): Unit {
        items.forEach { item -> writeItem(item.textType, item.msg, item.endLine) }
    }


    /**
     * Write a single item based on the semantic mode
     *
     * @param mode
     * @param msg
     * @param endLine
     */
    fun writeItem(mode: TextType, msg: String, endLine: Boolean) {
        if (lookup.contains(mode)) {
            TODO("test")
        }
    }


    /**
     * Converts the string representation of a semantic text to the strongly typed object
     *
     * @param mode
     */
    fun convert(mode: String): TextType {
        return when (mode.toLowerCase()) {

            "title"     -> Title
            "subtitle"  -> Subtitle
            "url"       -> Url
            "important" -> Important
            "highlight" -> Highlight
            "success"   -> Success
            "srror"     -> Error
            "text"      -> Text
            else        -> Text
        }
    }


    /**
     * prints text in the color supplied.
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun write(color: String, text: String, endLine: Boolean) {
        val finalText = if (endLine)
            color + " " + text + Strings.newline()
        else
            color + " " + text

        _io.run(finalText)
    }


    /**
     * prints a empty line
     */
    fun line() = _io.run(Strings.newline())


    /**
     * prints a empty line
     */
    fun lines(count: Int) {
        for (i in 0..count) {
            line()
        }
    }


    /**
     * prints a tab count times
     *
     * @param count
     */
    fun tab(count: Int = 1) {
        for (i in 0..count) {
            print(TAB)
        }
    }


    /**
     * Writes the text using the TextType
     *
     * @param mode
     * @param text
     * @param endLine
     */
    fun write(mode: TextType, text: String, endLine: Boolean = true) {
        write(mode.color, mode.format(text), endLine)
    }


    /**
     * prints text in title format ( UPPERCASE and BLUE )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun title(text: String, endLine: Boolean = true): Unit = write(Title, text, endLine)


    /**
     * prints text in subtitle format ( CYAN )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun subTitle(text: String, endLine: Boolean = true): Unit = write(Subtitle, text, endLine)


    /**
     * prints text in url format ( BLUE )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun url(text: String, endLine: Boolean = true): Unit = write(Url, text, endLine)


    /**
     * prints text in important format ( RED )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun important(text: String, endLine: Boolean = true): Unit = write(Important, text, endLine)


    /**
     * prints text in highlight format ( YELLOW )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun highlight(text: String, endLine: Boolean = true): Unit = write(Highlight, text, endLine)


    /**
     * prints text in title format ( UPPERCASE and BLUE )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun success(text: String, endLine: Boolean = true): Unit = write(Success, text, endLine)


    /**
     * prints text in error format ( RED )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun error(text: String, endLine: Boolean = true): Unit = write(Error, text, endLine)


    /**
     * prints text in normal format ( WHITE )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun text(text: String, endLine: Boolean = true): Unit = write(Text, text, endLine)


    /**
     * prints text in normal format ( WHITE )
     *
     * @param text    : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun label(text: String, endLine: Boolean = true) {
        val color = if (settings.darkMode) Console.BLACK else Console.WHITE
        write(color, text, endLine)
    }


    /**
     * Prints a list of items with indentation
     *
     * @param items
     * @param isOrdered
     */
    fun list(items: List<Any>, isOrdered: Boolean = false) {

        for (ndx in 0..items.size) {
            val item = items[ndx]
            val value = item.toString()
            val prefix = if (isOrdered) (ndx + 1).toString() + ". " else "- "
            text(TAB + prefix + value, endLine = true)
        }
        line()
    }


    /**
     * prints text using a label : value format
     *
     * @param key     :
     * @param value   : the text to print
     * @param endLine : whether or not to include a newline at the end
     */
    fun keyValue(key: String, value: String, endLine: Boolean = true) {
        label(key + " = ", false)
        text(value, endLine)
    }
}

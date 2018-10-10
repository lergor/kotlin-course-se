package ru.hse.spb.tex

import java.io.OutputStream

@DslMarker
annotation class TexTagMarker

interface Element {
    fun render(builder: StringBuilder)
}

class TextElement(private val text: String) : Element {
    override fun render(builder: StringBuilder) {
        builder.append("$text\n")
    }
}

data class Options(val options: List<Pair<String, String>> = listOf()) {
    override fun toString(): String {
        return if (options.isNotEmpty()) {
            return options.joinToString("][", "[", "]", transform = { "${it.first}=${it.second}" })
        } else ""
    }
}

data class Arguments(val arguments: List<String> = listOf()) {
    override fun toString(): String {
        return if (arguments.isNotEmpty()) arguments.joinToString(", ", "{", "}") else ""
    }
}

@TexTagMarker
abstract class SimpleTag(
        val name: String,
        val arguments: Arguments = Arguments()
) : Element {

    override fun render(builder: StringBuilder) {
        builder.append("\\$name$arguments\n")
    }
}

abstract class TagWithOptions(
        name: String,
        arguments: Arguments = Arguments(),
        val options: Options = Options()
) : SimpleTag(name, arguments) {
    override fun render(builder: StringBuilder) {
        builder.append("\\$name$options$arguments\n")
    }
}

abstract class WrappedTag(
        name: String,
        arguments: Arguments = Arguments(),
        options: Options = Options()
) : TagWithOptions(name, arguments, options) {

    val children = arrayListOf<Element>()

    fun customTag(name: String, arguments: List<String> = listOf(),
                  vararg options: Pair<String, String>, init: CustomTag.() -> Unit) {
        initTag(CustomTag(name, Arguments(arguments), Options(options.asList())), init)
    }

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    override fun render(builder: StringBuilder) {
        builder.append("\\begin{$name}$arguments$options\n")
        children.forEach { it.render(builder) }
        builder.append("\\end{$name}\n")
    }

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit = {}): T {
        tag.init()
        children.add(tag)
        return tag
    }
}

abstract class TeXWrappedTag(
        name: String,
        arguments: Arguments = Arguments(),
        options: Options = Options()
) : WrappedTag(name, arguments, options) {

    fun itemize(vararg options: Pair<String, String>,
                init: Itemize.() -> Unit) = initTag(Itemize(options.asList()), init)

    fun enumerate(vararg options: Pair<String, String>,
                  init: Enumerate.() -> Unit) = initTag(Enumerate(options.asList()), init)

    fun frame(frameTitle: String,
              vararg options: Pair<String, String>,
              init: Frame.() -> Unit) = initTag(Frame(frameTitle, options.asList()), init)

    fun math(vararg options: Pair<String, String>,
             init: Math.() -> Unit) = initTag(Math(options.asList()), init)

    fun center(init: Alignment.() -> Unit) = initTag(Alignment(Alignment.AlignmentType.CENTER), init)

    fun left(init: Alignment.() -> Unit) = initTag(Alignment(Alignment.AlignmentType.LEFT), init)

    fun right(init: Alignment.() -> Unit) = initTag(Alignment(Alignment.AlignmentType.RIGHT), init)
}

class CustomTag(
        name: String,
        arguments: Arguments = Arguments(),
        options: Options = Options()
) : TeXWrappedTag(name, arguments, options)

class DocumentClass(cls: String, options: Options)
    : TagWithOptions("documentclass", Arguments(listOf(cls)), options)

class UsePackage(packages: Arguments, options: Options = Options())
    : TagWithOptions("usepackage", packages, options)

class Title(title: String) : SimpleTag("title", Arguments(listOf(title)))

class Author(author: String) : SimpleTag("author", Arguments(listOf(author)))

class Date(date: String) : SimpleTag("date", Arguments(listOf(date)))

class DocumentBody : TeXWrappedTag("document")

class FrameTitle(title: String) : SimpleTag("frametitle", Arguments(listOf(title)))

class Item : TeXWrappedTag("item") {
    override fun render(builder: StringBuilder) {
        builder.append("\\$name$arguments$options\n")
        children.forEach { it.render(builder) }
    }
}

class Itemize(options: List<Pair<String, String>>)
    : WrappedTag("itemize", options = Options(options)) {
    fun item(init: Item.() -> Unit) = initTag(Item(), init)
}

class Enumerate(options: List<Pair<String, String>>)
    : WrappedTag("enumerate", options = Options(options)) {
    fun item(init: Item.() -> Unit) = initTag(Item(), init)
}

class Math(options: List<Pair<String, String>>)
    : WrappedTag("displaymath", options = Options(options))

class Alignment(type: AlignmentType) : TeXWrappedTag(type.toString()) {
    enum class AlignmentType {
        CENTER {
            override fun toString() = "center"
        },
        RIGHT {
            override fun toString() = "right"
        },
        LEFT {
            override fun toString() = "left"
        }

    }
}

class Frame(frameTitle: String, options: List<Pair<String, String>>)
    : TeXWrappedTag("frame", options = Options(options)) {
    init {
        initTag(FrameTitle(frameTitle))
    }
}

class TeXDocument : WrappedTag("") {

    fun documentClass(cls: String, vararg options: Pair<String, String>) {
        initTag(DocumentClass(cls, Options(options.asList())))
    }

    fun usepackage(name: String) = initTag(UsePackage(Arguments(listOf(name))))

    fun usepackage(vararg packages: String) {
        initTag(UsePackage(Arguments(packages.asList())))
    }

    fun usepackage(name: String, vararg options: Pair<String, String>) {
        initTag(UsePackage(Arguments(listOf(name)), Options(options.asList())))
    }

    fun title(title: String) = initTag(Title(title))

    fun author(author: String) = initTag(Author(author))

    fun date(date: String) = initTag(Date(date))

    fun documentBody(init: DocumentBody.() -> Unit) = initTag(DocumentBody(), init)

    override fun render(builder: StringBuilder) {
        children.forEach { it.render(builder) }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder)
        return builder.toString()
    }

    fun toOutputStream(os: OutputStream) {
        os.write(toString().toByteArray())
    }

}

fun document(init: TeXDocument.() -> Unit) = TeXDocument().apply(init)
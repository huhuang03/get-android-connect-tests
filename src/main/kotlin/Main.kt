import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.util.*


val tests = mutableListOf<String>()

fun getTests(file: File): List<String> {
    if (file.absolutePath.endsWith(".kt")) {
        return emptyList()
    }
    val parser = StaticJavaParser.parse(file)
    val rst = mutableListOf<String>()
    val visitor = object: VoidVisitorAdapter<Void>() {
        override fun visit(n: ClassOrInterfaceDeclaration?, arg: Void?) {
            if (n == null) {
                return
            }
            val annotations = n.annotations
            val isTest = annotations.any { it.nameAsString == "RunWith" }
            if (isTest) {
                rst.add(n.nameAsString)
            }
//            val methods = n.methods
//            methods.forEach {
//                if (it.annotations.any{ a -> a.nameAsString == "Test" }) {
//
//                }
//            }
        }
    }
    visitor.visit(parser, null)
    return rst
}

fun main(args: Array<String>) {
//    if (args.size < 2) {
//        println("Usage: ${args[0]} folder")
//    }
    val root = "/Users/hwf/source/xiaoan-android-repo/xiaoan-android/app/src/androidTest"
//    val root = args[1]

    for (file in File(root).walk()) {
        if (file.isFile) {
            tests.addAll(getTests(file))
        }
    }
    tests.sort()
    println("tests: \n${tests.joinToString("\n")}")
}
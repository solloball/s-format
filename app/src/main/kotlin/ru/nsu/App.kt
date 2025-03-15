/*
 * This source file was generated by the Gradle 'init' task
 */
package ru.nsu

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.JExpressionPath
import ru.nsu.jexpression_path.enums.PathMode
import ru.nsu.sexpression.SExpression
import ru.nsu.sexpression.SExpressionParserDumb

fun main() {
    val parser = SExpressionParserDumb()
    val test = JExpression.fromSExpression(parser.parse("[ name [ otherName [ thirdName ( 1 2 SUCCESS  ) ] ]  ]"))
    val test2 = JExpression.fromSExpression(parser.parse("[ name [ name [ thirdName ( 1 2 SUCCESS  ) ] ]  ]"))
    val test3 = JExpression.fromSExpression(parser.parse("( ( 1 2 3 ) ( 1 2 ) ( 1 2 3 4 ) )"))

    val path = JExpressionPath()

    println(path.find("$.name.otherName[thirdName][2]", test))
    println(path.find("$..[name]", test2))
    println(path.modify("$.name.otherName[thirdName][2]", test, JExpression.fromSExpression(SExpression.AtomString("New Value"))))
    println(path.find("$..[0][? ($) == (1) ]", test3))
    println(JExpressionPath
        .QueryBuilder()
        .root(test3)
        .mode(PathMode.DEEP_MODE)
        .bracketIndex(0)
        .filter("(\$) == (1)")
        .find()
    )
}

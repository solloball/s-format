package ru.nsu.jexpression_path

import com.google.common.collect.ImmutableList
import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.enums.PathMode
import ru.nsu.jexpression_path.executor.JExpressionPathExecutor
import ru.nsu.jexpression_path.parser.JExpressionPathParser
import ru.nsu.jexpression_path.types.UpdaterResult
import ru.nsu.sexpression.SExpression

class JExpressionPath {
    private val jExpressionPathParser: JExpressionPathParser = JExpressionPathParser()
    private val jExpressionPathExecutor: JExpressionPathExecutor = JExpressionPathExecutor()

    fun find(jExpressionPath: String, root: JExpression): List<JExpression> {

        val parserResult = jExpressionPathParser.parse(jExpressionPath)

        val result = jExpressionPathExecutor.execute(root, parserResult)

        return result
    }

    fun modify(jExpressionPath: String, root: JExpression, newElement: JExpression): UpdaterResult {
        val finderResult = find(jExpressionPath, root)
        if (finderResult.size != 1) {
            return UpdaterResult(false, null)
        }

        return UpdaterResult(true, returnWithChanging(root, finderResult[0], newElement))
    }

    private fun returnWithChanging(root: JExpression, oldValue: JExpression, toChange: JExpression): JExpression {
        if (root.getValue() == oldValue.getValue()) {
            return toChange
        }
        return when (root) {
            is JExpression.JArray -> JExpression.JArray.fromSExpression( SExpression.List( root.getValue().map { returnWithChanging(it, oldValue, toChange).getSExpression() } ) )
            is JExpression.JObject -> JExpression.JObject.fromSExpression( SExpression.Array( root.getValue().flatMap { ImmutableList.of(SExpression.AtomString(it.key), returnWithChanging(it.value, oldValue, toChange).getSExpression()) } ) )
            is JExpression.JString -> root
            is JExpression.JInteger -> root
        }
    }

    class QueryBuilder {
        private var root: JExpression? = null
        private var mode: PathMode? = null
        private var path: String = ""
        private var toChange: JExpression? = null

        fun mode(mode: PathMode): QueryBuilder {
            this.mode = mode
            return this
        }
        fun root(root: JExpression): QueryBuilder {
            this.root = root
            return this
        }
        fun dot(name: String): QueryBuilder {
            this.path += ".$name"
            return this
        }
        fun bracket(name: String): QueryBuilder {
            this.path += "[$name]"
            return this
        }
        fun bracketIndex(idx: Int): QueryBuilder {
            this.path += "[$idx]"
            return this
        }
        fun filter(predicateExpression: String): QueryBuilder {
            this.path += "[?$predicateExpression]"
            return this
        }

        fun find(): List<JExpression> {
            if (mode == null || root == null) {
                throw IllegalArgumentException("Failed to find because of mode or root is null")
            }
            return JExpressionPath().find("${mode!!.jExpressionPrefix}$path", root!!)
        }

        fun update(): UpdaterResult {
            if (mode == null || root == null || toChange == null) {
                throw IllegalArgumentException("Failed to find because of mode or root is null")
            }
            return JExpressionPath().modify("${mode!!.jExpressionPrefix}$path", root!!, toChange!!)
        }
    }
}
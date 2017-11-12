package ru.spbau.mit

import java.io.OutputStream

/** Node = File | Statement */
sealed class Node

/** Statement = Function | Variable | Expression | While | If | Assignment| Return */
sealed class Statement: Node() {
    abstract fun perform(ctx: Context)
    abstract val line: Int
}

data class File(val block: Block): Node() {
    fun perform(stdout: OutputStream = System.out) {
        block.perform(RootContext(stdout).addStackFrame())
    }
}

data class Function(val name: String, val argsNames: List<String>, val block: Block,
                    override val line: Int = 0) : Statement() {
    var declCtx: Context? = null
        private set
    override fun perform(ctx: Context) {
        declCtx = ctx
        ctx.addFun(name, this, line)
    }
}

data class Variable(val name: String, val expr: Expression? = null, override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) {
        ctx.addVar(name, expr?.evaluate(ctx) ?: 0, line)
    }
}

data class While(val cond: Expression, val block: Block, override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) {
        while (cond.evaluate(ctx) != 0) {
            block.perform(ctx.addStackFrame())
        }
    }
}

data class If(val cond: Expression, val thenBlock: Block, val elseBlock: Block? = null,
              override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) {
        if (cond.evaluate(ctx) != 0) {
            thenBlock.perform(ctx.addStackFrame())
        } else {
            elseBlock?.perform(ctx.addStackFrame())
        }
    }
}

/** Expression = FunctionCall | BinaryExpression | Reference | Literal */
sealed class Expression: Statement() {
    abstract fun evaluate(ctx: Context): Int

    override fun perform(ctx: Context) {
        evaluate(ctx)
    }
}

data class FunctionCall(val name: String, val argsExprs: List<Expression>,
                        override val line: Int = 0): Expression() {
    override fun evaluate(ctx: Context): Int {
        if (name == "println") {
            return Builtin.println(argsExprs, ctx.addStackFrame())
        }

        try {
            val func = ctx.getFun(name, line)
            if (func.argsNames.size != argsExprs.size) {
                throw WrongNumberOfArgsException(name, func.argsNames.size, argsExprs.size, ctx, line)
            }
            val newCtx = func.declCtx!!.addStackFrame()
            for ((name, expr) in func.argsNames.zip(argsExprs)) {
                newCtx.addVar(name, expr.evaluate(ctx), line)
            }
            func.block.perform(newCtx)
        } catch (ret: ReturnException) {
            return ret.value
        }
        return 0
    }
}

data class BinaryOperation(val lhs: Expression, val op: String, val rhs: Expression,
                           override val line: Int = 0): Expression() {
    companion object {
        private fun intToBool(value: Int): Boolean = value == 1
        private fun boolToInt(value: Boolean): Int = if (value) 1 else 0
    }

    override fun evaluate(ctx: Context): Int = when (op) {
        "+" -> lhs.evaluate(ctx) + rhs.evaluate(ctx)
        "-" -> lhs.evaluate(ctx) - rhs.evaluate(ctx)
        "*" -> lhs.evaluate(ctx) * rhs.evaluate(ctx)
        "/" -> lhs.evaluate(ctx) / rhs.evaluate(ctx)
        "%" -> lhs.evaluate(ctx) % rhs.evaluate(ctx)
        ">" -> boolToInt(lhs.evaluate(ctx) > rhs.evaluate(ctx))
        "<" -> boolToInt(lhs.evaluate(ctx) < rhs.evaluate(ctx))
        ">=" -> boolToInt(lhs.evaluate(ctx) >= rhs.evaluate(ctx))
        "<=" -> boolToInt(lhs.evaluate(ctx) <= rhs.evaluate(ctx))
        "==" -> boolToInt(lhs.evaluate(ctx) == rhs.evaluate(ctx))
        "!=" -> boolToInt(lhs.evaluate(ctx) != rhs.evaluate(ctx))
        "||" -> boolToInt(intToBool(lhs.evaluate(ctx)) || intToBool(rhs.evaluate(ctx)))
        "&&" -> boolToInt(intToBool(lhs.evaluate(ctx)) && intToBool(rhs.evaluate(ctx)))
        else -> throw UnsupportedBinaryOperation(op, line)
    }
}

data class Reference(val name: String,
                     override val line: Int = 0): Expression() {
    override fun evaluate(ctx: Context): Int = ctx.getVar(name, line)
}

data class Literal(val value: Int, override val line: Int = 0): Expression() {
    override fun evaluate(ctx: Context): Int = value
}

data class Return(val expr: Expression, override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) = throw ReturnException(expr.evaluate(ctx))
}

data class Assignment(val name: String, val expr: Expression, override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) = ctx.assignVar(name, expr.evaluate(ctx), line)
}

data class Block(val statements: List<Statement>, override val line: Int = 0): Statement() {
    override fun perform(ctx: Context) {
        for (st in statements) {
            st.perform(ctx)
        }
    }
}

object Builtin {
    fun println(argsExprs: List<Expression>, ctx: Context): Int {
        ctx.stdout.write(argsExprs.joinToString(postfix = "\n") { it.evaluate(ctx).toString() }.toByteArray())
        return 0
    }
}

data class ReturnException(val value: Int): Exception()

data class WrongNumberOfArgsException(val name: String, val expected: Int, val action: Int,
                                      val ctx: Context, val line: Int): Exception()

data class UnsupportedBinaryOperation(val op: String, val line: Int): Exception()

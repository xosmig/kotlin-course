package ru.spbau.mit

import java.io.OutputStream
import java.util.*
import kotlin.collections.HashMap

// Node = File | Statement
sealed class Node

sealed class Statement: Node() {
    abstract fun perform(ctx: Context)
}

class StackFrame {
    private val vars = HashMap<String, Int>()
    private val funs = HashMap<String, Function>()

    fun getVar(name: String): Int? = vars[name]

    fun addVar(name: String, value: Int) {
        vars[name] = value
    }

    fun getFun(name: String): Function? = funs[name]

    fun addFun(name: String, func: Function) {
        funs[name] = func
    }
}

class Context(val stdout: OutputStream) {
    private val callStack = LinkedList<StackFrame>()
    private val top get() = callStack.first

    var maxStackSize = 1000

    fun getVar(name: String): Int {
        for (frame in callStack) {
            val value = frame.getVar(name)
            if (value != null) {
                return value
            }
        }
        throw UnknownIdentifierException(name, this)
    }

    fun addVar(name: String, value: Int) {
        top.addVar(name, value)
    }

    fun assignVar(name: String, value: Int) {
        for (frame in callStack) {
            val oldValue = frame.getVar(name)
            if (oldValue != null) {
                frame.addVar(name, value)
                return
            }
        }
        throw UnknownIdentifierException(name, this)
    }

    fun getFun(name: String): Function {
        for (frame in callStack) {
            val func = frame.getFun(name)
            if (func != null) {
                return func
            }
        }
        throw UnknownIdentifierException(name, this)
    }

    fun addFun(name: String, func: Function) {
        top.addFun(name, func)
        if (callStack.size > maxStackSize) {
            throw TooDeepRecursionException(callStack.size)
        }
    }

    fun addStackFrame() {
        callStack.addFirst(StackFrame())
    }

    fun popStackFrame() {
        callStack.removeFirst()
    }
}

data class File(val block: Block): Node() {
    fun perform(stdout: OutputStream = System.out) {
        block.perform(Context(stdout))
    }
}

data class Function(val name: String, val argsNames: List<String>, val block: Block): Statement() {
    override fun perform(ctx: Context) = ctx.addFun(name, this)
}

data class Variable(val name: String, val expr: Expression): Statement() {
    override fun perform(ctx: Context) {
        ctx.addVar(name, expr.evaluate(ctx))
    }
}

data class While(val cond: Expression, val block: Block): Statement() {
    override fun perform(ctx: Context) {
        while (cond.evaluate(ctx) != 0) {
            block.perform(ctx)
        }
    }
}

data class If(val cond: Expression, val thenBlock: Block, val elseBlock: Block? = null): Statement() {
    override fun perform(ctx: Context) {
        if (cond.evaluate(ctx) != 0) {
            thenBlock.perform(ctx)
        } else {
            elseBlock?.perform(ctx)
        }
    }
}

sealed class Expression: Statement() {
    abstract fun evaluate(ctx: Context): Int

    override fun perform(ctx: Context) {
        evaluate(ctx)
    }
}

data class FunctionCall(val name: String, val argsExprs: List<Expression>): Expression() {
    override fun evaluate(ctx: Context): Int {
        ctx.addStackFrame()
        try {
            val func = ctx.getFun(name)
            if (func.argsNames.size != argsExprs.size) {
                throw WrongNumberOfArgsException(name, func.argsNames.size, argsExprs.size, ctx)
            }
            for ((name, expr) in func.argsNames.zip(argsExprs)) {
                ctx.addVar(name, expr.evaluate(ctx))
            }
            func.block.perform(ctx)
        } catch (ret: ReturnException) {
            return ret.value
        } finally {
            ctx.popStackFrame()
        }
        return 0
    }
}


data class Plus(val lhs: Expression, val rhs: Expression): Expression() {
    override fun evaluate(ctx: Context): Int = lhs.evaluate(ctx) + rhs.evaluate(ctx)
}

data class Minus(val lhs: Expression, val rhs: Expression): Expression() {
    override fun evaluate(ctx: Context): Int = lhs.evaluate(ctx) - rhs.evaluate(ctx)
}

data class LessEqual(val lhs: Expression, val rhs: Expression): Expression() {
    override fun evaluate(ctx: Context): Int = if (lhs.evaluate(ctx) <= rhs.evaluate(ctx)) 1 else 0
}

//class Times(lhs: Expression, rhs: Expression): BinaryExpression(lhs, rhs)
//class Div(lhs: Expression, rhs: Expression): BinaryExpression(lhs, rhs)
//class Modulo(lhs: Expression, rhs: Expression): BinaryExpression(lhs, rhs)
//class Greater(lhs: Expression, rhs: Expression): BinaryExpression(lhs, rhs)
//class Less(lhs: Expression, rhs: Expression): BinaryExpression(lhs, rhs)
// TODO: >=, ==, !=, ||, &&

data class Reference(val name: String): Expression() {
    override fun evaluate(ctx: Context): Int = ctx.getVar(name)
}

data class Literal(val value: Int): Expression() {
    override fun evaluate(ctx: Context): Int = value
}

data class Return(val expr: Expression): Statement() {
    override fun perform(ctx: Context) = throw ReturnException(expr.evaluate(ctx))
}

data class Assignment(val name: String, val expr: Expression): Statement() {
    override fun perform(ctx: Context) = ctx.assignVar(name, expr.evaluate(ctx))
}

data class Block(val statements: List<Statement>): Statement() {
    override fun perform(ctx: Context) {
        ctx.addStackFrame()
        try {
            for (st in statements) {
                st.perform(ctx)
            }
        } finally {
            ctx.popStackFrame()
        }
    }
}

data class PrintlnCall(val args: List<Expression>): Statement() {
    override fun perform(ctx: Context) {
        ctx.stdout.write(args.joinToString(postfix = "\n") { it.evaluate(ctx).toString() }.toByteArray())
    }
}

data class ReturnException(val value: Int): Exception()

data class WrongNumberOfArgsException(val name: String, val expected: Int, val action: Int,
                                      val ctx: Context): Exception()
data class UnknownIdentifierException(val name: String, val ctx: Context): Exception()
data class TooDeepRecursionException(val depth: Int): Exception()
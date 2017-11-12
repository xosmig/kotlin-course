package ru.spbau.mit

import java.io.OutputStream

interface Context {
    val stdout: OutputStream
    fun getVar(name: String, line: Int): Int
    fun addVar(name: String, value: Int, line: Int)
    fun assignVar(name: String, value: Int, line: Int)
    fun getFun(name: String, line: Int): Function
    fun addFun(name: String, func: Function, line: Int)
    fun addStackFrame(): Context = StackFrame(this)
}

class RootContext(override val stdout: OutputStream): Context {
    override fun getVar(name: String, line: Int): Int =
            throw UnknownVariableException(name, line)
    override fun addVar(name: String, value: Int, line: Int) =
            throw AssertionError("This function should never be called on RootContext")
    override fun assignVar(name: String, value: Int, line: Int) =
            throw AssertionError("This function should never be called on RootContext")
    override fun getFun(name: String, line: Int): Function =
            throw UnknownFunctionException(name, line)
    override fun addFun(name: String, func: Function, line: Int) =
            throw AssertionError("This function should never be called on RootContext")
}

private class StackFrame(val parent: Context): Context {
    private val vars = HashMap<String, Int>()
    private val funs = HashMap<String, Function>()

    override val stdout: OutputStream get() = parent.stdout

    override fun getVar(name: String, line: Int): Int = vars[name] ?: parent.getVar(name, line)

    override fun addVar(name: String, value: Int, line: Int) {
        if (name in vars) {
            throw VariableRedeclarationException(name, line)
        } else {
            vars[name] = value
        }
    }

    override fun assignVar(name: String, value: Int, line: Int) {
        if (name in vars) {
            vars[name] = value
        } else {
            parent.assignVar(name, value, line)
        }
    }

    override fun getFun(name: String, line: Int): Function = funs[name] ?: parent.getFun(name, line)

    override fun addFun(name: String, func: Function, line: Int) {
        if (name in funs) {
            throw FunctionRedeclarationException(name, line)
        } else {
            funs[name] = func
        }
    }
}

data class UnknownVariableException(val name: String, val line: Int): Exception()
data class UnknownFunctionException(val name: String, val line: Int): Exception()
data class VariableRedeclarationException(val name: String, val line: Int): Exception()
data class FunctionRedeclarationException(val name: String, val line: Int): Exception()

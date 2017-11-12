package ru.spbau.mit

import java.io.OutputStream

interface Context {
    val stdout: OutputStream
    fun getVar(name: String): Int
    fun addVar(name: String, value: Int)
    fun assignVar(name: String, value: Int)
    fun getFun(name: String): Function
    fun addFun(name: String, func: Function)
    fun addStackFrame(): Context = StackFrame(this)
}

class RootContext(override val stdout: OutputStream): Context {
    override fun getVar(name: String): Int =
            throw UnknownVariableException(name)
    override fun addVar(name: String, value: Int) =
            throw TODO()
    override fun assignVar(name: String, value: Int) =
            throw TODO()
    override fun getFun(name: String): Function =
            throw UnknownFunctionException(name)
    override fun addFun(name: String, func: Function) =
            throw TODO()
}

private class StackFrame(val parent: Context): Context {
    private val vars = HashMap<String, Int>()
    private val funs = HashMap<String, Function>()

    override val stdout: OutputStream get() = parent.stdout

    override fun getVar(name: String): Int = vars[name] ?: parent.getVar(name)

    override fun addVar(name: String, value: Int) {
        if (name in vars) {
            throw VariableRedeclarationException(name)
        } else {
            vars[name] = value
        }
    }

    override fun assignVar(name: String, value: Int) {
        if (name in vars) {
            vars[name] = value
        } else {
            parent.assignVar(name, value)
        }
    }

    override fun getFun(name: String): Function = funs[name] ?: parent.getFun(name)

    override fun addFun(name: String, func: Function) {
        if (name in funs) {
            throw FunctionRedeclarationException(name)
        } else {
            funs[name] = func
        }
    }
}

data class UnknownVariableException(val name: String): Exception()
data class UnknownFunctionException(val name: String): Exception()
data class VariableRedeclarationException(val name: String): Exception()
data class FunctionRedeclarationException(val name: String): Exception()
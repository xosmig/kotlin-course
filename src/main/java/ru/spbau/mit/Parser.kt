package ru.spbau.mit

import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ErrorNode
import ru.spbau.mit.parser.HwLangBaseVisitor
import ru.spbau.mit.parser.HwLangLexer
import ru.spbau.mit.parser.HwLangParser
import java.io.FileInputStream
import java.io.InputStream

object Parser {
    fun parseFile(path: String): File {
        FileInputStream(path).use {
            return parse(it)
        }
    }

    fun parse(sourceCodeStream: InputStream): File {
        val lexer = HwLangLexer(CharStreams.fromStream(sourceCodeStream))
        val tokens = CommonTokenStream(lexer)
        val parser = HwLangParser(tokens)
        val fileCtx = parser.file()
        if (fileCtx.exception != null) {
            throw fileCtx.exception
        }
        return ANTLRVisitor.visit(fileCtx) as File
    }
}

object ANTLRVisitor : HwLangBaseVisitor<Node>() {
    override fun visitFile(ctx: HwLangParser.FileContext): Node =
            File(visitBlock(ctx.block_) as Block)

    override fun visitBlock(ctx: HwLangParser.BlockContext): Node =
            Block(ctx.statement().map { visit(it) as Statement }, ctx.start.line)

    override fun visitFunction(ctx: HwLangParser.FunctionContext): Node =
            Function(ctx.name_.text,
                    ctx.params_.IDENTIFIER().map { it.text },
                    visitBlock(ctx.block_) as Block,
                    ctx.start.line)

    override fun visitVariable(ctx: HwLangParser.VariableContext): Node =
            Variable(ctx.name_.text, visitExpression(ctx.expr_) as Expression, ctx.start.line)

    override fun visitWhileSt(ctx: HwLangParser.WhileStContext): Node =
            While(visitExpression(ctx.cond_) as Expression, visitBlock(ctx.block_) as Block, ctx.start.line)

    override fun visitIfSt(ctx: HwLangParser.IfStContext): Node =
            If(visitExpression(ctx.cond_) as Expression,
                    visitBlock(ctx.thenBlock_) as Block,
                    ctx.elseBlock_?.let { visitBlock(it) as Block },
                    ctx.start.line)

    override fun visitAssignment(ctx: HwLangParser.AssignmentContext): Node =
            Assignment(ctx.name_.text, visitExpression(ctx.expr_) as Expression, ctx.start.line)

    override fun visitReturnSt(ctx: HwLangParser.ReturnStContext): Node =
            Return(visitExpression(ctx.expr_) as Expression, ctx.start.line)

    override fun visitFunctionCall(ctx: HwLangParser.FunctionCallContext): Node =
            FunctionCall(ctx.name_.text,
                    ctx.args_.expression().map { visitExpression(it) as Expression },
                    ctx.start.line)

    override fun visitReference(ctx: HwLangParser.ReferenceContext): Node =
            Reference(ctx.name_.text, ctx.name_.line)

    override fun visitLiteral(ctx: HwLangParser.LiteralContext): Node =
            Literal(ctx.text.toInt(), ctx.start.line)

    override fun visitExpression(ctx: HwLangParser.ExpressionContext): Node {
        if (ctx.binOp_ != null) {
            return BinaryOperation(
                    visit(ctx.lhs_) as Expression,
                    ctx.binOp_.text,
                    visit(ctx.rhs_) as Expression,
                    ctx.start.line)
        } else {
            return visitChildren(ctx)
        }
    }
}

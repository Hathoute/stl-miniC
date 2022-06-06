package fr.n7.stl.block.ast.instruction.minijava;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ExpressionWrapper implements Instruction {

    protected Expression expression;

    public ExpressionWrapper(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString() + ";\n";
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return expression.collectAndBackwardResolve(_scope);
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        return expression.fullResolve(_scope);
    }

    @Override
    public boolean checkType() {
        return expression.getType() != AtomicType.ErrorType;
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        // Make sure that memory allocated by the expression is removed by this wrapper
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public CheckReturnCode checkReturnType(Type type) {
        return CheckReturnCode.CONTINUE;
    }
}

package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.expression.Expression;
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
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return this.expression.collectAndBackwardResolve(_scope);
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        return this.expression.fullResolve(_scope);
    }

    @Override
    public boolean checkType() {
        return !this.expression.getType().equalsTo(AtomicType.ErrorType);
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        return expression.getCode(_factory);
    }

    @Override
    public CheckReturnCode checkReturnType(Type type) {
        return CheckReturnCode.CONTINUE;
    }
}

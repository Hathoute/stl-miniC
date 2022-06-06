package fr.n7.stl.block.ast.instruction.minijava;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;

import java.util.List;

public class MethodCall implements AssignableExpression, Instruction {

    protected Expression object;
    protected List<Expression> parameters;

    public MethodCall(Expression object, List<Expression> parameters) {
        this.object = object;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return object.toString() + "(" + Helper.formatParameters(parameters) + ")";
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return Helper.startSequence(object.collectAndBackwardResolve(_scope))
                .and(Helper.matchAll(parameters, x -> x.collectAndBackwardResolve(_scope)))
                .finish();
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        return Helper.startSequence(object.fullResolve(_scope))
                .and(Helper.matchAll(parameters, x -> x.fullResolve(_scope)))
                .finish();
    }

    @Override
    public boolean checkType() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Type getType() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public boolean isConstant() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public CheckReturnCode checkReturnType(Type type) {
        throw new RuntimeException("Method is not defined");
    }
}

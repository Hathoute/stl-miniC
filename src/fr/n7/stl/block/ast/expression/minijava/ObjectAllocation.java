package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;

import java.util.List;

public class ObjectAllocation implements Expression {

    protected Type type;
    protected List<Expression> parameters;

    public ObjectAllocation(Type type, List<Expression> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "new " + type + "(" + Helper.formatParameters(parameters) + ")";
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return Helper.startSequence(Helper.matchAll(parameters, x -> x.collectAndBackwardResolve(_scope)))
                .and(type.resolve(_scope))
                .finish();
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        return Helper.matchAll(parameters, x -> x.fullResolve(_scope));
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
}

package fr.n7.stl.block.ast.instruction.minijava;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.MethodType;
import fr.n7.stl.block.ast.type.minijava.ThisType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class MethodCall implements AssignableExpression, Instruction {

    protected Expression object;
    protected List<Expression> parameters;

    protected MethodDefinition methodDefinition;

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
        Type objectType = object.getType();
        List<Type> parameterTypes = parameters.stream().map(Expression::getType).collect(Collectors.toList());
        if(objectType instanceof MethodType) {
            MethodType mt = (MethodType) objectType;
            methodDefinition = mt.getDefinition();
            if(!mt.compatibleWithParameters(parameterTypes)) {
                Logger.error("No overload existing for method '" + methodDefinition.getName() + "'" +
                        " that takes parameters: " + Helper.formatParameters(parameterTypes));
                return AtomicType.ErrorType;
            }
            return ((MethodType) objectType).getReturnType();
        }

        Logger.error("Attempting to invoke a non-invokable expression \"" + this.object + "\"");
        return AtomicType.ErrorType;
    }

    @Override
    public boolean isConstant() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment thisCode = _factory.createFragment();
        int i = this.parameters.size() - 1;
        for(; i >= 0; i--) {
            thisCode.append(this.parameters.get(i).getCode(_factory));
        }
        // TODO: Return here after implementing static methods.
        thisCode.append(this.object.getCode(_factory)); // this_call as calling convention
        thisCode.add(_factory.createCall(this.methodDefinition.getName(), Register.LB));
        return thisCode;
    }

    @Override
    public CheckReturnCode checkReturnType(Type type) {
        throw new RuntimeException("Method is not defined");
    }
}

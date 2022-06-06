package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.minijava.ClassDefinition;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.ConstructorDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.InstanceType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectAllocation implements Expression {

    protected Type type;
    protected List<Expression> parameters;

    protected ClassDefinition definition;
    protected ConstructorDefinition constructor;

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
        if(!(type instanceof InstanceType)) {
            Logger.error("Cannot instantiate object of type '" + type + "'");
            return AtomicType.ErrorType;
        }

        Element element = ((InstanceType) type).getTypeDefinition();
        // TODO: Return here when implementing abstract classes
        if(!(element instanceof ClassDefinition)) {
            Logger.error("Cannot instantiate interface '" + element.getName() + "'");
            return AtomicType.ErrorType;
        }

        this.definition = (ClassDefinition) element;
        this.constructor = definition.getConstructor();
        List<Type> parameterTypes = parameters.stream().map(Expression::getType).collect(Collectors.toList());
        if (!constructor.compatibleWith(parameterTypes)) {
            Logger.error("Cannot instantiate '" + element.getName() + "' : " +
                    "No constructor accepting '" + Helper.formatParameters(parameterTypes) + "'");
            return AtomicType.ErrorType;
        }

        return type;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment thisCode = _factory.createFragment();
        int i = this.parameters.size() - 1;
        for(; i >= 0; i--) {
            thisCode.append(this.parameters.get(i).getCode(_factory));
        }

        thisCode.add(_factory.createCall(this.constructor.getName(), Register.LB));
        return thisCode;
    }
}

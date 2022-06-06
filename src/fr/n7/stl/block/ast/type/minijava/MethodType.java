package fr.n7.stl.block.ast.type.minijava;

import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.minijava.subelement.Signature;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

import java.util.List;

public class MethodType implements Type {

    protected MethodDefinition definition;

    public MethodType(MethodDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean equalsTo(Type _other) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public boolean compatibleWith(Type _other) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Type merge(Type _other) {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public int length() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }

    public MethodDefinition getDefinition() {
        return definition;
    }

    public boolean compatibleWithParameters(List<Type> parameterTypes) {
        if(definition.getSignature().getParameters().size() != parameterTypes.size()) {
            return false;
        }

        for (int i = 0; i < parameterTypes.size(); i++) {
            if(!parameterTypes.get(i).compatibleWith(definition.getSignature().getParameters().get(i).getType())) {
                return false;
            }
        }

        return true;
    }

    public Type getReturnType() {
        return this.definition.getSignature().getType();
    }
}

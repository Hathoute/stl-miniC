package fr.n7.stl.block.ast.type.minijava;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public class MethodType implements Type {
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
        throw new RuntimeException("Method is not defined");
    }
}

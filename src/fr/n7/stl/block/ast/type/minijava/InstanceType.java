package fr.n7.stl.block.ast.type.minijava;

import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class InstanceType implements Type {

    protected String name;

    protected Element typeDefinition;

    public InstanceType(String name) {
        this.name = name;
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
        if(!_scope.knows(name)) {
            Logger.error("Unknown type: \"" + name + "\"");
            return false;
        }

        if(!(_scope.get(name) instanceof Element)) {
            Logger.error("Invalid type \"" + name + "\" (" + _scope.get(name) + ")");
            return false;
        }

        this.typeDefinition = (Element) _scope.get(name);
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}

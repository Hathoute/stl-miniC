package fr.n7.stl.block.ast.type.minijava;

import fr.n7.stl.block.ast.minijava.ClassDefinition;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
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
        return _other instanceof InstanceType && this.typeDefinition == ((InstanceType) _other).typeDefinition;
    }

    @Override
    public boolean compatibleWith(Type _other) {
        // TODO: When implementing inheritance, come back here
        return equalsTo(_other);
    }

    @Override
    public Type merge(Type _other) {
        // TODO: When implementing inheritance, come back here
        return equalsTo(_other) ? this : AtomicType.ErrorType;
    }

    @Override
    public int length() {
        return 1;       // Object instance is a pointer...
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

    public String getName() {
        return this.name;
    }

    public boolean contains(String name) {
        if(this.typeDefinition instanceof ClassDefinition) {
            ClassDefinition cd = (ClassDefinition) this.typeDefinition;

            // TODO: Return here when implementing overloading
            return cd.getAttribute(name) != null || cd.getMethod(name) != null;
        }

        // TODO: Return here when implementing interfaces
        return false;
    }

    public ClassElement get(String name) {
        ClassElement element;
        if(this.typeDefinition instanceof ClassDefinition) {
            ClassDefinition cd = (ClassDefinition) this.typeDefinition;

            if ((element = cd.getAttribute(name)) != null) {
                return element;
            }
            else if((element = cd.getMethod(name)) != null) {
                return element;
            }
        }

        throw new RuntimeException("Cannot find class element " + name + " in type " + this.typeDefinition);
    }
}

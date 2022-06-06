package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class AttributeDefinition implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected Type type;
    protected String name;
    protected Expression value;

    public AttributeDefinition(boolean isStatic, boolean isFinal, Type type, String name, Expression value) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);

        if(!elementScope.accepts(this)) {
            Logger.error("Duplicate field definition for \"" + name + "\"");
            return false;
        }

        elementScope.register(this);
        Environment.getInstance().setCurrentClassElement(null);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);

        boolean ok = type.resolve(elementScope);
        if(value != null) {
            SymbolTable<Declaration> s = new SymbolTable<>(elementScope);
            ok = value.collectAndBackwardResolve(s) && ok;
            ok = value.fullResolve(s) && ok;
        }

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }
}

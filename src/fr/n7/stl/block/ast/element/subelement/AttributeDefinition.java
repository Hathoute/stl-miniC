package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

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

    @Override
    public boolean collect(HierarchicalScope<ClassElement> _scope) {
        if(!_scope.accepts(this)) {
            return false;
        }

        _scope.register(this);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<ClassElement> _scope) {
        boolean ok = this.type.resolve();
        SymbolTable<Declaration> innerScope = new SymbolTable<>();
        ok = this.value.collectAndBackwardResolve(innerScope) && ok;
        ok = this.value.fullResolve(innerScope) && ok;

        return ok;
    }

    @Override
    public boolean checkType() {
        return false;
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }
}

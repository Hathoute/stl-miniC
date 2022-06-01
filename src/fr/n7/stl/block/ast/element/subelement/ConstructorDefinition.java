package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class ConstructorDefinition implements ClassElement {

    protected String name;
    protected List<ParameterDeclaration> parameters;
    protected Block block;

    public ConstructorDefinition(String name, List<ParameterDeclaration> parameters, Block block) {
        this.name = name;
        this.parameters = parameters;
        this.block = block;
    }

    @Override
    public boolean collect(HierarchicalScope<ClassElement> _scope) {
        String parentClassName = Environment.getInstance().getCurrentElement().getName();
        if(!parentClassName.equals(name)) {
            return false;
        }

        if(!_scope.accepts(this)) {
            return false;
        }

        _scope.register(this);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<ClassElement> _scope) {
        HierarchicalScope<Declaration> innerScope = new SymbolTable<>(_scope);
        Signature s = new Signature(AtomicType.NullType, name, parameters);
        boolean ok = s.collect(innerScope);
        ok = s.resolve(innerScope) && ok;
        ok = block.collect(innerScope) && ok;
        ok = block.resolve(innerScope) && ok;

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
    public int getOffset() {
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
        return null;
    }
}

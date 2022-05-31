package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class MethodSignatureDefinition implements ClassElement {

    protected Signature signature;

    public MethodSignatureDefinition(Signature signature) {
        this.signature = signature;
    }

    @Override
    public boolean collect(HierarchicalScope<ClassElement> _scope) {
        if(!_scope.accepts(this)) {
            return false;
        }

        _scope.register(this);
        return this.signature.collect(new SymbolTable<>());
    }

    @Override
    public boolean resolve(HierarchicalScope<ClassElement> _scope) {
        return this.signature.resolve(new SymbolTable<>());
    }

    @Override
    public boolean checkType() {
        return true;
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
        return signature.name;
    }

    @Override
    public Type getType() {
        return signature.type;
    }
}

package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Method implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected Signature signature;
    protected Block block;

    public Method(boolean isStatic, boolean isFinal, boolean isAbstract, Signature signature, Block block) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.signature = signature;
        this.block = block;
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> _scope) {
        if(!_scope.accepts(this)) {
            return false;
        }

        _scope.register(this);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        Environment.getInstance().setCurrentFunction(this);
        HierarchicalScope<Declaration> innerScope = new SymbolTable(_scope);
        boolean res = block.collect(innerScope) && block.resolve(innerScope);
        Environment.getInstance().setCurrentFunction(null);

        return res;
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
        return signature.name;
    }

    @Override
    public Type getType() {
        return signature.type;
    }
}

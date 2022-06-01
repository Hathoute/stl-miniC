package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.MethodType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;
import java.util.stream.Collectors;

public class MethodDefinition implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected Signature signature;
    protected Block block;

    public MethodDefinition(boolean isStatic, boolean isFinal, boolean isAbstract, Signature signature, Block block) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.signature = signature;
        this.block = block;
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
        Environment.getInstance().setCurrentFunction(this);
        HierarchicalScope<Declaration> innerScope = new SymbolTable<>(_scope);
        boolean res = signature.collect(innerScope) && signature.resolve(innerScope);
        res = block.collect(innerScope) && block.resolve(innerScope) && res;
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
    public int getOffset() {
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
        return new MethodType(this, getParameterTypes(), getReturnType());
    }

    protected Type getReturnType() {
        return signature.type;
    }

    protected List<Type> getParameterTypes() {
        return signature.parameters.stream()
                .map(ParameterDeclaration::getType)
                .collect(Collectors.toList());
    }
}

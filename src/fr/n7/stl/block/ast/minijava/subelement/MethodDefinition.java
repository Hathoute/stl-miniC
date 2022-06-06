package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.MethodType;
import fr.n7.stl.util.Helper;

public class MethodDefinition implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected Signature signature;
    protected Block body;

    protected Type type;

    public MethodDefinition(boolean isStatic, boolean isFinal, boolean isAbstract, Signature signature, Block body) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.signature = signature;
        this.body = body;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> elementScope) {
        return Helper.registerDeclaration(this, elementScope);
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);
        boolean ok = Helper.startSequence(Helper.matchAll(signature.parameters, x -> x.getType().resolve(elementScope)))
                .and(signature.type.resolve(elementScope))
                .finish();

        type = new MethodType(this);
        SymbolTable<Declaration> s = new SymbolTable<>(elementScope);
        ok = Helper.startSequence(ok)
                .and(type.resolve(s))
                .and(Helper.registerDeclarations(getSignature().parameters, s))
                .and(body.collect(s))
                .and(body.resolve(s))
                .finish();

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public boolean checkType() {
        Environment.getInstance().setCurrentClassElement(this);

        boolean ok = body.checkType();
        ok = Helper.checkReturnType(body, signature.getType()) == CheckReturnCode.FINISHED && ok;

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public String getName() {
        return signature.getName();
    }

    @Override
    public Type getType() {
        return type;
    }
}

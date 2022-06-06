package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.minijava.block.ConstructorBlock;
import fr.n7.stl.block.ast.minijava.block.VoidBlock;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.MethodType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;

public class MethodDefinition implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected Signature signature;
    protected Block body;

    protected MethodType type;
    protected String qualifiedName;

    public MethodDefinition(boolean isStatic, boolean isFinal, boolean isAbstract, Signature signature, Block body) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.signature = signature;
        this.body = body;

        if (signature.type instanceof AtomicType && signature.type.equalsTo(AtomicType.VoidType)) {
            this.body = body.toSpecificBlock(VoidBlock.class);
        }
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
        Environment.getInstance().setCurrentClassElement(this);
        qualifiedName = Helper.formatQualifiedName(Environment.getInstance().getCurrentClass(), this);
        boolean ok = Helper.registerDeclaration(this, elementScope);
        Environment.getInstance().setCurrentClassElement(null);
        return ok;
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

        // TODO: Return here when implementing static methods
        int offset = -1;        // this
        for(ParameterDeclaration decl : signature.parameters) {
            offset -= decl.getType().length();
            decl.setOffset(offset);
        }

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        Environment.getInstance().setCurrentClassElement(this);
        // TODO: Return here after implementing static methods
        body.allocateMemory(Register.LB, 3);    // 3 + 1 for this
        Environment.getInstance().setCurrentClassElement(null);
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Environment.getInstance().setCurrentClassElement(this);
        Fragment bodyCode = this.body.getCode(_factory);

        if(type.getReturnType() == AtomicType.VoidType) {
            bodyCode.add(_factory.createReturn(0, 0));
        }

        bodyCode.addPrefix(getName());
        Environment.getInstance().setCurrentClassElement(null);
        return bodyCode;
    }

    @Override
    public String getRealName() {
        return signature.name;
    }

    @Override
    public String getName() {
        return qualifiedName;
    }

    @Override
    public Type getType() {
        return type;
    }
}

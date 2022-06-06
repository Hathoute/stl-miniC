package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;

import java.util.List;

public class MainClassDefinition implements Element {

    protected List<ParameterDeclaration> parameters;
    protected Block body;

    public MainClassDefinition(List<ParameterDeclaration> parameters, Block body) {
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public boolean collect(HierarchicalScope<Element> globalScope) {
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Element> globalScope) {
        SymbolTable<Declaration> s = new SymbolTable<>(globalScope);
        return Helper.startSequence(Helper.matchAll(this.parameters, x -> x.getType().resolve(s)))
                .and(Helper.registerDeclarations(this.parameters, s))
                .and(this.body.collect(s))
                .and(this.body.resolve(s))
                .finish();
    }

    @Override
    public boolean checkType() {
        return Helper.startSequence(body.checkType())
                .and(body.checkReturnType(AtomicType.VoidType) != CheckReturnCode.TYPE_MISMATCH)
                .finish();
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        body.allocateMemory(Register.LB, 3);
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment f = body.getCode(_factory);
        f.add(_factory.createReturn(0, 0));
        f.addPrefix("entry");
        return f;
    }

    @Override
    public RecordType getAssociatedRecord() {
        throw new RuntimeException("Method should not be used!");
    }

    @Override
    public String getName() {
        return "Main";
    }

    @Override
    public Type getType() {
        throw new RuntimeException("Method should not be used!");
    }


}

package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
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
        SymbolTable s = new SymbolTable(globalScope);
        return Helper.startSequence(Helper.matchAll(this.parameters, x -> x.getType().resolve(s)))
                .and(this.body.collect(s))
                .and(this.body.resolve(s))
                .finish();
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

package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

import java.util.List;

public class ConstructorDefinition implements ClassElement {

    protected String name;
    protected List<ParameterDeclaration> parameters;
    protected Block body;

    public ConstructorDefinition(String name, List<ParameterDeclaration> parameters, Block body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);
        if(!elementScope.accepts(this)) {
            Logger.error("Duplicate constructor definition for \"" + name + "(" +
                    Helper.formatParameters(parameters) + ")\"");
            return false;
        }

        elementScope.register(this);
        Environment.getInstance().setCurrentClassElement(null);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);
        SymbolTable<Declaration> s = new SymbolTable<>(elementScope);
        boolean ok = Helper.startSequence(Helper.matchAll(parameters, x -> x.getType().resolve(s)))
                .and(Helper.registerDeclarations(parameters, s))
                .and(body.collect(s))
                .and(body.resolve(s))
                .finish();

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public String getName() {
        return name + "#constructor";
    }

    @Override
    public Type getType() {
        throw new RuntimeException("Method is not defined");
    }
}
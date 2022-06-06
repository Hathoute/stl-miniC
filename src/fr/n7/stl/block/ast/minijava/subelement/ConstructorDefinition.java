package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.minijava.block.ConstructorBlock;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

import java.util.List;

public class ConstructorDefinition implements ClassElement {

    protected String name;
    protected List<ParameterDeclaration> parameters;
    protected ConstructorBlock body;

    protected String qualifiedName;

    public ConstructorDefinition(String name, List<ParameterDeclaration> parameters, Block body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body.toSpecificBlock(ConstructorBlock.class);
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);
        qualifiedName = Helper.formatQualifiedName(Environment.getInstance().getCurrentClass(), this);

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
    public boolean checkType() {
        Environment.getInstance().setCurrentClassElement(this);

        boolean ok = body.checkType();
        ok = body.checkReturnType(AtomicType.VoidType) != CheckReturnCode.TYPE_MISMATCH && ok;

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        Environment.getInstance().setCurrentClassElement(this);
        body.allocateMemory(Register.LB, 3);
        Environment.getInstance().setCurrentClassElement(null);
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Environment.getInstance().setCurrentClassElement(this);

        int paramSize = parameters.stream()
                .mapToInt(x -> x.getType().length())
                .sum();

        Fragment bodyCode = this.body.getCode(_factory);
        bodyCode.add(_factory.createLoad(Register.LB, 3, 1));
        bodyCode.add(_factory.createReturn(1, paramSize));
        bodyCode.addPrefix(getName());
        Environment.getInstance().setCurrentClassElement(null);
        return bodyCode;
    }

    @Override
    public String getRealName() {
        return name;
    }

    @Override
    public String getName() {
        return qualifiedName;
    }

    @Override
    public Type getType() {
        throw new RuntimeException("Method is not defined");
    }

    public boolean compatibleWith(List<Type> types) {
        if(parameters.size() != types.size()) {
            return false;
        }

        for (int i = 0; i < parameters.size(); i++) {
            if (!types.get(i).compatibleWith(parameters.get(i).getType())) {
                return false;
            }
        }

        return true;
    }
}

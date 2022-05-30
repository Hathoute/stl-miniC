package fr.n7.stl.block.ast.element.subelement;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class Signature {

    protected Type type;
    protected String name;
    protected List<ParameterDeclaration> parameters;

    public Signature(Type type, String name, List<ParameterDeclaration> parameters) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
    }

    public boolean collect(HierarchicalScope<Declaration> _scope) {
        for (ParameterDeclaration pd : parameters) {
            if(!_scope.accepts(pd)) {
                return false;
            }
            _scope.register(pd);
        }
        return true;
    }

    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        return parameters.stream().allMatch(x -> x.getType().resolve(_scope)) && type.resolve(_scope);
    }

    public int allocateMemory(Register _register, int _offset) {
        return 0;
    }

    public Fragment getCode(TAMFactory _factory) {
        return null;
    }
}
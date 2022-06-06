package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.Type;

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

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<ParameterDeclaration> getParameters() {
        return parameters;
    }
}

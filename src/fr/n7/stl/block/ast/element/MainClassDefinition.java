package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.element.subelement.MethodDefinition;
import fr.n7.stl.block.ast.element.subelement.Signature;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;

import java.util.ArrayList;
import java.util.List;

public class MainClassDefinition extends AbstractClassDefinition {

    public MainClassDefinition(List<ParameterDeclaration> parameters, Block block) {
        super("Main", new ArrayList<>());
        Signature s = new Signature(AtomicType.VoidType, "main", parameters);
        MethodDefinition m = new MethodDefinition(true, false, false, s, block);
        elements.add(m);
    }

    @Override
    public Type getType() {
        return null;
    }
}

package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.element.subelement.Method;
import fr.n7.stl.block.ast.element.subelement.Signature;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.AtomicType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass extends AbstractClass {

    public MainClass(List<ParameterDeclaration> parameters, Block block) {
        super("Main", new ArrayList<>());
        Signature s = new Signature(AtomicType.VoidType, "main", parameters);
        Method m = new Method(true, false, false, s, block);
        elements.add(m);
    }
}

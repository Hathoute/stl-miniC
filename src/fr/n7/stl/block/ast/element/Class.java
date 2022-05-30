package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.element.subelement.ClassElement;

import java.util.List;

public class Class extends AbstractClass {

    protected boolean isFinal;
    protected boolean isAbstract;

    public Class(boolean isFinal, boolean isAbstract, String name, List<ClassElement> elements) {
        super(name, elements);
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
    }
}

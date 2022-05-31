package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.block.ast.type.Type;

import java.util.List;

public class ClassDefinition extends AbstractClassDefinition {

    protected boolean isFinal;
    protected boolean isAbstract;

    public ClassDefinition(boolean isFinal, boolean isAbstract, String name, List<ClassElement> elements) {
        super(name, elements);
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public Type getType() {
        return null;
    }
}

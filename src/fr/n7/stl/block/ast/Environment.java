package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.element.Element;
import fr.n7.stl.block.ast.element.subelement.Method;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;

public class Environment {

    private static Environment instance;

    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();
        }

        return instance;
    }

    private Method currentFunction;
    private Element currentElement;

    private Environment() {

    }

    public void setCurrentFunction(Method decl) {
        this.currentFunction = decl;
    }

    public Method getCurrentFunction() {
        return this.currentFunction;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    public void setCurrentElement(Element currentElement) {
        this.currentElement = currentElement;
    }
}

package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.element.Element;
import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.block.ast.element.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Scope;

public class Environment {

    private static Environment instance;

    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();
        }

        return instance;
    }

    private MethodDefinition currentFunction;
    private Element currentElement;
    private Scope<Element> globalElements;

    private Environment() {

    }

    public void setCurrentFunction(MethodDefinition decl) {
        this.currentFunction = decl;
    }

    public MethodDefinition getCurrentFunction() {
        return this.currentFunction;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    public void setCurrentElement(Element currentElement) {
        this.currentElement = currentElement;
    }

    public void setGlobalContext(Scope<Element> globalElements) {
        this.globalElements = globalElements;
    }

    public Element getElement(String name) {
        return this.globalElements.get(name);
    }

    public ClassElement getClassElementInCurrentContext(String elementName) {
        Scope<ClassElement> context = currentElement.getContext();
        return context.get(elementName);
    }
}

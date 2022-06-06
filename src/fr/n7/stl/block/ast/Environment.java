package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.minijava.ClassDefinition;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;

public class Environment {

    private static Environment instance;

    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();
        }

        return instance;
    }
    private Environment() {

    }

    //region miniC
    private FunctionDeclaration currentFunction;

    public void setCurrentFunction(FunctionDeclaration decl) {
        this.currentFunction = decl;
    }

    public FunctionDeclaration getCurrentFunction() {
        return this.currentFunction;
    }
    //endregion


    //region miniJava
    private ClassDefinition currentClass;
    private ClassElement currentClassElement;

    public ClassDefinition getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(ClassDefinition currentClass) {
        this.currentClass = currentClass;
    }

    public ClassElement getCurrentClassElement() {
        return currentClassElement;
    }

    public void setCurrentClassElement(ClassElement currentClassElement) {
        this.currentClassElement = currentClassElement;
    }

    //endregion
}

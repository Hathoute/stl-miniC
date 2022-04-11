package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;

public class Environment {

    private static Environment instance;

    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();
        }

        return instance;
    }

    private FunctionDeclaration currentFunction;

    private Environment() {

    }

    public void setCurrentFunction(FunctionDeclaration decl) {
        this.currentFunction = decl;
    }

    public FunctionDeclaration getCurrentFunction() {
        return this.currentFunction;
    }
}

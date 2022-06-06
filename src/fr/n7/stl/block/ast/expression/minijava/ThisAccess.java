package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.minijava.ClassDefinition;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.ConstructorDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.ThisType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisAccess implements AssignableExpression {

    protected Type type;

    @Override
    public String toString() {
        return "this";
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return true;
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        this.type = new ThisType();
        this.type.resolve(_scope);
        return true;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean isConstant() {
        throw new RuntimeException("Method is not defined");
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        throw new RuntimeException("Method is not defined");
    }
}

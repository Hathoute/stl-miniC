package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;

public interface ClassElement extends Declaration {

    boolean collect(HierarchicalScope<Declaration> elementScope);
    boolean resolve(HierarchicalScope<Declaration> elementScope);
    boolean checkType();

}

package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;

public interface Element extends Declaration {

    boolean collect(HierarchicalScope<Element> globalScope);
    boolean resolve(HierarchicalScope<Element> globalScope);
    boolean checkType();

}

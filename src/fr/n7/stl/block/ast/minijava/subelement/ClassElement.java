package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public interface ClassElement extends Declaration {

    boolean collect(HierarchicalScope<Declaration> elementScope);
    boolean resolve(HierarchicalScope<Declaration> elementScope);
    boolean checkType();
    int allocateMemory(Register register, int offset);
    Fragment getCode(TAMFactory _factory);

    String getRealName();
}

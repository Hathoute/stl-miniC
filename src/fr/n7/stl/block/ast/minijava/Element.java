package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public interface Element extends Declaration {

    boolean collect(HierarchicalScope<Element> globalScope);
    boolean resolve(HierarchicalScope<Element> globalScope);
    boolean checkType();
    int allocateMemory(Register register, int offset);
    Fragment getCode(TAMFactory _factory);

    RecordType getAssociatedRecord();
}

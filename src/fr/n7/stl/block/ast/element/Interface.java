package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.element.subelement.Signature;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class Interface implements Element {

    protected String name;
    protected List<Signature> elements;

    public Interface(String name, List<Signature> elements) {
        this.name = name;
        this.elements = elements;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean collect(HierarchicalScope<ElementDeclaration> _scope) {
        return false;
    }

    @Override
    public boolean resolve(HierarchicalScope<ElementDeclaration> _scope) {
        return false;
    }

    @Override
    public boolean checkType() {
        return false;
    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        return null;
    }
}

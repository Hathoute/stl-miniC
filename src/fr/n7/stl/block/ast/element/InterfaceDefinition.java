package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.block.ast.element.subelement.MethodSignatureDefinition;
import fr.n7.stl.block.ast.element.subelement.Signature;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class InterfaceDefinition implements Element {

    protected String name;
    protected List<MethodSignatureDefinition> elements;

    protected Scope<ClassElement> interfaceElements;

    public InterfaceDefinition(String name, List<MethodSignatureDefinition> elements) {
        this.name = name;
        this.elements = elements;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public Scope<ClassElement> getContext() {
        return interfaceElements;
    }

    @Override
    public boolean collect(Scope<Element> _scope) {
        return false;
    }

    @Override
    public boolean resolve(Scope<Element> _scope) {
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

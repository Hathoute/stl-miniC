package fr.n7.stl.block.ast.element;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public abstract class AbstractClassDefinition implements Element {

    protected String name;
    protected List<ClassElement> elements;
    protected HierarchicalScope<ClassElement> elementsScope;

    public AbstractClassDefinition(String name, List<ClassElement> elements) {
        this.name = name;
        this.elements = elements;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Scope<ClassElement> getContext() {
        return elementsScope;
    }

    @Override
    public boolean collect(Scope<Element> _scope) {
        Environment.getInstance().setCurrentElement(this);
        if(!_scope.accepts(this)) {
            return false;
        }
        _scope.register(this);

        elementsScope = new SymbolTable<>(elementsScope);
        boolean ok = elements.stream().allMatch(x -> x.collect(elementsScope));
        Environment.getInstance().setCurrentElement(null);

        return ok;
    }

    @Override
    public boolean resolve(Scope<Element> _scope) {
        Environment.getInstance().setCurrentElement(this);
        boolean ok = elements.stream().allMatch(x -> x.resolve(elementsScope));
        Environment.getInstance().setCurrentElement(null);

        return ok;
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

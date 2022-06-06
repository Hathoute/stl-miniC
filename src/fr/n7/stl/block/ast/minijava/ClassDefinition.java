package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.minijava.subelement.AttributeDefinition;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.ConstructorDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ClassDefinition implements Element {

    protected String name;
    protected List<ClassElement> classElements;

    protected List<AttributeDefinition> attributes;
    protected List<MethodDefinition> methods;
    protected List<ConstructorDefinition> constructors;


    public ClassDefinition(String name, List<ClassElement> classElements) {
        this.name = name;
        this.classElements = classElements;

        this.attributes = classElements.stream()
                .filter(x -> x instanceof AttributeDefinition)
                .map(x -> (AttributeDefinition) x)
                .collect(Collectors.toList());
        this.methods = classElements.stream()
                .filter(x -> x instanceof MethodDefinition)
                .map(x -> (MethodDefinition) x)
                .collect(Collectors.toList());
        this.constructors = classElements.stream()
                .filter(x -> x instanceof ConstructorDefinition)
                .map(x -> (ConstructorDefinition) x)
                .collect(Collectors.toList());
    }

    public AttributeDefinition getAttribute(String name) {
        return attributes.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public MethodDefinition getMethod(String name) {
        return methods.stream()
                .filter(x -> x.getSignature().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public ConstructorDefinition getConstructor() {
        return constructors.stream()
                .findFirst()
                .orElse(null);
    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        throw new RuntimeException("Method should not be used!");
    }

    @Override
    public boolean collect(HierarchicalScope<Element> globalScope) {
        if(!globalScope.accepts(this)) {
            Logger.error("Duplicate definition of \"" + name + "\"");
            return false;
        }

        globalScope.register(this);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Element> globalScope) {
        SymbolTable s = new SymbolTable(globalScope);
        return Helper.matchAll(classElements, x -> x.resolve(s));
    }
}

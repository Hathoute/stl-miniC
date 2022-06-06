package fr.n7.stl.block.ast.minijava;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.minijava.subelement.AttributeDefinition;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.ConstructorDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
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

    protected RecordType associatedRecord;

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

        this.associatedRecord = new RecordType("#record_" + name);
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
    public RecordType getAssociatedRecord() {
        return associatedRecord;
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
        Environment.getInstance().setCurrentClass(this);

        if(!globalScope.accepts(this)) {
            Logger.error("Duplicate definition of \"" + name + "\"");
            return false;
        }
        globalScope.register(this);

        Environment.getInstance().setCurrentClass(null);
        return true;
    }

    @Override
    public boolean resolve(HierarchicalScope<Element> globalScope) {
        Environment.getInstance().setCurrentClass(this);

        SymbolTable<Declaration> s = new SymbolTable<>(globalScope);
        boolean ok = Helper.startSequence(associatedRecord.resolve(s))
                .and(Helper.matchAll(classElements, x -> x.collect(s)))
                .and(Helper.matchAll(classElements, x -> x.resolve(s)))
                .finish();

        Environment.getInstance().setCurrentClass(null);
        return ok;
    }

    @Override
    public boolean checkType() {
        Environment.getInstance().setCurrentClass(this);

        boolean ok = Helper.matchAll(classElements, ClassElement::checkType);

        Environment.getInstance().setCurrentClass(null);
        return ok;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        Environment.getInstance().setCurrentClass(this);

        int attributeOffset = 0;
        for (AttributeDefinition a : attributes) {
            attributeOffset += a.allocateMemory(Register.LB, attributeOffset);
        }

        for (MethodDefinition m : methods) {
            offset += m.allocateMemory(register, offset);
        }

        for (ConstructorDefinition c : constructors) {
            offset += c.allocateMemory(register, offset);
        }

        Environment.getInstance().setCurrentClass(null);
        return offset;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Environment.getInstance().setCurrentClass(this);
        Fragment f = _factory.createFragment();

        // Add methods
        for (MethodDefinition m : methods) {
            f.append(m.getCode(_factory));
        }

        // Add constructors
        for (ConstructorDefinition c : constructors) {
            f.append(c.getCode(_factory));
        }

        f.addComment("class " + getName());

        Environment.getInstance().setCurrentClass(null);
        return f;
    }
}

package fr.n7.stl.block.ast.minijava.subelement;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.minijava.ClassDefinition;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Helper;
import fr.n7.stl.util.Logger;

public class AttributeDefinition implements ClassElement {

    protected boolean isStatic;
    protected boolean isFinal;
    protected Type type;
    protected String name;
    protected Expression value;

    protected String qualifiedName;
    protected Register register;
    protected int offset;

    public AttributeDefinition(boolean isStatic, boolean isFinal, Type type, String name, Expression value) {
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return qualifiedName;
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);
        qualifiedName = Helper.formatQualifiedName(Environment.getInstance().getCurrentClass(), this);

        boolean ok = true;
        /*if(!elementScope.accepts(this)) {
            Logger.error("Duplicate field definition for \"" + name + "\"");
            ok = false;
        }
        elementScope.register(this);*/

        // Add this field to class's record
        ClassDefinition klass = Environment.getInstance().getCurrentClass();
        FieldDeclaration field = new FieldDeclaration(name, type);
        if(!klass.getAssociatedRecord().accepts(field)) {
            Logger.error("Duplicate field definition for \"" + name + "\"");
            ok = false;
        }
        klass.getAssociatedRecord().add(field);

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> elementScope) {
        Environment.getInstance().setCurrentClassElement(this);

        boolean ok = type.resolve(elementScope);
        if(value != null) {
            SymbolTable<Declaration> s = new SymbolTable<>(elementScope);
            ok = value.collectAndBackwardResolve(s) && ok;
            ok = value.fullResolve(s) && ok;
        }

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public boolean checkType() {
        Environment.getInstance().setCurrentClassElement(this);

        boolean ok = true;
        if(value != null) {
            ok = value.getType().compatibleWith(type);
        }

        Environment.getInstance().setCurrentClassElement(null);
        return ok;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        this.register = register;
        this.offset = offset;
        return offset + type.length();
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        throw new RuntimeException("Method should not be defined.");
    }

    @Override
    public String getRealName() {
        return name;
    }
}

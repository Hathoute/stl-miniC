package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.minijava.InstanceType;
import fr.n7.stl.block.ast.type.minijava.ThisType;
import fr.n7.stl.util.Logger;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractObjectField implements Expression {

    protected Expression object;
    protected String name;

    protected Element element;
    protected ClassElement field;

    public AbstractObjectField(Expression _record, String _name) {
        this.object = _record;
        this.name = _name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.object + "." + this.name;
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
     */
    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        return this.object.collectAndBackwardResolve(_scope);
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
     */
    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        return this.object.fullResolve(_scope);
    }

    /**
     * Synthesized Semantics attribute to compute the type of an expression.
     * @return Synthesized Type of the expression.
     */
    public Type getType() {
        if(field != null) {
            return field.getType();
        }

        Type type = this.object.getType();
        type = Type.getRealType(type);

        if(type instanceof ThisType) {
            type = ((ThisType) type).getInstanceType();
        }

        if (type instanceof InstanceType) {
            InstanceType instanceType = (InstanceType) type;
            this.element = instanceType.getTypeDefinition();
            if (instanceType.contains(name)) {
                field = instanceType.get(name);
                return field.getType();
            }
            else {
                Logger.error("Type '" + ((InstanceType) type).getName() + "' has no element named \"" + name + "\"");
            }
        }
        else {
            Logger.error("'" + this.object.toString() + "' is not an InstanceType.");
        }

        return AtomicType.ErrorType;
    }
}
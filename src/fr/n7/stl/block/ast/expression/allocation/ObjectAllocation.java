/**
 *
 */
package fr.n7.stl.block.ast.expression.allocation;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

/**
 * @author Marc Pantel
 *
 */
public class ObjectAllocation implements Expression {

	protected Type element;
	protected List<Expression> parameters;

	public ObjectAllocation(Type _element, List<Expression> parameters) {
		this.element = _element;
		this.parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "new " + this.element;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = this.element.resolve();
		for (Expression expr : parameters) {
			ok = expr.collectAndBackwardResolve(_scope) && ok;
		}

		return ok;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment thisCode = _factory.createFragment();
		thisCode.add(_factory.createLoadL(this.element.length()));
		thisCode.add(Library.MAlloc);
		return thisCode;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

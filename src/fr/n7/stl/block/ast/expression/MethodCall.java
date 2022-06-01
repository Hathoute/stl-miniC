/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.element.subelement.MethodDefinition;
import fr.n7.stl.block.ast.expression.accessible.FieldAccess;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.accessible.VariableAccess;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
 *
 */
public class MethodCall implements Expression {

	protected Expression method;

	protected MethodDefinition definition;

	protected List<Expression> arguments;

	public MethodCall(Expression method, List<Expression> _arguments) {
		this.method = method;
		this.definition = null;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = this.method.toString() + "(";
		Iterator<Expression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += ", " + _iter.next();
		}
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = method.collectAndBackwardResolve(_scope);
		for(Expression arg : arguments) {
			ok = arg.collectAndBackwardResolve(_scope) && ok;
		}

		return ok;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = method.fullResolve(_scope);
		if(method instanceof FieldAccess) {

		}
		else if(method instanceof IdentifierAccess) {

		}
		else {
			ok = false;
		}

		for(Expression arg : arguments) {
			ok = arg.fullResolve(_scope) && ok;
		}

		return ok;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return function.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment thisCode = _factory.createFragment();
		int i = this.arguments.size() - 1;
		for(; i >= 0; i--) {
			thisCode.append(this.arguments.get(i).getCode(_factory));
		}
		//thisCode.add(_factory.createCall(this.name, Register.LB));
		return thisCode;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

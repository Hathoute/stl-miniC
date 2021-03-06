/**
 * 
 */
package fr.n7.stl.block.ast.instruction.declaration;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a function declaration.
 * @author Marc Pantel
 */
public class FunctionDeclaration implements Instruction, Declaration {

	/**
	 * Name of the function
	 */
	protected String name;
	
	/**
	 * AST node for the returned type of the function
	 */
	protected Type type;
	
	/**
	 * List of AST nodes for the formal parameters of the function
	 */
	protected List<ParameterDeclaration> parameters;
	
	/**
	 * @return the parameters
	 */
	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}

	/**
	 * AST node for the body of the function
	 */
	protected Block body;

	/**
	 * Builds an AST node for a function declaration
	 * @param _name : Name of the function
	 * @param _type : AST node for the returned type of the function
	 * @param _parameters : List of AST nodes for the formal parameters of the function
	 * @param _body : AST node for the body of the function
	 */
	public FunctionDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		this.name = _name;
		this.type = _type;
		this.parameters = _parameters;
		this.body = _body;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = this.type + " " + this.name + "( ";
		Iterator<ParameterDeclaration> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " )" + this.body;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this.type;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		if(Environment.getInstance().getCurrentFunction() != null) {
			Logger.error("Nested functions are not supported (" + this.name
					+ " inside " + Environment.getInstance().getCurrentFunction().name + ").");
			return false;
		}

		Environment.getInstance().setCurrentFunction(this);

		HierarchicalScope<Declaration> funcScope = new SymbolTable(_scope);
		boolean result = true;
		for(ParameterDeclaration decl : parameters) {
			if(!funcScope.accepts(decl)) {
				Logger.error("Parameter " + decl.name + " already defined.");
				result = false;
				continue;
			}
			funcScope.register(decl);
		}
		_scope.register(this);
		result = result && body.collect(funcScope);

		Environment.getInstance().setCurrentFunction(null);

		return result;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		Environment.getInstance().setCurrentFunction(this);
		type.resolve(_scope);
		for(ParameterDeclaration decl : parameters) {
			decl.getType().resolve(_scope);
		}
		boolean result = body.resolve(_scope);
		Environment.getInstance().setCurrentFunction(null);
		return result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Environment.getInstance().setCurrentFunction(this);
		CheckReturnCode code = body.checkReturnType(type);
		boolean result = code == CheckReturnCode.FINISHED
				|| (type == AtomicType.VoidType && code == CheckReturnCode.CONTINUE);
		result = result && body.checkType();

		int offset = 0;
		for(ParameterDeclaration decl : parameters) {
			offset -= decl.getType().length();
			decl.setOffset(offset);
		}

		Environment.getInstance().setCurrentFunction(null);
		return result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		Environment.getInstance().setCurrentFunction(this);
		body.allocateMemory(Register.LB, 3);
		Environment.getInstance().setCurrentFunction(null);
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Environment.getInstance().setCurrentFunction(this);
		Fragment bodyCode = this.body.getCode(_factory);

		if(type == AtomicType.VoidType) {
			bodyCode.add(_factory.createReturn(0, 0));
		}

		bodyCode.addPrefix(this.name);
		Environment.getInstance().setCurrentFunction(null);
		return bodyCode;
	}

	@Override
	public CheckReturnCode checkReturnType(Type type) {
		return CheckReturnCode.CONTINUE;
	}

}

/**
 * 
 */
package fr.n7.stl.block.ast.instruction.minijava;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a return instruction.
 * @author Marc Pantel
 *
 */
public class MethodReturn implements Instruction {

	protected Expression value;
	protected MethodDefinition method;

	public MethodReturn(Expression _value) {
		this.value = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "return " + (this.value == null ? "" : this.value) + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return value == null || value.collectAndBackwardResolve(_scope);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return value == null || value.fullResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		if(Environment.getInstance().getCurrentClassElement() == null
			|| !(Environment.getInstance().getCurrentClassElement() instanceof MethodDefinition)) {
			Logger.error("Found return outside the body of a function.");
			return false;
		}

		method = (MethodDefinition) Environment.getInstance().getCurrentClassElement();
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Type returnType = method.getSignature().getType();
		int paramSize = method.getSignature().getParameters().stream()
				.mapToInt(x -> x.getType().length())
				.sum();

		// TODO: Return here when implementing static methods
		paramSize = paramSize + 1;	// this as parameter.

		Fragment result = this.value == null ? _factory.createFragment() :  this.value.getCode(_factory);
		result.add(_factory.createReturn(returnType.length(), paramSize));
		return result;
	}

	@Override
	public CheckReturnCode checkReturnType(Type type) {
		return (value == null && type.equalsTo(AtomicType.VoidType) ||
				value.getType().equalsTo(type)) ? CheckReturnCode.FINISHED : CheckReturnCode.TYPE_MISMATCH;
	}

}

/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import java.util.Optional;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.UniqueIdProvider;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		if(elseBranch != null){
			return condition.collectAndBackwardResolve(_scope) && thenBranch.collect(_scope) && elseBranch.collect(_scope);
		}
		return condition.collectAndBackwardResolve(_scope) && thenBranch.collect(_scope);

	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		if(elseBranch != null) {
			return condition.fullResolve(_scope) && thenBranch.resolve(_scope) && elseBranch.resolve(_scope);
		}
		return condition.fullResolve(_scope) && thenBranch.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		return this.condition.getType().equalsTo(AtomicType.BooleanType);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		if(elseBranch != null) {
			return thenBranch.allocateMemory(_register, _offset) + elseBranch.allocateMemory(_register, _offset);
		}
		return thenBranch.allocateMemory(_register, _offset);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int uniqueId = UniqueIdProvider.GetNextId();
		String lblThen = "if_then_" + uniqueId;
		String lblExit = "if_exit_" + uniqueId;

		Fragment _condCode = this.condition.getCode(_factory);
		Fragment _thenCode = this.thenBranch.getCode(_factory);
		Fragment _elseCode = this.elseBranch == null ? _factory.createFragment() : this.elseBranch.getCode(_factory);

		_thenCode.addPrefix(lblThen);
		_thenCode.addSuffix(lblExit);

		Fragment thisCode = _factory.createFragment();
		thisCode.append(_condCode);
		thisCode.add(_factory.createJumpIf(lblThen, 1));
		thisCode.append(_elseCode);
		thisCode.add(_factory.createJump(lblExit));
		thisCode.append(_thenCode);

		return thisCode;
	}

	@Override
	public CheckReturnCode checkReturnType(Type type) {
		CheckReturnCode thenCode = thenBranch.checkReturnType(type);
		CheckReturnCode elseCode = elseBranch == null ? CheckReturnCode.CONTINUE : elseBranch.checkReturnType(type);

		if(thenCode == CheckReturnCode.TYPE_MISMATCH || elseCode == CheckReturnCode.TYPE_MISMATCH) {
			return CheckReturnCode.TYPE_MISMATCH;
		}
		else if(thenCode == CheckReturnCode.CONTINUE || elseCode == CheckReturnCode.CONTINUE) {
			return CheckReturnCode.CONTINUE;
		}
		else {
			return CheckReturnCode.FINISHED;
		}
	}

}

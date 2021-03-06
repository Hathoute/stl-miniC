/**
 * 
 */
package fr.n7.stl.block.ast;

import java.util.ArrayList;
import java.util.List;

import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import fr.n7.stl.util.UniqueIdProvider;

/**
 * Represents a Block node in the Abstract Syntax Tree node for the Bloc language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 * 
 * @author Marc Pantel
 *
 */
public class Block {

	/**
	 * Sequence of instructions contained in a block.
	 */
	protected List<Instruction> instructions;

	protected HierarchicalScope<Declaration> blockScope;

	private int allocatedSize;

	/**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> _instructions) {
		this.instructions = _instructions;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n" ;
	}
	
	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and check
	 * that the declaration are allowed.
	 * @param _scope Inherited Scope attribute that contains the identifiers defined previously
	 * in the context.
	 * @return Synthesized Semantics attribute that indicates if the identifier declaration are
	 * allowed.
	 */
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		this.blockScope = new SymbolTable(_scope);
		boolean ok = true;

		for (Instruction instr : this.instructions) {
			ok = instr.collectAndBackwardResolve(blockScope) && ok;
		}
		
		return ok;
	}

	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined and
	 * associate all identifiers uses with their definitions.
	 * @param _scope Inherited Scope attribute that contains the defined identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used in the
	 * block have been previously defined.
	 */
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;

		for (Instruction instr : this.instructions) {
			ok = instr.fullResolve(this.blockScope) && ok;
		}
		
		return ok;
	}

	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */	
	public boolean checkType() {
		boolean ok = true;

		for (Instruction instr : this.instructions) {
			ok = instr.checkType() && ok;
		}

		return ok;
	}

	public CheckReturnCode checkReturnType(Type type) {
		CheckReturnCode returnCode = CheckReturnCode.CONTINUE;

		int totalInstr = this.instructions.size();
		int curInstr = 0;
		for(; curInstr < totalInstr; curInstr++) {
			Instruction instr = this.instructions.get(curInstr);
			CheckReturnCode code = instr.checkReturnType(type);

			if(code != CheckReturnCode.CONTINUE) {
				returnCode = code;
				break;
			}
		}

		if(curInstr + 1 < totalInstr) {
			Logger.warning("Unreachable code detected.");
		}

		return returnCode;
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated memory. 
	 * @param _register Inherited Register associated to the address of the variables.
	 * @param _offset Inherited Current offset for the address of the variables.
	 */	
	public int allocateMemory(Register _register, int _offset) {
		allocatedSize = 0;
		for(Instruction instr : this.instructions) {
			allocatedSize += instr.allocateMemory(_register, _offset + allocatedSize);
		}

		// Variables declared in a block are only visible inside the block.
		return 0;
	}

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * @param _factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */
	public Fragment getCode(TAMFactory _factory) {
		List<Instruction> callableInst = new ArrayList<>();

		Fragment thisFragment = _factory.createFragment();

		// Allocate memory for block variables
		thisFragment.add(_factory.createPush(this.allocatedSize));

		for(Instruction instr : this.instructions) {
			if(instr instanceof FunctionDeclaration) {
				callableInst.add(instr);
				continue;
			}

			Fragment f = instr.getCode(_factory);
			thisFragment.append(f);

			String line = instr.toString();
			line = line.endsWith("\n") ? line.substring(0, line.length() - 1) : line;
			line = line.replaceAll("\n", "\n;");
			f.addComment("");
			f.addComment(line);
		}

		thisFragment.add(_factory.createPop(0, this.allocatedSize));

		if(callableInst.size() > 0) {
			Fragment functionsCode = _factory.createFragment();
			String lblSkipFuncs = "block_end_" +  UniqueIdProvider.GetNextId();

			functionsCode.add(_factory.createJump(lblSkipFuncs));
			for (Instruction instr : callableInst) {
				functionsCode.append(instr.getCode(_factory));
			}

			functionsCode.addSuffix(lblSkipFuncs);
			thisFragment.append(functionsCode);
		}

		return thisFragment;
	}

}

/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.element.Element;
import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.block.ast.element.subelement.MethodDefinition;
import fr.n7.stl.block.ast.element.subelement.MethodSignatureDefinition;
import fr.n7.stl.util.BlockSemanticsError;
import fr.n7.stl.util.Logger;

import java.util.List;

/**
 * Implementation of the Abstract Syntax Tree node for a named type.
 * 
 * @author Marc Pantel
 *
 */
public class MethodType implements Type {

	protected ClassElement definition;
	protected Type returnType;
	protected List<Type> parameterTypes;

	public MethodType(ClassElement _declaration, List<Type> parameterTypes, Type returnType) {
		this.definition = _declaration;
		this.parameterTypes = parameterTypes;
		this.returnType = returnType;

		if(!(definition instanceof MethodDefinition
			|| definition instanceof MethodSignatureDefinition)) {
			throw new IllegalArgumentException("Cannot initialize MethodType:" +
					" argument must be of type Method(Signature)Definition");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if(!(_other instanceof MethodType mt)) {
			return false;
		}

		if(mt.parameterTypes.size() != this.parameterTypes.size()) {
			return false;
		}

		if(mt.returnType != null && !this.returnType.equalsTo(mt.returnType)) {
			return false;
		}

		for (int i = 0; i < this.parameterTypes.size(); i++) {
			if(!this.parameterTypes.get(i).equalsTo(mt.parameterTypes.get(i))) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if(!(_other instanceof MethodType mt)) {
			return false;
		}

		if(mt.parameterTypes.size() != this.parameterTypes.size()) {
			return false;
		}

		if(mt.returnType != null && !this.returnType.compatibleWith(mt.returnType)) {
			return false;
		}

		for (int i = 0; i < this.parameterTypes.size(); i++) {
			if(!this.parameterTypes.get(i).compatibleWith(mt.parameterTypes.get(i))) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		throw new SemanticsUndefinedException("Illegal access to merge: cannot merge MethodType");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.definition.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve() {
		return this.returnType.resolve() && this.parameterTypes.stream().allMatch(Type::resolve);
	}

	public Type getReturnType() {
		return this.returnType;
	}

}

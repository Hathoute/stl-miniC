/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.element.Element;
import fr.n7.stl.block.ast.element.subelement.ClassElement;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a named type.
 * 
 * @author Marc Pantel
 *
 */
public class MethodType implements Type {

	private ClassElement definition;
	public String name;

	public MethodType(String _name) {
		this.name = _name;
		this.definition = null;
	}

	public MethodType(ClassElement _declaration) {
		this.definition = _declaration;
		this.name = _declaration.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		_other = Type.getRealType(_other);

		return Type.getRealType(this).equalsTo(_other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		_other = Type.getRealType(_other);

		return Type.getRealType(this).compatibleWith(_other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		_other = Type.getRealType(_other);

		return Type.getRealType(this).merge(_other);
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
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve() {
		if (this.definition == null) {
			Element base = Environment.getInstance().getElement(name);
			if (base != null) {
				this.definition = base;
				return true;
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;
			}
		} else {
			return true;
		}
	}

}

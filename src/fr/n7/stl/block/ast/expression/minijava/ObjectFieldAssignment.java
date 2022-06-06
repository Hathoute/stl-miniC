/**
 * 
 */
package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.AbstractObjectField;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a field in a record.
 * @author Marc Pantel
 *
 */
public class ObjectFieldAssignment extends AbstractObjectField implements AssignableExpression {

	/**
	 * Construction for the implementation of a record field assignment expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field assignment expression.
	 * @param _name Name of the field in the record field assignment expression.
	 */
	public ObjectFieldAssignment(AssignableExpression _record, String _name) {
		super(_record, _name);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.FieldAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField implements Expression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment objCode = this.record.getCode(_factory);

		int totalLen = this.record.getType().length();
		int offset = this.field.getOffset();
		int fieldLen = this.field.getType().length();

		int topRemove = totalLen - offset - fieldLen;
		int bottomRemove = offset;

		if(topRemove > 0) {
			objCode.add(_factory.createPop(0, topRemove));
		}
		if(bottomRemove > 0) {
			objCode.add(_factory.createPop(fieldLen, bottomRemove));
		}

		return objCode;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

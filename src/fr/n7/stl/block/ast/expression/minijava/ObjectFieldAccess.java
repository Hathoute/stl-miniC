/**
 * 
 */
package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.expression.AbstractObjectField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.minijava.subelement.AttributeDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class ObjectFieldAccess extends AbstractObjectField implements Expression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public ObjectFieldAccess(Expression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment objCode = this.object.getCode(_factory);

		if (field instanceof AttributeDefinition) {
			RecordType r = element.getAssociatedRecord();
			FieldDeclaration fd = r.get(name);

			int totalLen = r.length();
			int offset = fd.getOffset();
			int fieldLen = fd.getType().length();

			int topRemove = totalLen - offset - fieldLen;
			int bottomRemove = offset;

			objCode.add(_factory.createLoadI(r.length()));

			if(topRemove > 0) {
				objCode.add(_factory.createPop(0, topRemove));
			}
			if(bottomRemove > 0) {
				objCode.add(_factory.createPop(fieldLen, bottomRemove));
			}

			return objCode;
		}

		// For methods, we let MethodCall generate code.
		return objCode;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

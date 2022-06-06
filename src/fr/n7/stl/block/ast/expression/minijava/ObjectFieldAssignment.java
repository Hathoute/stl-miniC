/**
 * 
 */
package fr.n7.stl.block.ast.expression.minijava;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.AbstractObjectField;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.minijava.subelement.AttributeDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
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
		Fragment objCode = this.object.getCode(_factory);
		if (!(this.object instanceof ThisAccess)) {
			objCode.add(_factory.createLoadI(1));
		}

		if (this.field instanceof AttributeDefinition) {
			RecordType r = element.getAssociatedRecord();
			FieldDeclaration fd = r.get(name);

			if(fd.getOffset() > 0) {
				objCode.add(_factory.createLoadL(fd.getOffset()));
				objCode.add(Library.IAdd);
			}

			return objCode;
		}

		return objCode;
	}

	@Override
	public boolean isConstant() {
		return false;
	}
}

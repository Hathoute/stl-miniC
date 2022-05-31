/**
 * 
 */
package fr.n7.stl.block.ast.scope;

import fr.n7.stl.block.ast.element.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of a hierarchical scope using maps.
 * @author Marc Pantel
 *
 */
public class ElementTable implements Scope<Element> {

	private Map<String, Element> declarations;

	public ElementTable() {
		this( null );
	}

	public ElementTable(Scope<Declaration> _context) {
		this.declarations = new HashMap<String,Element>();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#get(java.lang.String)
	 */
	@Override
	public Element get(String _name) {
		if (this.declarations.containsKey(_name)) {
			return this.declarations.get(_name);
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String _name) {
		return (this.declarations.containsKey(_name));
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#accepts(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public boolean accepts(Element _declaration) {
		return (! this.contains(_declaration.getName()));
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#register(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public void register(Element _declaration) {
		if (this.accepts(_declaration)) {
			this.declarations.put(_declaration.getName(), _declaration);
		} else {
			throw new IllegalArgumentException();
		}
	}

}

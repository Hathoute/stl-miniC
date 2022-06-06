package fr.n7.stl.block.ast.minijava.block;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.Environment;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.allocation.PointerAllocation;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.minijava.InstanceType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConstructorBlock extends Block {

    /**
     * Constructor for a block.
     *
     * @param _instructions
     */
    public ConstructorBlock(List<Instruction> _instructions) {
        super(_instructions);
    }

    @Override
    public boolean collect(HierarchicalScope<Declaration> _scope) {
        this.instructions = new LinkedList<>(this.instructions);

        InstanceType thisType = new InstanceType(Environment.getInstance().getCurrentClass().getName());
        Expression value = new PointerAllocation(Environment.getInstance().getCurrentClass().getAssociatedRecord());
        VariableDeclaration declaration = new VariableDeclaration("this", thisType, value);
        ((LinkedList<Instruction>) this.instructions).addFirst(declaration);

        return super.collect(_scope);
    }
}

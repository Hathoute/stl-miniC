package fr.n7.stl.block.ast.minijava.block;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.minijava.MethodReturn;

import java.util.List;

public class VoidBlock extends Block {
    /**
     * Constructor for a block.
     *
     * @param _instructions
     */
    public VoidBlock(List<Instruction> _instructions) {
        super(_instructions);
        _instructions.add(new MethodReturn(null));
    }
}

package fr.n7.stl.util;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.CheckReturnCode;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.minijava.Element;
import fr.n7.stl.block.ast.minijava.subelement.AttributeDefinition;
import fr.n7.stl.block.ast.minijava.subelement.ClassElement;
import fr.n7.stl.block.ast.minijava.subelement.ConstructorDefinition;
import fr.n7.stl.block.ast.minijava.subelement.MethodDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Helper {

    public static <T> String formatParameters(List<T> params) {
        return params.stream().map(Objects::toString).collect(Collectors.joining(", "));
    }

    public static <T> boolean matchAll(List<T> objs, Function<T, Boolean> matcher) {
        boolean ok = true;
        for (T obj : objs) {
            ok = matcher.apply(obj) && ok;
        }
        return ok;
    }

    public static boolean registerDeclaration(Declaration declaration, Scope<Declaration> scope) {
        if(!scope.accepts(declaration)) {
            Logger.error("Duplicate definition for \"" + declaration + "\"");
            return false;
        }

        scope.register(declaration);
        return true;
    }

    public static boolean registerDeclarations(List<? extends Declaration> declarations, Scope<Declaration> scope) {
        return matchAll(declarations, x -> registerDeclaration(x, scope));
    }

    public static TruthSequence startSequence(boolean bool) {
        return new TruthSequence(bool);
    }

    public static CheckReturnCode checkReturnType(Block b, Type t) {
        CheckReturnCode code = b.checkReturnType(t);
        if(t == AtomicType.VoidType && code == CheckReturnCode.CONTINUE) {
            return CheckReturnCode.FINISHED;
        }

        return code;
    }

    public static String formatQualifiedName(Element element, ClassElement classElement) {
        if (classElement instanceof AttributeDefinition) {
            return classElement.getRealName();
        }
        if (classElement instanceof MethodDefinition) {
            return element.getName() + "#" + classElement.getRealName();
        }
        if (classElement instanceof ConstructorDefinition) {
            return element.getName() + "#constructor#";
        }

        return classElement.getRealName();
    }
}

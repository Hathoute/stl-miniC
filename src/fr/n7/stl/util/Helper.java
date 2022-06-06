package fr.n7.stl.util;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.Scope;

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
}

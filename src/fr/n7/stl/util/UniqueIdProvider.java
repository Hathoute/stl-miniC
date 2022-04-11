package fr.n7.stl.util;

public class UniqueIdProvider {

    private static int currentId = 0;

    public static int GetNextId() {
        return ++currentId;
    }
}

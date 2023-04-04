package org.stella.utils;

public class ExceptionsUtils {
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwException(Throwable exception, Object ignoredDummy) throws T
    {
        throw (T) exception;
    }

    private static void throwException(Throwable exception)
    {
        ExceptionsUtils.throwException(exception, null);
    }

    public static void throwTypeException(String message, String expected, String given){
        throwException(new IncorrectTypeException(message, expected, given));
    }

    public static void throwUndefinedException(String message){
        throwException(new UndefinedVariableException(message));
    }

    public static void throwExtensionException(String message) {
        throwException(new UnsupportedExtensionException(message));
    }
}

class IncorrectTypeException extends RuntimeException {
    public IncorrectTypeException(String exprType, String expected, String given) {
        super("\n\nTYPE_ERROR:\n" + exprType + "\n\nExpected type:\n" + expected + "\n\nBut got:\n" + given + "\n\n");
    }
}

class UndefinedVariableException extends RuntimeException {
    public UndefinedVariableException(String varName) {
        super("\n\nUNDEFINED_VARIABLE_ERROR:\nVar " + varName + " is undefined\n");
    }
}

class UnsupportedExtensionException extends RuntimeException {
    public UnsupportedExtensionException(String extensionName) {
        super("\n\nUNSUPPORTED_EXTENSION_ERROR:\n" + extensionName + "\n");
    }
}
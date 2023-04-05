package org.stella.typecheck.defined;

import org.stella.utils.ExceptionsUtils;

public class DefinedType {
    public final TypesEnum type;

    public DefinedType args;

    public DefinedType result;

    public DefinedType(TypesEnum t){
        type = t;
        args = null;
        result = null;
    }

    public DefinedType(TypesEnum t, DefinedType a, DefinedType r){
        type = t;
        args = a;
        result = r;
    }

    public boolean equals(DefinedType o, String context)
    {
        return equals(this, o, context);
    }

    @Override
    public String toString() {
        return type.name();
    }

    public String toString(int k) {
        return "\t".repeat(k) + type.name();
    }

    protected boolean equals(DefinedType a, DefinedType b, String context) {
        if (a == b)
            return true;

        if (a.type == b.type)
        {
            return equals(a.args, b.args, context)
                    && equals(a.result, b.result, context);
        }

        String aType = a.type == null ? "NULL" : a.toString();
        String bType = b.type == null ? "NULL" : b.toString();

        ExceptionsUtils.throwTypeException(context, aType, bType);

        return false;
    }
}
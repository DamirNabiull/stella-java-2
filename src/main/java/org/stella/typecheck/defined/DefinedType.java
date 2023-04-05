package org.stella.typecheck.defined;

import org.stella.utils.ExceptionsUtils;

import java.util.ArrayList;
import java.util.List;

public class DefinedType {
    public final TypesEnum type;

    public List<DefinedType> args;

    public DefinedType result;

    public DefinedType(TypesEnum t){
        type = t;
        args = new ArrayList<>();
        result = null;
    }

    public boolean equals(DefinedType o, String context)
    {
        return equals(this, o, context);
    }

    public boolean argsEqual(List<DefinedType> args, String context) {
        return equals(this.args, args, context);
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

        if (a.type == b.type && equals(a.args, b.args, context))
        {
            return equals(a.result, b.result, context);
        }

        String aType = a.type == null ? "NULL" : a.toString();
        String bType = b.type == null ? "NULL" : b.toString();

        ExceptionsUtils.throwTypeException(context, aType, bType);

        return false;
    }

    protected boolean equals(List<DefinedType> a, List<DefinedType> b, String context) {
        if (a == b)
            return true;

        if (a.size() == b.size())
        {
            boolean argsResult = true;
            for (int i = 0; i < a.size(); i++)
                argsResult &= equals(a.get(i), b.get(i), context);

            return argsResult;
        }

        ExceptionsUtils.throwTypeException(context, String.format("Args number: %d", a.size()), String.format("Args number: %d", b.size()));

        return false;
    }
}
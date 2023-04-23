package org.stella.typecheck.defined;

import org.stella.utils.ExceptionsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefinedType {
    public TypesEnum type;

    public List<DefinedType> args;

    public Map<String, DefinedType> labels;

    public DefinedType result;

    public DefinedType(TypesEnum t){
        type = t;
        args = new ArrayList<>();
        labels = new HashMap<>();
        result = null;
    }

    public boolean equals(DefinedType o)
    {
        return equals(this, o, "No context given");
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

    public String toString(String name, int k) {
        return "\t".repeat(k) + name + " : " + type.name();
    }

    protected boolean equals(DefinedType a, DefinedType b, String context) {
        if (a == b)
            return true;

//        if (a.type == b.type && a.type == TypesEnum.Sum)
//            return equalsInlInr(a.args, b.args, context);

        if (a.type == b.type)
        {
            return equals(a.args, b.args, context)
                    && equals(a.labels, b.labels, context)
                    && equals(a.result, b.result, context);
        }

        if (a.type == TypesEnum.Panic)
        {
            a.type = b.type;
            a.args = b.args;
            a.labels = b.labels;
            a.result = b.result;
            return true;
        }

        if (b.type == TypesEnum.Panic)
        {
            b.type = a.type;
            b.args = a.args;
            b.labels = a.labels;
            b.result = a.result;
            return true;
        }

        String aType = a.type == null ? "NULL" : a.toString();
        String bType = b.type == null ? "NULL" : b.toString();

        ExceptionsUtils.throwTypeException(context, aType, bType);

        return false;
    }

    protected boolean equals(List<DefinedType> a, List<DefinedType> b, String context) {
        if (a == b || a.equals(b))
            return true;

        if (a.size() == b.size())
        {
            boolean argsResult = true;
            for (int i = 0; i < a.size(); i++)
                argsResult &= equals(a.get(i), b.get(i), context);

            return argsResult;
        }

        ExceptionsUtils.throwUnexpectedSizeException(context, a.size(), b.size());

        return false;
    }

    protected boolean equals(Map<String, DefinedType> a, Map<String, DefinedType> b, String context) {
        if (a == b || a.equals(b))
            return true;

        if (a.size() == 1 && b.size() >= 1) {
            for (String label : a.keySet()) {
                if (!b.containsKey(label))
                    ExceptionsUtils.throwUnexpectedSizeException(context, a.size(), b.size()); // Add new exception

                return equals(a.get(label), b.get(label), context);
            }
        }

        if (b.size() == 1 && a.size() >= 1) {
            for (String label : b.keySet()) {
                if (!a.containsKey(label))
                    ExceptionsUtils.throwUnexpectedSizeException(context, a.size(), b.size()); // Add new exception

                return equals(a.get(label), b.get(label), context);
            }
        }

        if (a.size() == b.size()) {
            boolean labelsResult = a.keySet().equals(b.keySet());

            if (!labelsResult)
                ExceptionsUtils.throwUnexpectedSizeException(context, a.size(), b.size()); // Add new exception

            for (String label : a.keySet()) {
                labelsResult &=  equals(a.get(label), b.get(label), context);
            }

            return labelsResult;
        }

        ExceptionsUtils.throwUnexpectedSizeException(context, a.size(), b.size()); // Add new exception

        return false;
    }
}
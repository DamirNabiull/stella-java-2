package org.stella.typecheck.defined;

import org.stella.utils.ExceptionsUtils;

public class DefinedType {
    public final TypesEnum type;

    public DefinedType arg;

    public DefinedType result;

    public DefinedType(TypesEnum t){
        type = t;
        arg = null;
        result = null;
    }

    public DefinedType(TypesEnum t, DefinedType a, DefinedType r){
        type = t;
        arg = a;
        result = r;
    }

    public boolean equals(DefinedType o, String context)
    {
        return equals(this, o, context);
    }

    public boolean CheckNatRecFunParam(DefinedType o){
        boolean isFunc = o.type == TypesEnum.Fun;
        boolean isCorrectArg = o.arg.type == TypesEnum.Nat;
        boolean isCorrectReturn =
                o.result.type == TypesEnum.Fun
                        && this.equals(o.result.arg, "NatRec")
                        && this.equals(o.result.result, "NatRec");

        if (!isFunc)
            ExceptionsUtils.throwTypeException("NatRec [T3]", "Fun", o.type.name());

        if (!isCorrectArg)
            ExceptionsUtils.throwTypeException("NatRec [T3 Arg]", "Nat", o.arg.type.name());

        if (!isCorrectReturn)
            ExceptionsUtils.throwTypeException("NatRec [T3 Return]", "Fun", o.result.type.name());

        return true;
    }

    @Override
    public String toString() {
        return GetArgsAndReturns(this, 0);
    }

    private boolean equals(DefinedType a, DefinedType b, String context) {
        if (a == b)
            return true;

        if (a.type == b.type)
        {
            return equals(a.arg, b.arg, context)
                    && equals(a.result, b.result, context);
        }

        String aType = a.type == null ? "NULL" : a.toString();
        String bType = b.type == null ? "NULL" : b.toString();

        ExceptionsUtils.throwTypeException(context, aType, bType);

        return false;
    }

    private String GetArgsAndReturns(DefinedType t, int k){
        String ans = "";

        if (t.type == TypesEnum.Fun) {
            ans += "\t".repeat(k) + "FUN\n";
            ans += "\t".repeat(k + 1) + "{ARGS}\n" + GetArgsAndReturns(t.arg, k+1);
            ans += "\n" + "\t".repeat(k + 1) + "{RETURN}\n" + GetArgsAndReturns(t.result, k+1);
        } else {
            ans += "\t".repeat(k) + t.type.name();
        }

        if (k > 0)
            ans += "\n";

        return ans;
    }
}
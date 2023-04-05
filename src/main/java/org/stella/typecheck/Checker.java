package org.stella.typecheck;

import org.stella.typecheck.defined.DefinedType;
import org.stella.typecheck.defined.TypesEnum;
import org.stella.utils.ExceptionsUtils;

public class Checker {
    public static boolean CheckNatRecFunParam(DefinedType t2, DefinedType t3){
        boolean isFunc = t3.type == TypesEnum.Fun;
        boolean isCorrectArg = t3.args.type == TypesEnum.Nat;
        boolean isCorrectReturn =
                t3.result.type == TypesEnum.Fun
                        && t2.equals(t3.result.args, "NatRec")
                        && t2.equals(t3.result.result, "NatRec");

        if (!isFunc)
            ExceptionsUtils.throwTypeException("NatRec [T3]", "Fun", t3.type.name());

        if (!isCorrectArg)
            ExceptionsUtils.throwTypeException("NatRec [T3 Arg]", "Nat", t3.args.type.name());

        if (!isCorrectReturn)
            ExceptionsUtils.throwTypeException("NatRec [T3 Return]", "Fun", t3.result.type.name());

        return true;
    }
}

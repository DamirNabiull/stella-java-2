package org.stella.typecheck;

import org.stella.typecheck.defined.DefinedType;
import org.stella.typecheck.defined.TypesEnum;
import org.stella.utils.ExceptionsUtils;

import java.util.List;

public class Checker {
    public static boolean CheckNatRecFunParam(DefinedType t1, DefinedType t2, DefinedType t3){
        if (t1.type != TypesEnum.Nat)
            ExceptionsUtils.throwTypeException("NatRec [T1]", "Nat", t1.type.name());

        if (t2 == null)
            ExceptionsUtils.throwTypeException("NatRec [T2]", "[SomeType]", "NULL");

        if (t3 == null)
            ExceptionsUtils.throwTypeException("NatRec [T3]", "Fun", "NULL");

        if (t3.args.size() != 1)
            ExceptionsUtils.throwTypeException("NatRec [T3 : Incorrect number of args]", "1 arg", String.format("%d args", t3.args.size()));

        boolean isFunc = t3.type == TypesEnum.Fun;
        boolean isCorrectArg = t3.args.get(0).type == TypesEnum.Nat;
        boolean isCorrectReturn =
                t3.result.type == TypesEnum.Fun
                        && t2.equals(t3.result.args.get(0), "NatRec")
                        && t2.equals(t3.result.result, "NatRec");

        if (!isFunc)
            ExceptionsUtils.throwTypeException("NatRec [T3]", "Fun", t3.type.name());

        if (!isCorrectArg)
            ExceptionsUtils.throwTypeException("NatRec [T3 Arg]", "Nat", t3.args.get(0).type.name());

        if (!isCorrectReturn)
            ExceptionsUtils.throwTypeException("NatRec [T3 Return]", "Fun", t3.result.type.name());

        return true;
    }

    public static boolean CheckApplication(DefinedType funType, List<DefinedType> argsType) {
        if (funType.type != TypesEnum.Fun)
            ExceptionsUtils.throwTypeException("Application", "Fun", funType.type.name());

        return funType.argsEqual(argsType, "Application");
    }
}

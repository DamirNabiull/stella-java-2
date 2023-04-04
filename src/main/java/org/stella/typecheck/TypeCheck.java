package org.stella.typecheck;

import org.syntax.stella.Absyn.Program;

public class TypeCheck
{
    public static void typecheckProgram(Program program)
    {
        var visitors = new VisitTypeCheck();
        var visitor = visitors.new ProgramVisitor();
        program.accept(visitor, new Context());
    }
}

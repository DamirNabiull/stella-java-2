// File generated by the BNF Converter (bnfc 2.9.4.1).

package org.stella.typecheck;

import org.syntax.stella.Absyn.*;

import org.stella.helpers.SupportedExtensions;
import org.stella.typecheck.defined.*;
import org.stella.utils.ExceptionsUtils;

import java.util.ArrayList;
import java.util.List;

public class VisitTypeCheck
{
    public class ProgramVisitor implements org.syntax.stella.Absyn.Program.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AProgram p, Context arg)
        { /* Code for AProgram goes here */
            p.languagedecl_.accept(new LanguageDeclVisitor(), arg);
            for (org.syntax.stella.Absyn.Extension x: p.listextension_) {
                x.accept(new ExtensionVisitor(), arg);
            }
            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), arg);
            }
            return null;
        }
    }
    public class LanguageDeclVisitor implements org.syntax.stella.Absyn.LanguageDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.LanguageCore p, Context arg)
        { /* Code for LanguageCore goes here */
            return null;
        }
    }
    public class ExtensionVisitor implements org.syntax.stella.Absyn.Extension.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AnExtension p, Context arg)
        { /* Code for AnExtension goes here */
            for (String x: p.listextensionname_) {
                SupportedExtensions.Support(x);
            }
            return null;
        }
    }
    public class DeclVisitor implements org.syntax.stella.Absyn.Decl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.DeclFun p, Context arg)
        { /* Code for DeclFun goes here */
            DefinedType funType = new FunDefined();
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.Annotation x: p.listannotation_) {
                x.accept(new AnnotationVisitor(), context);
            }

            for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
                funType.args.add(x.accept(new ParamDeclVisitor(), context));
            }

            funType.result = p.returntype_.accept(new ReturnTypeVisitor(), context);
            p.throwtype_.accept(new ThrowTypeVisitor(), context);

            context.addLocal(p.stellaident_, funType);

            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), context);
            }
            var body = p.expr_.accept(new ExprVisitor(), context);

            funType.result.equals(body, "FunDecl [" + p.stellaident_ + "]");

            //Add fun to global definitions
            arg.addGlobal(p.stellaident_, funType);

            return funType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DeclTypeAlias p, Context arg)
        { /* Code for DeclTypeAlias goes here */
            var type = p.type_.accept(new TypeVisitor(), arg);
            arg.addGlobal(p.stellaident_, type);
            return type;
        }

        public DefinedType visit(DeclExceptionType p, Context arg) {
            return null;
        }

        public DefinedType visit(DeclExceptionVariant p, Context arg) {
            return null;
        }
    }
    public class LocalDeclVisitor implements org.syntax.stella.Absyn.LocalDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ALocalDecl p, Context arg)
        { /* Code for ALocalDecl goes here */
            p.decl_.accept(new DeclVisitor(), arg);
            return null;
        }
    }
    public class AnnotationVisitor implements org.syntax.stella.Absyn.Annotation.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.InlineAnnotation p, Context arg)
        { /* Code for InlineAnnotation goes here */
            return null;
        }
    }
    public class ParamDeclVisitor implements org.syntax.stella.Absyn.ParamDecl.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AParamDecl p, Context arg)
        { /* Code for AParamDecl goes here */
            var param = p.type_.accept(new TypeVisitor(), arg);
            arg.addLocal(p.stellaident_, param);
            return param;
        }
    }
    public class ReturnTypeVisitor implements org.syntax.stella.Absyn.ReturnType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoReturnType p, Context arg)
        { /* Code for NoReturnType goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeReturnType p, Context arg)
        { /* Code for SomeReturnType goes here */
            var returnType = p.type_.accept(new TypeVisitor(), arg);

            if (returnType == null)
                ExceptionsUtils.throwTypeException("SomeReturnType", "[SomeType]", "NULL");

            return returnType;
        }
    }
    public class ThrowTypeVisitor implements org.syntax.stella.Absyn.ThrowType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoThrowType p, Context arg)
        { /* Code for NoThrowType goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeThrowType p, Context arg)
        { /* Code for SomeThrowType goes here */
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            return null;
        }
    }
    public class TypeVisitor implements org.syntax.stella.Absyn.Type.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.TypeFun p, Context arg)
        { /* Code for TypeFun goes here */
            DefinedType funType = new FunDefined();

            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                funType.args.add(x.accept(new TypeVisitor(), arg));
            }
            funType.result = p.type_.accept(new TypeVisitor(), arg);

            return funType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeRec p, Context arg)
        { /* Code for TypeRec goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeSum p, Context arg)
        { /* Code for TypeSum goes here */
            SumDefined sumType = new SumDefined();
            sumType.labels.put("Inl", p.type_1.accept(new TypeVisitor(), arg));
            sumType.labels.put("Inr", p.type_2.accept(new TypeVisitor(), arg));
            return sumType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeTuple p, Context arg)
        { /* Code for TypeTuple goes here */
            TupleDefined tupleType = new TupleDefined();
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                tupleType.args.add(x.accept(new TypeVisitor(), arg));
            }
            return tupleType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeRecord p, Context arg)
        { /* Code for TypeRecord goes here */
            RecordDefined recordType = new RecordDefined();
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.RecordFieldType x: p.listrecordfieldtype_) {
                x.accept(new RecordFieldTypeVisitor(), context);
            }

            recordType.labels = context.LocalDefinitions;
            return recordType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeVariant p, Context arg)
        { /* Code for TypeVariant goes here */
            VariantDefined variantType = new VariantDefined();
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.VariantFieldType x: p.listvariantfieldtype_) {
                x.accept(new VariantFieldTypeVisitor(), context);
            }

            variantType.labels = context.LocalDefinitions;
            return variantType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeList p, Context arg)
        { /* Code for TypeList goes here */
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeBool p, Context arg)
        { /* Code for TypeBool goes here */
            return new BoolDefined();
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeNat p, Context arg)
        { /* Code for TypeNat goes here */
            return new NatDefined();
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeUnit p, Context arg)
        { /* Code for TypeUnit goes here */
            return new UnitDefined();
        }
        public DefinedType visit(TypeTop p, Context arg) {
            return null;
        }
        public DefinedType visit(TypeBottom p, Context arg) {
            return null;
        }
        public DefinedType visit(TypeRef p, Context arg) {
            RefDefined refType = new RefDefined();
            refType.args.add(p.type_.accept(new TypeVisitor(), arg));
            return refType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeVar p, Context arg)
        { /* Code for TypeVar goes here */
            DefinedType type;
            type = arg.lookUp(p.stellaident_);
            return type;
        }
    }
    public class MatchCaseVisitor implements org.syntax.stella.Absyn.MatchCase.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AMatchCase p, Context arg)
        { /* Code for AMatchCase goes here */
            Context context = new Context(arg);
            p.pattern_.accept(new PatternVisitor(), context);
            return p.expr_.accept(new ExprVisitor(), context);
        }
    }
    public class OptionalTypingVisitor implements org.syntax.stella.Absyn.OptionalTyping.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoTyping p, Context arg)
        { /* Code for NoTyping goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeTyping p, Context arg)
        { /* Code for SomeTyping goes here */
            return p.type_.accept(new TypeVisitor(), arg);
        }
    }
    public class PatternDataVisitor implements org.syntax.stella.Absyn.PatternData.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoPatternData p, Context arg)
        { /* Code for NoPatternData goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomePatternData p, Context arg)
        { /* Code for SomePatternData goes here */
            return p.pattern_.accept(new PatternVisitor(), arg);
//            return null;
        }
    }
    public class ExprDataVisitor implements org.syntax.stella.Absyn.ExprData.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.NoExprData p, Context arg)
        { /* Code for NoExprData goes here */
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.SomeExprData p, Context arg)
        { /* Code for SomeExprData goes here */
            return p.expr_.accept(new ExprVisitor(), arg);
//            return null;
        }
    }
    public class PatternVisitor implements org.syntax.stella.Absyn.Pattern.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.PatternVariant p, Context arg)
        { /* Code for PatternVariant goes here */
            if (arg.MatchType.type != TypesEnum.Variant || !arg.MatchType.labels.containsKey(p.stellaident_))
                ExceptionsUtils.throwTypeException("PatternVariant", String.format("Variant : %s", p.stellaident_), arg.MatchType.toString());
            //p.stellaident_;
            arg.MatchType = arg.MatchType.labels.get(p.stellaident_);
            return p.patterndata_.accept(new PatternDataVisitor(), arg);
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternInl p, Context arg)
        { /* Code for PatternInl goes here */
            if (arg.MatchType.type != TypesEnum.Sum || !arg.MatchType.labels.containsKey("Inl"))
                ExceptionsUtils.throwTypeException("PatternInl", "Sum : Inl", arg.MatchType.toString());

            arg.MatchType = arg.MatchType.labels.get("Inl");
            return p.pattern_.accept(new PatternVisitor(), arg);
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternInr p, Context arg)
        { /* Code for PatternInr goes here */
            if (arg.MatchType.type != TypesEnum.Sum || !arg.MatchType.labels.containsKey("Inr"))
                ExceptionsUtils.throwTypeException("PatternInr", "Sum : Inr", arg.MatchType.toString());

            arg.MatchType = arg.MatchType.labels.get("Inr");
            return p.pattern_.accept(new PatternVisitor(), arg);
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternTuple p, Context arg)
        { /* Code for PatternTuple goes here */
            TupleDefined tupleType = new TupleDefined();
            Context context = new Context(arg);
            int i = 0;
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                context.MatchType = arg.MatchType.args.get(i);
                tupleType.args.add(x.accept(new PatternVisitor(), context));
                i++;
            }

            if (arg.MatchType.type != TypesEnum.Tuple || !tupleType.equals(arg.MatchType, "Tuple"))
                ExceptionsUtils.throwTypeException("PatternTuple", tupleType.toString(), arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternRecord p, Context arg)
        { /* Code for PatternRecord goes here */
            RecordDefined recordType = new RecordDefined();
            Context context = new Context(arg);
            for (org.syntax.stella.Absyn.LabelledPattern x: p.listlabelledpattern_) {
                x.accept(new LabelledPatternVisitor(), context);
            }
            recordType.labels = context.LocalDefinitions;

            if (arg.MatchType.type != TypesEnum.Record || !recordType.equals(arg.MatchType, "PatternRecord"))
                ExceptionsUtils.throwTypeException("PatternRecord", recordType.toString(), arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternList p, Context arg)
        { /* Code for PatternList goes here */
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                x.accept(new PatternVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternCons p, Context arg)
        { /* Code for PatternCons goes here */
            p.pattern_1.accept(new PatternVisitor(), arg);
            p.pattern_2.accept(new PatternVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternFalse p, Context arg)
        { /* Code for PatternFalse goes here */
            if (arg.MatchType.type != TypesEnum.Bool)
                ExceptionsUtils.throwTypeException("PatternFalse", "Bool", arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternTrue p, Context arg)
        { /* Code for PatternTrue goes here */
            if (arg.MatchType.type != TypesEnum.Bool)
                ExceptionsUtils.throwTypeException("PatternTrue", "Bool", arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternUnit p, Context arg)
        { /* Code for PatternUnit goes here */
            if (arg.MatchType.type != TypesEnum.Unit)
                ExceptionsUtils.throwTypeException("PatternUnit", "Unit", arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternInt p, Context arg)
        { /* Code for PatternInt goes here */
            if (arg.MatchType.type != TypesEnum.Nat || p.integer_ != 0)
                ExceptionsUtils.throwTypeException("PatternInt", "Nat : 0", arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternSucc p, Context arg)
        { /* Code for PatternSucc goes here */
            var type = p.pattern_.accept(new PatternVisitor(), arg);
            if (arg.MatchType.type != TypesEnum.Nat || type.type != TypesEnum.Nat)
                ExceptionsUtils.throwTypeException("PatternSucc", "Nat", arg.MatchType.toString());

            return arg.MatchType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.PatternVar p, Context arg)
        { /* Code for PatternVar goes here */
            arg.addLocal(p.stellaident_, arg.MatchType);
            return arg.MatchType;
        }
    }
    public class LabelledPatternVisitor implements org.syntax.stella.Absyn.LabelledPattern.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ALabelledPattern p, Context arg)
        { /* Code for ALabelledPattern goes here */
            //p.stellaident_;
            Context context = new Context(arg);
            context.MatchType = arg.MatchType.labels.get(p.stellaident_);
            var type = p.pattern_.accept(new PatternVisitor(), context);
            arg.addLocal(p.stellaident_, type);
            return type;
        }
    }
    public class BindingVisitor implements org.syntax.stella.Absyn.Binding.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ABinding p, Context arg)
        { /* Code for ABinding goes here */
            //p.stellaident_;
            var type = p.expr_.accept(new ExprVisitor(), arg);
            arg.addLocal(p.stellaident_, type);
            return type;
        }
    }
    public class ExprVisitor implements org.syntax.stella.Absyn.Expr.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.Sequence p, Context arg)
        { /* Code for Sequence goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);

            if (!(t1.equals(new UnitDefined())))
                ExceptionsUtils.throwTypeException("Sequence", "Unit", t1.toString());

            return t2;
        }
        public DefinedType visit(Assign p, Context arg) {
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);

            if (t1.type == TypesEnum.Ref && t1.args.get(0).equals(t2))
                return new UnitDefined();

            ExceptionsUtils.throwTypeException("Assign", "Ref", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.If p, Context arg)
        { /* Code for If goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            var t3 = p.expr_3.accept(new ExprVisitor(), arg);

            if (t1.type == TypesEnum.Bool && t2.equals(t3, "IF"))
                return t2;

            ExceptionsUtils.throwTypeException("IF [T1]", "Bool", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Let p, Context arg)
        { /* Code for Let goes here */
            for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor(), arg);
            }
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LetRec p, Context arg)
        { /* Code for LetRec goes here */
            for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor(), arg);
            }
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LessThan p, Context arg)
        { /* Code for LessThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LessThanOrEqual p, Context arg)
        { /* Code for LessThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.GreaterThan p, Context arg)
        { /* Code for GreaterThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.GreaterThanOrEqual p, Context arg)
        { /* Code for GreaterThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Equal p, Context arg)
        { /* Code for Equal goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.NotEqual p, Context arg)
        { /* Code for NotEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.TypeAsc p, Context arg)
        { /* Code for TypeAsc goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public DefinedType visit(TypeCast p, Context arg) {
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Abstraction p, Context arg)
        { /* Code for Abstraction goes here */
            DefinedType abstractFun = new FunDefined();
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
                abstractFun.args.add(x.accept(new ParamDeclVisitor(), context));
            }
            abstractFun.result = p.expr_.accept(new ExprVisitor(), context);

            return abstractFun;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Variant p, Context arg)
        { /* Code for Variant goes here */
            VariantDefined variantType = new VariantDefined();
            var type = p.exprdata_.accept(new ExprDataVisitor(), arg);
            variantType.labels.put(p.stellaident_, type);
            return variantType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Match p, Context arg)
        { /* Code for Match goes here */
            DefinedType matchType = p.expr_.accept(new ExprVisitor(), arg);
            Context context = new Context(arg);
            context.MatchType = matchType;

            List<DefinedType> returnTypes = new ArrayList<>();

            for (org.syntax.stella.Absyn.MatchCase x: p.listmatchcase_) {
                returnTypes.add(x.accept(new MatchCaseVisitor(), context));
            }

            var firstType = returnTypes.get(0);
            DefinedType unexpectedType = firstType;
            boolean allAreEqual = true;
            for (var type : returnTypes) {
                allAreEqual = type.equals(firstType, "Match");
                if (!allAreEqual){
                    unexpectedType = type;
                    break;
                }
            }

            if (!allAreEqual)
                ExceptionsUtils.throwTypeException("Match - Return", firstType.toString(), unexpectedType.toString());

            return firstType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.List p, Context arg)
        { /* Code for List goes here */
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                x.accept(new ExprVisitor(), arg);
            }
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Add p, Context arg)
        { /* Code for Add goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            if (t1.equals(t2) && t1.type == TypesEnum.Nat)
                return t1;

            ExceptionsUtils.throwTypeException("Add", "Nat", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Subtract p, Context arg)
        { /* Code for Subtract goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            if (t1.equals(t2) && t1.type == TypesEnum.Nat)
                return t1;

            ExceptionsUtils.throwTypeException("Subtract", "Nat", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicOr p, Context arg)
        { /* Code for LogicOr goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Multiply p, Context arg)
        { /* Code for Multiply goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            if (t1.equals(t2) && t1.type == TypesEnum.Nat)
                return t1;

            ExceptionsUtils.throwTypeException("Multiply", "Nat", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Divide p, Context arg)
        { /* Code for Divide goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            if (t1.equals(t2) && t1.type == TypesEnum.Nat)
                return t1;

            ExceptionsUtils.throwTypeException("Divide", "Nat", t1.toString());
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicAnd p, Context arg)
        { /* Code for LogicAnd goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(Ref p, Context arg) {
            RefDefined refType = new RefDefined();
            refType.args.add(p.expr_.accept(new ExprVisitor(), arg));
            return refType;
        }

        public DefinedType visit(Deref p, Context arg) {
            var type = p.expr_.accept(new ExprVisitor(), arg);
            if (type.type != TypesEnum.Ref)
                ExceptionsUtils.throwTypeException("Deref", "Ref", type.toString());

            return type.args.get(0);
        }
        public DefinedType visit(org.syntax.stella.Absyn.Application p, Context arg)
        { /* Code for Application goes here */
            List<DefinedType> argType = new ArrayList<>();
            DefinedType funType = p.expr_.accept(new ExprVisitor(), arg);
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                argType.add(x.accept(new ExprVisitor(), arg));
            }

            if (Checker.CheckApplication(funType, argType))
                return funType.result;

            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DotRecord p, Context arg)
        { /* Code for DotRecord goes here */
            var recordType = p.expr_.accept(new ExprVisitor(), arg);

            if (recordType.labels.containsKey(p.stellaident_))
                return recordType.labels.get(p.stellaident_);
            //p.stellaident_;

            ExceptionsUtils.throwKeyDoesNotExistException("Variant", p.stellaident_);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.DotTuple p, Context arg)
        { /* Code for DotTuple goes here */
            var tupleType = p.expr_.accept(new ExprVisitor(), arg);

            if (1 <= p.integer_ && p.integer_ <= tupleType.args.size())
                return tupleType.args.get(p.integer_ - 1);

            ExceptionsUtils.throwOutOfRangeException("Tuple", tupleType.args.size(), p.integer_);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Tuple p, Context arg)
        { /* Code for Tuple goes here */
            TupleDefined tupleType = new TupleDefined();
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                tupleType.args.add(x.accept(new ExprVisitor(), arg));
            }
            return tupleType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Record p, Context arg)
        { /* Code for Record goes here */
            RecordDefined recordType = new RecordDefined();
            Context context = new Context(arg);

            for (org.syntax.stella.Absyn.Binding x: p.listbinding_) {
                x.accept(new BindingVisitor(), context);
            }

            recordType.labels = context.LocalDefinitions;
            return recordType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConsList p, Context arg)
        { /* Code for ConsList goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Head p, Context arg)
        { /* Code for Head goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.IsEmpty p, Context arg)
        { /* Code for IsEmpty goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Tail p, Context arg)
        { /* Code for Tail goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(Panic p, Context arg) {
            return null;
        }

        public DefinedType visit(Throw p, Context arg) {
            return null;
        }

        public DefinedType visit(TryCatch p, Context arg) {
            return null;
        }

        public DefinedType visit(TryWith p, Context arg) {
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Inl p, Context arg)
        { /* Code for Inl goes here */
            SumDefined sumType = new SumDefined();
            sumType.labels.put("Inl", p.expr_.accept(new ExprVisitor(), arg));
            return sumType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Inr p, Context arg)
        { /* Code for Inr goes here */
            SumDefined sumType = new SumDefined();
            sumType.labels.put("Inr", p.expr_.accept(new ExprVisitor(), arg));
            return sumType;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Succ p, Context arg)
        { /* Code for Succ goes here */
            var type = p.expr_.accept(new ExprVisitor(), arg);

            if (type == null)
                ExceptionsUtils.throwTypeException("Succ", "Nat", "NULL");

            if (type.type != TypesEnum.Nat)
                ExceptionsUtils.throwTypeException("Succ", "Nat", type.type.name());

            return type;
        }
        public DefinedType visit(org.syntax.stella.Absyn.LogicNot p, Context arg)
        { /* Code for LogicNot goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Pred p, Context arg)
        { /* Code for Pred goes here */
            var type = p.expr_.accept(new ExprVisitor(), arg);

            if (type == null)
                ExceptionsUtils.throwTypeException("Pred", "Nat", "NULL");

            if (type.type != TypesEnum.Nat)
                ExceptionsUtils.throwTypeException("Pred", "Nat", type.type.name());

            return type;
        }
        public DefinedType visit(org.syntax.stella.Absyn.IsZero p, Context arg)
        { /* Code for IsZero goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Fix p, Context arg)
        { /* Code for Fix goes here */
            var type = p.expr_.accept(new ExprVisitor(), arg);

            if (type.type != TypesEnum.Fun || type.result == null)
                ExceptionsUtils.throwTypeException("Fix", "Fun", type.toString());

            return type.result;
        }
        public DefinedType visit(org.syntax.stella.Absyn.NatRec p, Context arg)
        { /* Code for NatRec goes here */
            var t1 = p.expr_1.accept(new ExprVisitor(), arg);
            var t2 = p.expr_2.accept(new ExprVisitor(), arg);
            var t3 = p.expr_3.accept(new ExprVisitor(), arg);

            if (Checker.CheckNatRecFunParam(t1, t2, t3))
                return t2;

            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Fold p, Context arg)
        { /* Code for Fold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Unfold p, Context arg)
        { /* Code for Unfold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstTrue p, Context arg)
        { /* Code for ConstTrue goes here */
            return new BoolDefined();
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstFalse p, Context arg)
        { /* Code for ConstFalse goes here */
            return new BoolDefined();
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstUnit p, Context arg)
        { /* Code for ConstUnit goes here */
            return new UnitDefined();
        }
        public DefinedType visit(org.syntax.stella.Absyn.ConstInt p, Context arg)
        { /* Code for ConstInt goes here */
            if (p.integer_ == 0)
                return new NatDefined();

            ExceptionsUtils.throwTypeException("ConstInt", "value == 0", "value != 0");
            return null;
        }
        public DefinedType visit(ConstMemory p, Context arg) {
            return null;
        }
        public DefinedType visit(org.syntax.stella.Absyn.Var p, Context arg)
        { /* Code for Var goes here */
            DefinedType type;
            type = arg.lookUp(p.stellaident_);
            return type;
        }
    }
    public class PatternBindingVisitor implements org.syntax.stella.Absyn.PatternBinding.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.APatternBinding p, Context arg)
        { /* Code for APatternBinding goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class VariantFieldTypeVisitor implements org.syntax.stella.Absyn.VariantFieldType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.AVariantFieldType p, Context arg)
        { /* Code for AVariantFieldType goes here */
            //p.stellaident_;
            var type = p.optionaltyping_.accept(new OptionalTypingVisitor(), arg);
            arg.addLocal(p.stellaident_, type);
            return type;
        }
    }
    public class RecordFieldTypeVisitor implements org.syntax.stella.Absyn.RecordFieldType.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ARecordFieldType p, Context arg)
        { /* Code for ARecordFieldType goes here */
            //p.stellaident_;
            var type = p.type_.accept(new TypeVisitor(), arg);
            arg.addLocal(p.stellaident_, type);
            return type;
        }
    }
    public class TypingVisitor implements org.syntax.stella.Absyn.Typing.Visitor<DefinedType,Context>
    {
        public DefinedType visit(org.syntax.stella.Absyn.ATyping p, Context arg)
        { /* Code for ATyping goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
}

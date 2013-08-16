tree grammar Ampl;
options {
    tokenVocab=AmplCombined;
    ASTLabelType=CommonTree;
}

@header {
package net.vatov.ampl;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedHashSet;

import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.SymbolDeclaration.DeclarationAttributeEnum;
import net.vatov.ampl.model.NodeValue.OperationType;
import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SetMember;
import net.vatov.ampl.model.SetMember.SetMemberType;
import net.vatov.ampl.model.SetExpression;
import net.vatov.ampl.model.SetExpression.SetExpressionType;
import net.vatov.ampl.model.SetNodeValue;
import net.vatov.ampl.model.SetNodeValue.SetOperationType;
import net.vatov.ampl.model.RangeValue;

import net.vatov.ampl.model.Expression;
import static net.vatov.ampl.model.Util.*;

}

@members {
private OptimModel m = new OptimModel();
}

model returns [OptimModel m]
@init{
    $m = this.m;
}
    : (param_decl | var_decl | set_decl)+ objective_decl* constraint*
    ;

/*
 * objective
 */
 objective_decl
    : ^(OBJ_ENTITY NAME expr) {
            m.addObjective($OBJ_ENTITY.text, $NAME.text, $expr.e);
       }
    ;

/*
 * constraint
 */ 

constr_relop returns [String r]
    : GTE {r = $GTE.text;}
    | ASSIGN {r = $ASSIGN.text;}
    | LTE {r = $LTE.text;}
    ;

constraint
    : ^(NAME constr_relop e1=expr e2=expr) {
        m.addConstraint($NAME.text, $constr_relop.r, $e1.e, $e2.e);
       }
    ;

/*
 * parameters
 */
param_decl returns [SymbolDeclaration pd]
    : ^(PARAM_DECL NAME param_attributes?) {
            $pd = SymbolDeclaration.createParamDeclaration($NAME.text);
            $pd.setAttributes($param_attributes.pda);
            m.addSymbolDeclaration($pd);
        }
    ;

param_attributes returns [Map<DeclarationAttributeEnum, Object> pda]
@init {
    $pda = new HashMap<DeclarationAttributeEnum, Object>();
}
    : param_attribute[$pda]+
    ;

param_attribute[Map<DeclarationAttributeEnum, Object> pda]
    : INTEGER { $pda.put(DeclarationAttributeEnum.INTEGER,true); }
    | ^(DEFAULT_ASSIGN expr) {
            $pda.put(DeclarationAttributeEnum.VALUE, $expr.e);
        }
    | BINARY { $pda.put(DeclarationAttributeEnum.BINARY,true); }
    ;
/*
 * end parameters
 */

/*
 * sets
 */
set_decl returns [SymbolDeclaration sd]
    : ^(SET_DECL NAME set_attributes?) {
      $sd = SymbolDeclaration.createSetDeclaration($NAME.text);
      $sd.setAttributes($set_attributes.sda);
      m.addSymbolDeclaration($sd);
    }
    ;

set_attributes returns [Map<DeclarationAttributeEnum, Object> sda]
@init {
    $sda = new HashMap<DeclarationAttributeEnum, Object>();
}
    : set_attribute[$sda]+
    ;

set_attribute[Map<DeclarationAttributeEnum, Object> sda]
    : ^(DIMEN INT) { $sda.put(DeclarationAttributeEnum.SET_DIMEN, Integer.parseInt($INT.text)); }
    | ^(WITHIN sexpr) { $sda.put(DeclarationAttributeEnum.SET_WITHIN, $sexpr.sex); }
    | ^(ASSIGN sexpr) { $sda.put(DeclarationAttributeEnum.SET_ASSIGN, $sexpr.sex); }
    | ^(DEFAULT sexpr) { $sda.put(DeclarationAttributeEnum.SET_DEFAULT, $sexpr.sex); }
    | SET_ORDERED { $sda.put(DeclarationAttributeEnum.SET_ORDERED, true);}
    | SET_CIRCULAR { $sda.put(DeclarationAttributeEnum.SET_CIRCULAR, true);}
    ;

/*
sexpr_list:
      member 'in' sexpr
    | sexpr (',' sexpr)*
    ;
*/

//TODO: change return type
sexpr returns [SetExpression sex]
  : ^(o = UNION a = sexpr b = sexpr) { 
             $sex = new SetExpression(SetExpressionType.TREE,
                                                                new SetNodeValue(SetOperationType.parse($o.text),
                                                                                                      new SetExpression[] {$a.sex, $b.sex}));
       }
   | ^(o = DIFF a = sexpr b = sexpr) { 
             $sex = new SetExpression(SetExpressionType.TREE,
                                                                new SetNodeValue(SetOperationType.parse($o.text),
                                                                                                      new SetExpression[] {$a.sex, $b.sex}));
       }
    | ^(o = SYMDIFF a = sexpr b = sexpr) { 
             $sex = new SetExpression(SetExpressionType.TREE,
                                                                new SetNodeValue(SetOperationType.parse($o.text),
                                                                                                      new SetExpression[] {$a.sex, $b.sex}));
       }
     | ^(o = INTER a = sexpr b = sexpr) { 
             $sex = new SetExpression(SetExpressionType.TREE,
                                                                new SetNodeValue(SetOperationType.parse($o.text),
                                                                                                      new SetExpression[] {$a.sex, $b.sex}));
       }
     | ^(o = CROSS a = sexpr b = sexpr) { 
             $sex = new SetExpression(SetExpressionType.TREE,
                                                                new SetNodeValue(SetOperationType.parse($o.text),
                                                                                                      new SetExpression[] {$a.sex, $b.sex}));
       }
     | ^(SET_RANGE c = expr d = expr (s = expr)?) {
                  $sex = new SetExpression(SetExpressionType.RANGE,
                                                                new RangeValue($c.e, $d.e, $s.e));
       }
     | ^(MEMBER_ENUM {Set<SetMember> ms = new LinkedHashSet<SetMember>();} (member {ms.add($member.m);})*) { 
          $sex = new SetExpression(SetExpression.SetExpressionType.ENUM, ms);
              if (ms.isEmpty()) {
                  ms = null;
              }
        }
    //    | (OP_NAME indexing sexpr)
    | NAME { 
            $sex = new SetExpression(SetExpression.SetExpressionType.VARREF, m.getSetRef($NAME.text));
            }
  ;

lexpr
    : expr (COMPARE_OP expr)?
//    | lexpr LOGIC_OP lexpr
//    | '(' lexpr ')'
    ;
/*
 * end sets
 */
 
/*
 * variables
 */

var_decl returns [SymbolDeclaration vd]
    : ^(VAR_DECL NAME var_attributes?) {
            $vd = SymbolDeclaration.createVarDeclaration($NAME.text);
            $vd.setAttributes($var_attributes.vda);
            m.addSymbolDeclaration($vd);
        }
    ;

var_attributes returns [Map<DeclarationAttributeEnum, Object> vda]
@init {
    $vda = new HashMap<DeclarationAttributeEnum, Object>();
}
    : var_attribute[$vda]+
    ;

var_attribute[Map<DeclarationAttributeEnum, Object> vda]
    : INTEGER { $vda.put(DeclarationAttributeEnum.INTEGER,true); }
    | BINARY { $vda.put(DeclarationAttributeEnum.BINARY,true); }
    | ^(DEFAULT_ASSIGN expr) {
            $vda.put(DeclarationAttributeEnum.VALUE, $expr.e);
        }
    | ^(GTE expr) { $vda.put(DeclarationAttributeEnum.LOWER_BOUND, $expr.e); } 
    | ^(LTE expr) { $vda.put(DeclarationAttributeEnum.UPPER_BOUND, $expr.e); }
    ;
/*
 * end variables
 */

expr returns [Expression e]
@init {
    $e = new Expression();
}
    : ^(POW a=expr b=expr) {
            if (areConstExpressions($a.e, $b.e)) {
                $e.setValue(Math.pow($a.e.getValue(), $b.e.getValue()));
            } else {
                $e.setTreeValue(OperationType.POW, $a.e, $b.e);
            }
        }
    | ^(MULT a=expr b=expr) {
            if (areConstExpressions($a.e, $b.e)) {
                $e.setValue($a.e.getValue() * $b.e.getValue());
            } else {
                $e.setTreeValue(OperationType.MULT, $a.e, $b.e);
            }
        }
    | ^(DIV_SLASH a=expr b=expr) {
            if (areConstExpressions($a.e, $b.e)) {
                $e.setValue($a.e.getValue() / $b.e.getValue());
            } else {
                $e.setTreeValue(OperationType.DIV_SLASH, $a.e, $b.e);
            }
        }
    | ^(MOD a=expr b=expr) {
            if (areConstExpressions($a.e, $b.e)) {
                $e.setValue($a.e.getValue() \% $b.e.getValue());
            } else {
                $e.setTreeValue(OperationType.MOD, $a.e, $b.e);
            }
        }
    | ^(PLUS a=expr b=expr?) {
            if (null == b) {
              if (areConstExpressions($a.e)) {
                  $e.setValue($a.e.getValue());
              } else {
                  $e.setTreeValue(OperationType.UNARY_PLUS, $a.e);
              }
            } else {
	            if (areConstExpressions($a.e, $b.e)) {
	                $e.setValue($a.e.getValue() + $b.e.getValue());
	            } else {
	                $e.setTreeValue(OperationType.PLUS, $a.e, $b.e);
	            }
            }
        }
    | ^(MINUS a=expr b=expr?) {
            if (null == b) {
              if (areConstExpressions($a.e)) {
                  $e.setValue(-$a.e.getValue());
              } else {
                  $e.setTreeValue(OperationType.UNARY_MINUS, $a.e);
              }
            } else {
	            if (areConstExpressions($a.e, $b.e)) {
	                $e.setValue($a.e.getValue() - $b.e.getValue());
	            } else {
	                $e.setTreeValue(OperationType.MINUS, $a.e, $b.e);
	            }
            }
        }
    | number {$e.setValue($number.e);}
    | NAME {
            if (m.paramIsDefined($NAME.text)) {
                $e.setValue(m.getParamValue($NAME.text));
            } else if (m.varIsDefined($NAME.text)) {
                $e.setSymRef(m.getVarRef($NAME.text));
            } else {
                throw new ModelException($NAME.line
                                           + ":" + $NAME.pos
                                           + " undefined symbol "
                                           + $NAME.text);
            }
        }
     | builtin_function {$e.setTreeValue($builtin_function.f, $builtin_function.es);}
    ;

builtin_function returns [BuiltinFunction f, List<Expression> es]
@init {
  $es = new ArrayList<Expression>();
}
@after {
  if ($es.isEmpty()) {
    $es = null;
  }
}
    : ^(ABS expr) {$f = BuiltinFunction.ABS; $es.add($expr.e);}
    | ^(ACOS expr) {$f = BuiltinFunction.ACOS; $es.add($expr.e);}
    | ^(ACOSH expr) {$f = BuiltinFunction.ACOSH; $es.add($expr.e);}
    | ^(ASIN expr) {$f = BuiltinFunction.ASIN; $es.add($expr.e);}
    | ^(ASINH expr) {$f = BuiltinFunction.ASINH; $es.add($expr.e);}
    | ^(ATAN expr) {$f = BuiltinFunction.ATAN; $es.add($expr.e);}
    | ^(ATAN2 a=expr b=expr) {$f = BuiltinFunction.ATAN2; $es.add($a.e); $es.add($b.e);}
    | ^(ATANH expr) {$f = BuiltinFunction.ATANH; $es.add($expr.e);}
    | ^(CEIL expr) {$f = BuiltinFunction.CEIL; $es.add($expr.e);}
    | ^(CTIME (a=expr {$es.add($a.e);})?) {$f = BuiltinFunction.CTIME;}
    | ^(COS expr) {$f = BuiltinFunction.COS; $es.add($expr.e);}
    | ^(EXP expr) {$f = BuiltinFunction.EXP; $es.add($expr.e);}
    | ^(FLOOR expr) {$f = BuiltinFunction.FLOOR; $es.add($expr.e);}
    | ^(LOG expr) {$f = BuiltinFunction.LOG; $es.add($expr.e);}
    | ^(LOG10 expr) {$f = BuiltinFunction.LOG10; $es.add($expr.e);}
    | ^(MAX (a=expr {$es.add(a);}) (ex=expr {$es.add($ex.e);} )+) {$f = BuiltinFunction.MAX;}
    | ^(MIN (a=expr {$es.add(a);}) (ex=expr {$es.add($ex.e);} )+) {$f = BuiltinFunction.MIN;}
    | ^(PRECISION (a=expr {$es.add($a.e);}) (b=expr {$es.add($b.e);})?) {$f = BuiltinFunction.PRECISION;}
    | ^(ROUND (a=expr {$es.add($a.e);}) (b=expr {$es.add($b.e);})?) {$f = BuiltinFunction.ROUND;}
    | ^(SIN expr) {$f = BuiltinFunction.SIN; $es.add($expr.e);}
    | ^(SINH expr) {$f = BuiltinFunction.SINH; $es.add($expr.e);}
    | ^(SQRT expr) {$f = BuiltinFunction.SQRT; $es.add($expr.e);}
    | ^(TAN expr) {$f = BuiltinFunction.TAN; $es.add($expr.e);}
    | ^(TANH expr) {$f = BuiltinFunction.TANH; $es.add($expr.e);}
    | TIME {$f = BuiltinFunction.TIME;}
    | ^(TRUNC (a=expr {$es.add($a.e);}) (b=expr {$es.add($b.e);})?) {$f = BuiltinFunction.TRUNC;}
    ;

number returns [Double e]
    : INT {$e = new Double($INT.text);}
    | FLOAT {$e = new Double($FLOAT.text);}
    ;

member returns [SetMember m]
    : STRING { $m = new SetMember($STRING.text, SetMemberType.STRING);}
    | number { $m = new SetMember($number.e, SetMemberType.NUMBER);}
    | NAME  {
               if (this.m.symbolIsDefined($NAME.text)) {
                   $m = new SetMember(this.m.getSymbolRef($NAME.text), SetMemberType.SYMREF);
               } else {
                   throw new ModelException($NAME.line
                                           + ":" + $NAME.pos
                                           + " undefined symbol "
                                           + $NAME.text);
               }
            }
    ;
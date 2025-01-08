package com.github.zeroeighteightzero.lwlp.langs.css;

import com.github.zeroeighteightzero.lwlp.*;

import java.util.ArrayList;
import java.util.List;

public class CSS {

    Lexer lexer;
    Parser parser;
    public final CSSOptions options = new CSSOptions();

    private static boolean isHexadecimal(String str) {
        if (str == null || str.isEmpty()) return false;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))) {
                return false;
            }
        }
        return true;
    }

    public CSS() {
        lexer = new Lexer(
                new TokenPattern[]{
                        new TokenPattern("\\/\\*[\\s\\S]*?\\*\\/", "COMMENT").ignore(),
                        new TokenPattern("\\{", "LBRACE"),
                        new TokenPattern("\\}", "RBRACE"),
                        new TokenPattern("\\[", "LBRACKET"),
                        new TokenPattern("\\]", "RBRACKET"),
                        new TokenPattern("\\(", "LPAREN"),
                        new TokenPattern("\\)", "RPAREN"),
                        new TokenPattern("\\!important", "IMPORTANT"),
                        new TokenPattern("(?:\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(\\\\.[^'\\\\]*)*)')", "STRING"),
                        new TokenPattern("[^\\W\\d][\\w-]*", "IDENTIFIER"),
                        new TokenPattern("\\d+(\\.\\d+)?", "NUMBER"),
                        new TokenPattern("#[\\w-]+", "HASH"),
                        new TokenPattern("\\.[\\w-]+", "CLASS"),
                        new TokenPattern("\\,", "COMMA"),
                        new TokenPattern("\\:", "COLON"),
                        new TokenPattern("\\;", "SEMICOLON"),
                        new TokenPattern("\\=", "EQUALS"),
                        new TokenPattern("\\+", "PLUS"),
                        new TokenPattern("\\-", "MINUS"),
                        new TokenPattern("\\*", "ASTERISK"),
                        new TokenPattern("\\/", "SLASH"),
                        new TokenPattern("\\%", "PERCENT"),
                        new TokenPattern("[>~]", "COMBINATOR"),
                }
        );

        parser = new Parser(
                new Definition("val",
                        new ANDMatch("val",
                                new ORMatch(
                                        new ANDMatch("function",
                                                new TokenMatch("function_name", "IDENTIFIER"),
                                                new TokenMatch("_", "LPAREN"),
                                                new OrderedMinMatch("values", 1,
                                                        new DefMatch("value", "val"),
                                                        MultiMatch.min("others",
                                                                new ORMatch(
                                                                        new ANDMatch("_",
                                                                                new TokenMatch("_", "COMMA"),
                                                                                new DefMatch("value", "val")
                                                                        ),
                                                                        new ANDMatch("_",
                                                                                new DefMatch("value", "val")
                                                                        )
                                                                ),
                                                                1)
                                                ),
                                                new TokenMatch("_", "RPAREN")
                                        ),
                                        new TokenMatch("value_id", "IDENTIFIER"),
                                        new DefMatch("value_calc", "expr"),
                                        new TokenMatch("value_str", "STRING")
                                )
                        ),
                        false
                ),
                new Definition("simple_selector",
                        new ANDMatch("simple_selector",
                                new ORMatch(
                                        new TokenMatch("tag", "IDENTIFIER"),
                                        new TokenMatch("hash", "HASH"),
                                        new TokenMatch("class", "CLASS"),
                                        new ORMatch(
                                                new ANDMatch("functional_pseudo_class",
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("class", "IDENTIFIER"),
                                                        new TokenMatch("_", "LPAREN"),
                                                        new ANDMatch("extended_value",
                                                                new ORMatch(
                                                                        new DefMatch("value_val", "val"),
                                                                        new DefMatch("value_compound", "compound_selector")
                                                                )
                                                        ),
                                                        new TokenMatch("_", "RPAREN")
                                                ),
                                                new ANDMatch("pseudo_class",
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("class", "IDENTIFIER")
                                                )
                                        ),
                                        new ORMatch(
                                                new ANDMatch("attribute",
                                                        new TokenMatch("_", "LBRACKET"),
                                                        new TokenMatch("key", "IDENTIFIER"),
                                                        new TokenMatch("equals", "EQUALS"),
                                                        new DefMatch("value", "val"),
                                                        new TokenMatch("_", "RBRACKET")
                                                ),
                                                new ANDMatch("attribute_noval",
                                                        new TokenMatch("_", "LBRACKET"),
                                                        new TokenMatch("key", "IDENTIFIER"),
                                                        new TokenMatch("_", "RBRACKET")
                                                )
                                        )
                                )
                        ),
                        false
                ),
                new Definition("compound_selector",
                        new OrderedMinMatch("compound_selector", 1,
                                new DefMatch("simple_selector", "simple_selector"),
                                new ORMatch(
                                        new ANDMatch("add_pseudo",
                                                MultiMatch.min("additional_selectors",
                                                        new DefMatch("simple_selector", "simple_selector"),
                                                        0
                                                ),
                                                new ANDMatch("pseudo_element",
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("element", "IDENTIFIER")
                                                )
                                        ),
                                        new ANDMatch("pseudo_add",
                                                new ANDMatch("pseudo_element",
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("_", "COLON"),
                                                        new TokenMatch("element", "IDENTIFIER")
                                                ),
                                                MultiMatch.min("additional_selectors",
                                                        new DefMatch("simple_selector", "simple_selector"),
                                                        0
                                                )
                                        ),
                                        MultiMatch.min("additional_selectors",
                                                new DefMatch("simple_selector", "simple_selector"),
                                                0
                                        ),
                                        new ANDMatch("pseudo_element",
                                                new TokenMatch("_", "COLON"),
                                                new TokenMatch("_", "COLON"),
                                                new TokenMatch("element", "IDENTIFIER")
                                        )
                                )
                        ),
                        false
                ),
                new Definition("selector",
                        new OrderedMinMatch("selector", 1,
                                new DefMatch("compound_selector", "compound_selector"),
                                MultiMatch.min("combinator_and_compound_selector",
                                        new ORMatch(
                                                new ANDMatch("combinator_with_selector",
                                                        new ORMatch(
                                                                new TokenMatch("combinator", "COMBINATOR"),
                                                                new TokenMatch("combinator", "PLUS")
                                                        ),
                                                        new DefMatch("compound_selector", "compound_selector")
                                                ),
                                                new DefMatch("compound_selector", "compound_selector")
                                        ),
                                        0
                                )
                        ),
                        false
                ),
                new Definition("declaration",
                        new ANDMatch("declaration",
                                new TokenMatch("property", "IDENTIFIER"),
                                new TokenMatch("colon", "COLON"),
                                new OrderedMinMatch("values", 1,
                                        new DefMatch("value", "val"),
                                        MultiMatch.min("_",
                                                new ORMatch(
                                                        new ANDMatch("_",
                                                                new TokenMatch("_", "COMMA"),
                                                                new DefMatch("value", "val")
                                                        ),
                                                        new DefMatch("value", "val")
                                                ),
                                                0)
                                ),
                                new ORMatch(
                                        new TokenMatch("semicolon", "SEMICOLON"),
                                        new ANDMatch("important_semicolon",
                                                new TokenMatch("important", "IMPORTANT"),
                                                new TokenMatch("semicolon", "SEMICOLON")
                                        )
                                )
                        ),
                        false
                ),
                new Definition("rule",
                        new ANDMatch("rule",
                                new OrderedMinMatch("selectors", 1,
                                        new DefMatch("selector", "selector"),
                                        MultiMatch.min("others",
                                                new ANDMatch("_",
                                                        new TokenMatch("_", "COMMA"),
                                                        new DefMatch("selector", "selector")
                                                ),
                                                1)
                                ),
                                new TokenMatch("lbrace", "LBRACE"),
                                MultiMatch.min("declarations",
                                        new DefMatch("declaration", "declaration"),
                                        0
                                ),
                                new TokenMatch("rbrace", "RBRACE")
                        ),
                        true
                ),
                new Definition("expr",
                        new OrderedMinMatch("expression", 1,
                                new DefMatch("left", "term"),
                                MultiMatch.min("add_or_subtract",
                                        new ANDMatch("operation_and_term",
                                                new ORMatch(
                                                        new TokenMatch("operator", "PLUS"),
                                                        new TokenMatch("operator", "MINUS")
                                                ),
                                                new DefMatch("right", "term")
                                        ),
                                        1
                                )
                        ), false),
                new Definition("term",
                        new OrderedMinMatch("term_expression", 1,
                                new DefMatch("left_t", "factor"),
                                MultiMatch.min("multiply_or_divide",
                                        new ANDMatch("operation_and_factor",
                                                new ORMatch(
                                                        new TokenMatch("operator", "ASTERISK"),
                                                        new TokenMatch("operator", "SLASH")
                                                ),
                                                new DefMatch("right_t", "factor")
                                        ),
                                        1
                                )
                        ), false),
                new Definition("factor",
                        new ORMatch(
                                new ANDMatch("number_unit",
                                        new TokenMatch("value", "NUMBER"),
                                        new ORMatch(
                                                new TokenMatch("unit", "IDENTIFIER"),
                                                new TokenMatch("unit", "PERCENT")
                                        )
                                ),
                                new TokenMatch("value", "NUMBER"),
                                new ANDMatch("parentheses_expression",
                                        new TokenMatch("", "LPAREN"),
                                        new DefMatch("expr", "expr"),
                                        new TokenMatch("", "RPAREN")
                                ),
                                // Unary operators
                                new ANDMatch("unary_plus",
                                        new TokenMatch("operator", "PLUS"),
                                        new ANDMatch("operand", new DefMatch("operand", "factor"))
                                ),
                                new ANDMatch("unary_minus",
                                        new TokenMatch("operator", "MINUS"),
                                        new ANDMatch("operand", new DefMatch("operand", "factor"))
                                )
                        ), false)
        );
    }

    CSSErrorHandler errorHandler = (message, type) -> {
        throw new RuntimeException(type.name() + " : " + message);
    };

    private CSSMath factor(ListNode n) {
        Node f = n.nodes[0];
        if ("operator".equals(f.name))
            f = n.nodes[1];
        CSSMath v;
        if ("value".equals(f.name)) {
            v = new CSSMath();
            v.values.put(CSSUnit.NONE, new CSSNumberUnit(Float.parseFloat((((TokenNode) f).value))));
        } else if ("parentheses_expression".equals(f.name)) {
            v = expression((ListNode) ((ListNode) f).get("expression"));
        } else if ("number_unit".equals(f.name)) {
            ListNode list = (ListNode) f;
            v = new CSSMath();
            CSSUnit unit = CSSUnit.fromString(((TokenNode) list.get("unit")).value);
            v.values.put(unit, new CSSNumberUnit(Float.parseFloat((((TokenNode) list.get("value")).value)), unit));
        } else if ("unary_plus".equals(f.name)) {
            v = factor((ListNode) ((ListNode) f).get("operand"));
        } else {
            v = factor((ListNode) ((ListNode) ((ListNode) f).get("unary_minus")).get("operand"));
            for (CSSUnit unit : v.values.keySet()) {
                v.values.replace(unit, new CSSNumberUnit(-v.values.get(unit).number, v.values.get(unit).unit));
            }
        }
        return v;
    }

    private CSSMath term(ListNode t) {
        CSSMath left = factor(t);
        Node mdn = t.get("multiply_or_divide");
        if (mdn == null) return left;
        ListNode md = (ListNode) mdn;
        for (int i = 0; i < md.nodes.length; i++) {
            ListNode opf = (ListNode) md.nodes[i];
            String op = ((TokenNode) opf.get("operator")).value;
            CSSMath right = factor(opf);
            if (left.values.size() > 1 && right.values.size() > 1 || (!right.values.containsKey(CSSUnit.NONE) && !left.values.containsKey(CSSUnit.NONE)))
                errorHandler.error("Invalid term.", CSSErrorType.INVALID_VALUE);
            CSSNumberUnit rightNum = right.values.get(CSSUnit.NONE);
            if (!right.values.containsKey(CSSUnit.NONE)) {
                CSSMath leftTemp = left;
                left = right;
                right = leftTemp;
                rightNum = right.values.get(CSSUnit.NONE);
            }
            if ("*".equals(op)) {
                for (CSSUnit unit : left.values.keySet()) {
                    left.values.get(unit).number *= rightNum.number;
                }
            }
            if ("/".equals(op)) {
                if (rightNum.number == 0)
                    errorHandler.error("Division by zero.", CSSErrorType.DIVISION_BY_ZERO);
                for (CSSUnit unit : left.values.keySet()) {
                    left.values.get(unit).number /= rightNum.number;
                }
            }
        }
        return left;
    }

    private CSSMath expression(ListNode expr) {
        Node t = expr.get("term_expression");
        CSSMath left = term((ListNode) t);
        Node asn = expr.get("add_or_subtract");
        if (asn == null) return left;
        ListNode as = (ListNode) asn;
        for (int i = 0; i < as.nodes.length; i++) {
            ListNode opt = (ListNode) as.nodes[i];
            String op = ((TokenNode) opt.get("operator")).value;
            CSSMath right = term((ListNode) opt.get("term_expression"));
            if ("+".equals(op)) {
                for (CSSUnit unit : left.values.keySet()) {
                    if (right.values.containsKey(unit)) {
                        left.values.get(unit).number += right.values.get(unit).number;
                    }
                }
                for (CSSUnit unit : right.values.keySet()) {
                    if (!left.values.containsKey(unit)) {
                        left.values.put(unit, new CSSNumberUnit(right.values.get(unit).number, unit));
                    }
                }
            }
            if ("-".equals(op)) {
                for (CSSUnit unit : left.values.keySet()) {
                    if (right.values.containsKey(unit)) {
                        left.values.get(unit).number -= right.values.get(unit).number;
                    }
                }
                for (CSSUnit unit : right.values.keySet()) {
                    if (!left.values.containsKey(unit)) {
                        left.values.put(unit, new CSSNumberUnit(-right.values.get(unit).number, unit));
                    }
                }
            }
        }
        return left;
    }

    private ListNode parse(String source) {
        List<Token> tokens = lexer.lex(source);
        try {
            return parser.parse(tokens);
        } catch (Exception e) {
            errorHandler.error("Parse error: \n" + e.getLocalizedMessage(), CSSErrorType.SYNTAX_ERROR);
        }
        return null;
    }

    private CSSValue value(Node n) {
        Node node = ((ListNode) n).nodes[0];
        CSSValue value = new CSSValue();
        if ("value_id".equals(node.name)) {
            value.type = CSSType.IDENTIFIER;
            String id = ((TokenNode) node).value;
            value.identifier = id;
            int len = id.length();
            if (id.charAt(0) == '#' && (len == 4 || len == 5 || len == 7 || len == 9) && isHexadecimal(id.substring(1))) {
                value.type = CSSType.COLOR;
                String hex = id.substring(1);
                value.color = new CSSColor();
                if (len == 4)
                    value.color.fromHex("#" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2) + "FF");
                else if (len == 5)
                    value.color.fromHex("#" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2) + hex.charAt(3) + hex.charAt(3));
                else if (len == 7)
                    value.color.fromHex(id + "FF");
                else
                    value.color.fromHex(id);

            } else {
                for (String c : CSSColors.getColorNames()) {
                    if (c.equals(id)) {
                        value.type = CSSType.COLOR;
                        value.color = CSSColors.get(c);
                        break;
                    }
                }
            }
        } else if ("value_str".equals(node.name)) {
            value.type = CSSType.STRING;
            value.string = ((TokenNode) node).value;
        } else if ("expression".equals(node.name)) {
            value.type = CSSType.MATH;
            value.math = expression((ListNode) node);
            if (value.math.values.size() == 1 && options.convertColorFunctions) {
                value.type = CSSType.NUMBER_UNIT;
                value.numberUnit = value.math.values.values().toArray(new CSSNumberUnit[0])[0];
            }
        } else if ("function".equals(node.name)) {
            ListNode funcNode = (ListNode) node;
            value.type = CSSType.FUNCTION;
            value.functionName = ((TokenNode) funcNode.get("function_name")).value;
            ArrayList<CSSValue> arguments = new ArrayList<>();
            Node[] values = ((ListNode) funcNode.get("values")).nodes;
            arguments.add(value(values[0]));
            if (values.length > 1) {
                for (Node v : ((ListNode) values[1]).nodes) {
                    Node val = ((ListNode) v).nodes[((ListNode) v).nodes.length - 1];
                    if (val instanceof TokenNode)
                        continue;
                    arguments.add(value(val));
                }
            }
            value.functionArguments = arguments.toArray(new CSSValue[0]);
            if (options.convertColorFunctions) {
                CSSValue[] args = value.functionArguments;
                value.type = CSSType.COLOR;
                if (value.functionName.equals("rgb") && args.length == 3 && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT)) {
                    value.color = new CSSColor(args[0].numberUnit.number / 255f, args[1].numberUnit.number / 255f, args[2].numberUnit.number / 255f, 1);
                } else if (value.functionName.equals("rgba") && args.length == 4 && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT)) {
                    value.color = new CSSColor(args[0].numberUnit.number / 255f, args[1].numberUnit.number / 255f, args[2].numberUnit.number / 255f, args[3].numberUnit.number);
                } else if (value.functionName.equals("hsl") && args.length == 3 && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT) && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.NONE, CSSUnit.PERCENT, CSSUnit.PERCENT)) {
                    value.color = new CSSColor().fromHSL(args[0].numberUnit.number / 360f, args[1].numberUnit.number / 100f, args[2].numberUnit.number / 100f, 1);
                } else if (value.functionName.equals("hsla") && args.length == 4 && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT) && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.NONE, CSSUnit.PERCENT, CSSUnit.PERCENT, CSSUnit.NONE)) {
                    value.color = new CSSColor().fromHSL(args[0].numberUnit.number / 360f, args[1].numberUnit.number / 100f, args[2].numberUnit.number / 100f, args[3].numberUnit.number);
                } else if (value.functionName.equals("hwb") && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT)) {
                    if (args.length == 3 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.NONE, CSSUnit.PERCENT, CSSUnit.PERCENT)) {
                        value.color = new CSSColor().fromHWB(args[0].numberUnit.number / 360f, args[1].numberUnit.number / 100f, args[2].numberUnit.number / 100f, 1);
                    } else if (args.length == 4 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.NONE, CSSUnit.PERCENT, CSSUnit.PERCENT, CSSUnit.NONE)) {
                        value.color = new CSSColor().fromHWB(args[0].numberUnit.number / 360f, args[1].numberUnit.number / 100f, args[2].numberUnit.number / 100f, args[3].numberUnit.number);
                    } else {
                        value.type = CSSType.FUNCTION;
                    }
                } else if (value.functionName.equals("lab") && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT)) {
                    if (args.length == 3 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.PERCENT, CSSUnit.NONE, CSSUnit.NONE)) {
                        value.color = new CSSColor().fromLAB(args[0].numberUnit.number / 100f, (args[1].numberUnit.number + 128) / 255f, (args[2].numberUnit.number + 128) / 255f, 1);
                    } else if (args.length == 4 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.PERCENT, CSSUnit.NONE, CSSUnit.NONE, CSSUnit.NONE)) {
                        value.color = new CSSColor().fromLAB(args[0].numberUnit.number / 100f, (args[1].numberUnit.number + 128) / 255f, (args[2].numberUnit.number + 128) / 255f, args[3].numberUnit.number);
                    } else {
                        value.type = CSSType.FUNCTION;
                    }
                } else if (value.functionName.equals("lch") && FunctionUtils.argumentsTypeCheck(args, CSSType.NUMBER_UNIT)) {
                    if (args.length == 3 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.PERCENT, CSSUnit.NONE, CSSUnit.NONE)) {
                        value.color = new CSSColor().fromLAB(args[0].numberUnit.number / 100f, args[1].numberUnit.number, args[2].numberUnit.number / 360f, 1);
                    } else if (args.length == 4 && FunctionUtils.argumentsNumberUnitCheck(args, CSSUnit.PERCENT, CSSUnit.NONE, CSSUnit.NONE, CSSUnit.NONE)) {
                        value.color = new CSSColor().fromLAB(args[0].numberUnit.number / 100f, args[1].numberUnit.number, args[2].numberUnit.number / 360f, args[3].numberUnit.number);
                    } else {
                        value.type = CSSType.FUNCTION;
                    }
                } else {
                    value.type = CSSType.FUNCTION;
                }
            }
        }
        return value;
    }

    private CSSExtendedValue extendedValue(Node n) {
        ListNode group = (ListNode) n;
        CSSValue value = value(group.nodes[0]);
        CSSExtendedValue extendedValue = new CSSExtendedValue();
        switch (value.type) {
            case STRING:
                extendedValue.type = CSSExtendedType.STRING;
                extendedValue.string = value.string;
                break;
            case IDENTIFIER:
                extendedValue.type = CSSExtendedType.IDENTIFIER;
                extendedValue.identifier = value.identifier;
                break;
            case NUMBER_UNIT:
                extendedValue.type = CSSExtendedType.NUMBER_UNIT;
                extendedValue.numberUnit = value.numberUnit;
                break;
            case MATH:
                errorHandler.error("Math expression is not allowed (Pseudo Class) " + n, CSSErrorType.INVALID_VALUE);
                break;
            case FUNCTION:
                errorHandler.error("Function is not allowed (Pseudo Class) " + n, CSSErrorType.INVALID_VALUE);
        }
        if ("compound_selector".equals(n.name)) {
            extendedValue.type = CSSExtendedType.COMPOUND_SELECTOR;
            extendedValue.compoundSelector = compoundSelector(n);
        }
        return extendedValue;
    }

    private CSSSelector.SimpleSelector simpleSelector(Node n) {
        CSSSelector.SimpleSelector simpleSelector = new CSSSelector.SimpleSelector();
        Node node = ((ListNode) n).nodes[0];
        if ("tag".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.TAG;
            simpleSelector.tag = ((TokenNode) node).value;
        } else if ("hash".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.HASH;
            simpleSelector.hash = ((TokenNode) node).value;
        } else if ("class".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.CLASS;
            simpleSelector.cls = ((TokenNode) node).value;
        } else if ("functional_pseudo_class".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.PSEUDO_CLASS;
            simpleSelector.pseudoClassName = ((TokenNode) ((ListNode) node).get("class")).value;
            simpleSelector.pseudoClassValue = extendedValue(((ListNode) node).get("extended_value"));
        } else if ("pseudo_class".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.PSEUDO_CLASS;
            simpleSelector.pseudoClassName = ((TokenNode) ((ListNode) node).get("class")).value;
        } else if ("attribute".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.ATTRIBUTE;
            simpleSelector.attributeName = ((TokenNode) ((ListNode) node).get("key")).value;
            simpleSelector.attributeValue = value(((ListNode) node).get("val"));
        } else if ("attribute_noval".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.ATTRIBUTE;
            simpleSelector.attributeName = ((TokenNode) ((ListNode) node).get("key")).value;
            simpleSelector.attributeValue = new CSSValue(); // true
        }
        return simpleSelector;
    }

    private CSSSelector.CompoundSelector compoundSelector(Node n) {
        CSSSelector.CompoundSelector compoundSelector = new CSSSelector.CompoundSelector();
        ListNode node = (ListNode) n;
        ArrayList<CSSSelector.SimpleSelector> simpleSelectors = new ArrayList<>();
        simpleSelectors.add(simpleSelector(node.nodes[0]));
        if (node.nodes.length == 2) {
            Node o = node.nodes[1];
            if ("pseudo_add".equals(o.name)) {
                ListNode pseudoAdd = (ListNode) o;
                compoundSelector.pseudoElement = ((TokenNode) ((ListNode) pseudoAdd.nodes[0]).nodes[2]).value;
                ListNode additional = (ListNode) pseudoAdd.nodes[1];
                for (Node sel : additional.nodes) {
                    simpleSelectors.add(simpleSelector(sel));
                }
            } else if ("add_pseudo".equals(o.name)) {
                ListNode addPseudo = (ListNode) o;
                compoundSelector.pseudoElement = ((TokenNode) ((ListNode) addPseudo.nodes[1]).nodes[2]).value;
                ListNode additional = (ListNode) addPseudo.nodes[0];
                for (Node sel : additional.nodes) {
                    simpleSelectors.add(simpleSelector(sel));
                }
            } else if ("additional_selectors".equals(o.name)) {
                ListNode additional = (ListNode) o;
                for (Node sel : additional.nodes) {
                    simpleSelectors.add(simpleSelector(sel));
                }
            } else if ("pseudo_element".equals(o.name)) {
                compoundSelector.pseudoElement = ((TokenNode) ((ListNode) o).nodes[2]).value;
            }
        }
        compoundSelector.simpleSelectors = simpleSelectors.toArray(new CSSSelector.SimpleSelector[0]);
        return compoundSelector;
    }

    private CSSSelector selector(Node n) {
        CSSSelector selector = new CSSSelector();
        ListNode node = (ListNode) n;
        ArrayList<CSSSelector.CompoundSelector> compoundSelectors = new ArrayList<>();
        ArrayList<String> combinators = new ArrayList<>();
        compoundSelectors.add(compoundSelector(node.get("compound_selector")));

        if (node.get("combinator_and_compound_selector") != null) {
            ListNode otherCompoundSelectors = (ListNode) node.get("combinator_and_compound_selector");
            for (Node c : otherCompoundSelectors.nodes) {
                if ("combinator_with_selector".equals(c.name)) {
                    ListNode list = (ListNode) c;
                    combinators.add(((TokenNode) list.get("combinator")).value);
                    compoundSelectors.add(compoundSelector(list.get("compound_selector")));
                } else {
                    compoundSelectors.add(compoundSelector(c));
                    combinators.add(null);
                }
            }
        }

        selector.selectors = compoundSelectors.toArray(new CSSSelector.CompoundSelector[0]);
        selector.combinators = combinators.toArray(new String[0]);
        return selector;
    }

    private CSSDeclaration createDeclaration(Node n) {
        CSSDeclaration declaration = new CSSDeclaration();
        ListNode node = (ListNode) n;
        declaration.property = ((TokenNode) node.get("property")).value;
        ArrayList<CSSValue> values = new ArrayList<>();
        for (Node v : ((ListNode) node.get("values")).nodes) {
            values.add(value(v));
        }
        declaration.important = node.get("important_semicolon") != null;
        declaration.values = values.toArray(new CSSValue[0]);
        return declaration;
    }

    public CSSStylesheet process(String source) {
        ListNode program = parse(source);
        if (program == null)
            return null;
        CSSStylesheet stylesheet = new CSSStylesheet();
        stylesheet.rules = new CSSRule[program.nodes.length];
        for (int i = 0; i < program.nodes.length; i++) {
            Node n = program.nodes[i];
            ListNode ruleNode = (ListNode) n;
            CSSRule rule = new CSSRule();
            ArrayList<CSSSelector> selectors = new ArrayList<>();
            ListNode selectorsNode = (ListNode) ruleNode.get("selectors");
            selectors.add(selector(selectorsNode.nodes[0]));
            if (selectorsNode.nodes.length > 1) {
                for (Node sel : ((ListNode) selectorsNode.get("others")).nodes) {
                    selectors.add(selector(((ListNode) sel).nodes[1]));
                }
            }
            rule.selectors = selectors.toArray(new CSSSelector[0]);
            ArrayList<CSSDeclaration> declarations = new ArrayList<>();
            for (Node declaration : ((ListNode) ruleNode.get("declarations")).nodes) {
                declarations.add(createDeclaration(declaration));
            }
            rule.declarations = declarations.toArray(new CSSDeclaration[0]);
            stylesheet.rules[i] = rule;
        }
        return stylesheet;
    }

}

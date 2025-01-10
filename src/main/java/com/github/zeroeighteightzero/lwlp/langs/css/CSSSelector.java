package com.github.zeroeighteightzero.lwlp.langs.css;

import com.github.zeroeighteightzero.lwlp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSSSelector {

    public static class CompoundSelector {
        public SimpleSelector[] simpleSelectors;
        public String pseudoElement;
    }

    public static class SimpleSelector {

        public enum Type {
            UNIVERSAL,
            TAG,
            HASH,
            CLASS,
            PSEUDO_CLASS,
            ATTRIBUTE
        }

        public Type type;

        public String tag;
        public String hash;
        public String cls;
        public String pseudoClassName;
        public CSSExtendedValue pseudoClassValue;
        public String attributeName;
        public CSSValue attributeValue;

    }

    public CompoundSelector[] selectors;
    public String[] combinators;

    public String getString(boolean pseudoClasses, boolean pseudoElement, boolean attributes, boolean compact) {
        StringBuilder sb = new StringBuilder();
        if (selectors.length != combinators.length + 1)
            throw new RuntimeException("Selectors and combinators length mismatch.");
        for (int i = 0; i < selectors.length; i++) {
            if (i > 0 && !compact)
                sb.append(' ').append(combinators[i - 1]).append(' ');
            else if (i > 0)
                sb.append(combinators[i - 1]);
            CompoundSelector compound = selectors[i];
            for (SimpleSelector simple : compound.simpleSelectors) {
                switch (simple.type) {
                    case TAG:
                        sb.append(simple.tag);
                        break;
                    case HASH:
                        sb.append(simple.hash);
                        break;
                    case CLASS:
                        sb.append(simple.cls);
                        break;
                    case PSEUDO_CLASS:
                        if (!pseudoClasses) break;
                        sb.append(':').append(simple.pseudoClassName);
                        if (simple.pseudoClassValue != null) {
                            sb.append('(');
                            sb.append(simple.pseudoClassValue);
                            sb.append(')');
                        }
                        break;
                    case ATTRIBUTE:
                        if (!attributes) break;
                        sb.append('[').append(simple.attributeName).append('=').append(simple.attributeValue).append(']');
                        break;
                }
            }
            if (pseudoElement && compound.pseudoElement != null) sb.append("::").append(compound.pseudoElement);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getString(true, true, true, false);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getString(true, true, true, true));
    }

    private static final Lexer lexer = new Lexer(
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
                    new TokenPattern("((\\-\\-)?[^\\W\\d][\\w-]*)|(\\#\\w+)", "IDENTIFIER"),
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

    private static final Parser parser = new Parser(
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
                                    new TokenMatch("universal", "ASTERISK"),
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

    // TODO move to utility function
    private static CSSValue value(Node n) {
        Node node = ((ListNode) n).nodes[0];
        CSSValue value = new CSSValue();
        if ("value_id".equals(node.name)) {
            value.type = CSSType.IDENTIFIER;
            String id = ((TokenNode) node).value;
            value.identifier = id;
            int len = id.length();
            if (id.charAt(0) == '#' && (len == 4 || len == 5 || len == 7 || len == 9) && CSS.isHexadecimal(id.substring(1))) {
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
            value.math = CSS.expression((ListNode) node, null);
            if (value.math.values.size() == 1) {
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
        }
        return value;
    }

    private static CSSExtendedValue extendedValue(Node n) {
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
        }
        if ("compound_selector".equals(n.name)) {
            extendedValue.type = CSSExtendedType.COMPOUND_SELECTOR;
            extendedValue.compoundSelector = compoundSelector(n);
        }
        return extendedValue;
    }

    private static CSSSelector.SimpleSelector simpleSelector(Node n) {
        CSSSelector.SimpleSelector simpleSelector = new CSSSelector.SimpleSelector();
        Node node = ((ListNode) n).nodes[0];
        if ("universal".equals(node.name)) {
            simpleSelector.type = CSSSelector.SimpleSelector.Type.UNIVERSAL;
        } else if ("tag".equals(node.name)) {
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

    private static CSSSelector.CompoundSelector compoundSelector(Node n) {
        CSSSelector.CompoundSelector compoundSelector = new CSSSelector.CompoundSelector();
        ListNode node = (ListNode) n;
        ArrayList<SimpleSelector> simpleSelectors = new ArrayList<>();
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

    public static CSSSelector fromString(String selector) {
        List<Token> tokens = lexer.lex(selector);
        ListNode node = parser.parse(tokens);
        CSSSelector cssSelector = new CSSSelector();
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

        cssSelector.selectors = compoundSelectors.toArray(new CSSSelector.CompoundSelector[0]);
        cssSelector.combinators = combinators.toArray(new String[0]);

        return cssSelector;
    }

}

package com.github.zeroeighteightzero.lwlp.langs.css;

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

}

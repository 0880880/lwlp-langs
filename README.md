# Language Implementations with [LWLP](https://github.com/0880880/lwlp)

This repository contains implementations of various languages using LWLP. Currently, only the CSS implementation is
available.

- [CSS Implementation](#css)

## CSS

The CSS implementation is available in
the [CSS package](https://github.com/0880880/lwlp-langs/tree/core/src/main/java/com/github/zeroeighteightzero/lwlp/langs/css).


<details>
  <summary>CSS Features</summary>

This CSS implementation supports comprehensive CSS parsing and manipulation, including:

### Selectors and Rules

- Full support for selectors like tag, class, ID, and universal
- Compound selectors with combinators (>, ~)
- Pseudo-classes and pseudo-elements
- Attribute selectors
- Rule declarations with property-value pairs

### Color Support

- RGB/RGBA and HSL/HSLA color values
- HSV/HSVA color handling
- Advanced color spaces:
    - CIELAB with D50 illuminant
    - LCH, OKLCH, and OKLAB
- Named colors and hex values

### Values and Units

- Numbers with various units
- Length units (px, em, rem, etc.)
- Percentages and calculations
- String values, identifiers, and functions

### Additional Features

- CSS comment parsing
- `!important` declarations
- Error handling and validation
- Configurable parsing options
- Math expression evaluation
- Stylesheet editing features
- Variables

</details>

<br>

<details>
  <summary>CSS Code Example</summary>

### Example CSS Code

  ```css
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
    color: #333;
}

header {
    background-color: #0073e6;
    color: white;
    padding: 20px;
    text-align: center;
}
```

### Output

  ```
Object: CSSStylesheet
Field: rules
    Object: CSSRule[]
    Array Element [0]:
    	Object: CSSRule
    	Field: selectors
    	    Object: CSSSelector[]
    	    Array Element [0]:
    	    	Object: CSSSelector
    	    	Field: selectors
    	    	    Object: CompoundSelector[]
    	    	    Array Element [0]:
    	    	    	Object: CompoundSelector
    	    	    	Field: simpleSelectors
    	    	    	    Object: SimpleSelector[]
    	    	    	    Array Element [0]:
    	    	    	    	Object: SimpleSelector
    	    	    	    	Field: type
    	    	    	    	    Object: Type
    	    	    	    	    Value: TAG
    	    	    	    	Field: tag
    	    	    	    	    Object: String
    	    	    	    	    Value: body
    	    	    	    	Field: hash
    	    	    	    	    null
    	    	    	    	Field: cls
    	    	    	    	    null
    	    	    	    	Field: pseudoClassName
    	    	    	    	    null
    	    	    	    	Field: pseudoClassValue
    	    	    	    	    null
    	    	    	    	Field: attributeName
    	    	    	    	    null
    	    	    	    	Field: attributeValue
    	    	    	    	    null
    	    	    	Field: pseudoElement
    	    	    	    null
    	    	Field: combinators
    	    	    Object: String[]
    	Field: declarations
    	    Object: CSSDeclaration[]
    	    Array Element [0]:
    	    	Object: CSSDeclaration
    	    	Field: property
    	    	    Object: String
    	    	    Value: font-family
    	    	Field: values
    	    	    Object: CSSValue[]
    	    	    Array Element [0]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    Object: CSSType
    	    	    	    Value: IDENTIFIER
    	    	    	Field: identifier
    	    	    	    Object: String
    	    	    	    Value: Arial
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    null
    	    	    	Field: math
    	    	    	    null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    null
    	    	    Array Element [1]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    null
    	    	    	Field: identifier
    	    	    	    null
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    null
    	    	    	Field: math
    	    	    	    null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    null
    	    	Field: important
    	    	    Object: Boolean
    	    	    Value: false
    	    Array Element [1]:
    	    	Object: CSSDeclaration
    	    	Field: property
    	    	    Object: String
    	    	    Value: margin
    	    	Field: values
    	    	    Object: CSSValue[]
    	    	    Array Element [0]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    Object: CSSType
    	    	    	    Value: NUMBER_UNIT
    	    	    	Field: identifier
    	    	    	    null
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    Object: CSSNumberUnit
    	    	    	    Field: number
    	    	    	        Object: Float
    	    	    	        Value: 0.0
    	    	    	    Field: unit
    	    	    	        Object: CSSUnit
    	    	    	        Value: null
    	    	    	Field: math
    	    	    	    Object: CSSMath
    	    	    	    Field: values
    	    	    	        Object: HashMap
    	    	    	        Map Entry Key:
    	    	    	            Object: CSSUnit
    	    	    	            Value: null
    	    	    	        Map Entry Value:
    	    	    	            Object: CSSNumberUnit
    	    	    	            Field: number
    	    	    	                Object: Float
    	    	    	                Value: 0.0
    	    	    	            Field: unit
    	    	    	                Object: CSSUnit
    	    	    	                Value: null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    null
    	    	Field: important
    	    	    Object: Boolean
    	    	    Value: false
    	    Array Element [2]:
    	    	Object: CSSDeclaration
    	    	Field: property
    	    	    Object: String
    	    	    Value: padding
    	    	Field: values
    	    	    Object: CSSValue[]
    	    	    Array Element [0]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    Object: CSSType
    	    	    	    Value: NUMBER_UNIT
    	    	    	Field: identifier
    	    	    	    null
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    Object: CSSNumberUnit
    	    	    	    Field: number
    	    	    	        Object: Float
    	    	    	        Value: 0.0
    	    	    	    Field: unit
    	    	    	        Object: CSSUnit
    	    	    	        Value: null
    	    	    	Field: math
    	    	    	    Object: CSSMath
    	    	    	    Field: values
    	    	    	        Object: HashMap
    	    	    	        Map Entry Key:
    	    	    	            Object: CSSUnit
    	    	    	            Value: null
    	    	    	        Map Entry Value:
    	    	    	            Object: CSSNumberUnit
    	    	    	            Field: number
    	    	    	                Object: Float
    	    	    	                Value: 0.0
    	    	    	            Field: unit
    	    	    	                Object: CSSUnit
    	    	    	                Value: null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    null
    	    	Field: important
    	    	    Object: Boolean
    	    	    Value: false
    	    Array Element [3]:
    	    	Object: CSSDeclaration
    	    	Field: property
    	    	    Object: String
    	    	    Value: background-color
    	    	Field: values
    	    	    Object: CSSValue[]
    	    	    Array Element [0]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    Object: CSSType
    	    	    	    Value: COLOR
    	    	    	Field: identifier
    	    	    	    Object: String
    	    	    	    Value: #f4f4f4
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    null
    	    	    	Field: math
    	    	    	    null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    Object: CSSColor
    	    	    	    Field: r
    	    	    	        Object: Float
    	    	    	        Value: 0.9568628
    	    	    	    Field: g
    	    	    	        Object: Float
    	    	    	        Value: 0.9568628
    	    	    	    Field: b
    	    	    	        Object: Float
    	    	    	        Value: 0.9568628
    	    	    	    Field: a
    	    	    	        Object: Float
    	    	    	        Value: 1.0
    	    	    	    Field: currentColor
    	    	    	        Object: Boolean
    	    	    	        Value: false
    	    	Field: important
    	    	    Object: Boolean
    	    	    Value: false
    	    Array Element [4]:
    	    	Object: CSSDeclaration
    	    	Field: property
    	    	    Object: String
    	    	    Value: color
    	    	Field: values
    	    	    Object: CSSValue[]
    	    	    Array Element [0]:
    	    	    	Object: CSSValue
    	    	    	Field: type
    	    	    	    Object: CSSType
    	    	    	    Value: COLOR
    	    	    	Field: identifier
    	    	    	    Object: String
    	    	    	    Value: #fff
    	    	    	Field: string
    	    	    	    null
    	    	    	Field: numberUnit
    	    	    	    null
    	    	    	Field: math
    	    	    	    null
    	    	    	Field: functionName
    	    	    	    null
    	    	    	Field: functionArguments
    	    	    	    null
    	    	    	Field: color
    	    	    	    Object: CSSColor
    	    	    	    Field: r
    	    	    	        Object: Float
    	    	    	        Value: 1.0
    	    	    	    Field: g
    	    	    	        Object: Float
    	    	    	        Value: 1.0
    	    	    	    Field: b
    	    	    	        Object: Float
    	    	    	        Value: 1.0
    	    	    	    Field: a
    	    	    	        Object: Float
    	    	    	        Value: 1.0
    	    	    	    Field: currentColor
    	    	    	        Object: Boolean
    	    	    	        Value: false
    	    	Field: important
    	    	    Object: Boolean
    	    	    Value: false
    Array Element [1]:
    	...
  ```

</details>
/**
 *	This generated bean class CoveredPattern
 *	matches the schema element 'covered-pattern'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class CoveredPattern {
	private String _PatternCode;

	public CoveredPattern() {
		_PatternCode = "";
	}

	/**
	 * Required parameters constructor
	 */
	public CoveredPattern(String patternCode) {
		_PatternCode = patternCode;
	}

	/**
	 * Deep copy
	 */
	public CoveredPattern(org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern source) {
		_PatternCode = source._PatternCode;
	}

	// This attribute is mandatory
	public void setPatternCode(String value) {
		_PatternCode = value;
	}

	public String getPatternCode() {
		return _PatternCode;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		if (_PatternCode != null) {
			out.write(nextIndent);
			out.write("<pattern-code");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.writeXML(out, _PatternCode, false);
			out.write("</pattern-code>\n");	// NOI18N
		}
		out.write(indent);
		out.write("</"+nodeName+">\n");
	}

	public void readNode(org.w3c.dom.Node node) {
		org.w3c.dom.NodeList children = node.getChildNodes();
		for (int i = 0, size = children.getLength(); i < size; ++i) {
			org.w3c.dom.Node childNode = children.item(i);
			String childNodeName = (childNode.getLocalName() == null ? childNode.getNodeName().intern() : childNode.getLocalName().intern());
			String childNodeValue = "";
			if (childNode.getFirstChild() != null) {
				childNodeValue = childNode.getFirstChild().getNodeValue();
			}
			if (childNodeName == "pattern-code") {
				_PatternCode = childNodeValue;
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property patternCode
		if (getPatternCode() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getPatternCode() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "patternCode", this);	// NOI18N
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "patternCode")
			setPatternCode((String)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for CoveredPattern");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "patternCode")
			return getPatternCode();
		throw new IllegalArgumentException(name+" is not a valid property name for CoveredPattern");
	}

	public String nameSelf() {
		return "covered-pattern";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _PatternCode) {
				return "patternCode";
			}
		}
		return null;
	}

	/**
	 * Return an array of all of the properties that are beans and are set.
	 */
	public java.lang.Object[] childBeans(boolean recursive) {
		java.util.List children = new java.util.LinkedList();
		childBeans(recursive, children);
		java.lang.Object[] result = new java.lang.Object[children.size()];
		return (java.lang.Object[]) children.toArray(result);
	}

	/**
	 * Put all child beans into the beans list.
	 */
	public void childBeans(boolean recursive, java.util.List beans) {
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern inst = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern) o;
		if (!(_PatternCode == null ? inst._PatternCode == null : _PatternCode.equals(inst._PatternCode))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_PatternCode == null ? 0 : _PatternCode.hashCode());
		return result;
	}

}


/*
		The following schema file has been used for generation:

<?xml version='1.0' encoding='UTF-8'?>

<!--- Represents a base of inflection patterns. -->
<!ELEMENT inflection-patterns-base (inflection-pattern-definition)+>

<!--- Definition of single inflection pattern. -->
<!ELEMENT inflection-pattern-definition (pattern-code, pattern, cores, covered-patterns)>

<!--- String representation of inflection pattern. -->
<!ELEMENT pattern-code (#PCDATA)>

<!--- Definitions of form patterns. -->
<!ELEMENT pattern (form-pattern)+>

<!--- Single form pattern. -->
<!ELEMENT form-pattern (affixes, grammatical-properties-list)>

<!--- Pattern of grammatical morphemes. -->
<!ELEMENT affixes (#PCDATA)>

<!--- List of grammatical properties. -->
<!ELEMENT grammatical-properties-list (grammatical-properties-mark)+>

<!--- Single mark representing grammatical properties. -->
<!ELEMENT grammatical-properties-mark (#PCDATA)>

<!--- List of space separated cores covered by an inflection pattern. -->
<!ELEMENT cores (#PCDATA)>

<!--- Inflection patterns covered by the definition of inflection pattern. -->
<!ELEMENT covered-patterns (covered-pattern)+>

<!--- Single inflection pattern covered by a definition. -->
<!ELEMENT covered-pattern (pattern-code)>

*/

/**
 *	This generated bean class InflectionPatternDefinition
 *	matches the schema element 'inflection-pattern-definition'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class InflectionPatternDefinition {
	private String _PatternCode;
	private Pattern _Pattern;
	private String _Cores;
	private CoveredPatterns _CoveredPatterns;

	public InflectionPatternDefinition() {
		_PatternCode = "";
		_Pattern = new Pattern();
		_Cores = "";
		_CoveredPatterns = new CoveredPatterns();
	}

	/**
	 * Required parameters constructor
	 */
	public InflectionPatternDefinition(String patternCode, org.neurpheus.nlp.morphology.inflection.xml.Pattern pattern, String cores, org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns coveredPatterns) {
		_PatternCode = patternCode;
		_Pattern = pattern;
		_Cores = cores;
		_CoveredPatterns = coveredPatterns;
	}

	/**
	 * Deep copy
	 */
	public InflectionPatternDefinition(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition source) {
		_PatternCode = source._PatternCode;
		_Pattern = (source._Pattern == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.Pattern(source._Pattern);
		_Cores = source._Cores;
		_CoveredPatterns = (source._CoveredPatterns == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns(source._CoveredPatterns);
	}

	// This attribute is mandatory
	public void setPatternCode(String value) {
		_PatternCode = value;
	}

	public String getPatternCode() {
		return _PatternCode;
	}

	// This attribute is mandatory
	public void setPattern(org.neurpheus.nlp.morphology.inflection.xml.Pattern value) {
		_Pattern = value;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.Pattern getPattern() {
		return _Pattern;
	}

	// This attribute is mandatory
	public void setCores(String value) {
		_Cores = value;
	}

	public String getCores() {
		return _Cores;
	}

	// This attribute is mandatory
	public void setCoveredPatterns(org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns value) {
		_CoveredPatterns = value;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns getCoveredPatterns() {
		return _CoveredPatterns;
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
		if (_Pattern != null) {
			_Pattern.writeNode(out, "pattern", nextIndent);
		}
		if (_Cores != null) {
			out.write(nextIndent);
			out.write("<cores");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.writeXML(out, _Cores, false);
			out.write("</cores>\n");	// NOI18N
		}
		if (_CoveredPatterns != null) {
			_CoveredPatterns.writeNode(out, "covered-patterns", nextIndent);
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
			else if (childNodeName == "pattern") {
				_Pattern = new org.neurpheus.nlp.morphology.inflection.xml.Pattern();
				_Pattern.readNode(childNode);
			}
			else if (childNodeName == "cores") {
				_Cores = childNodeValue;
			}
			else if (childNodeName == "covered-patterns") {
				_CoveredPatterns = new org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns();
				_CoveredPatterns.readNode(childNode);
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
		// Validating property pattern
		if (getPattern() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getPattern() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "pattern", this);	// NOI18N
		}
		getPattern().validate();
		// Validating property cores
		if (getCores() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getCores() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "cores", this);	// NOI18N
		}
		// Validating property coveredPatterns
		if (getCoveredPatterns() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getCoveredPatterns() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "coveredPatterns", this);	// NOI18N
		}
		getCoveredPatterns().validate();
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "patternCode")
			setPatternCode((String)value);
		else if (name == "pattern")
			setPattern((Pattern)value);
		else if (name == "cores")
			setCores((String)value);
		else if (name == "coveredPatterns")
			setCoveredPatterns((CoveredPatterns)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for InflectionPatternDefinition");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "patternCode")
			return getPatternCode();
		if (name == "pattern")
			return getPattern();
		if (name == "cores")
			return getCores();
		if (name == "coveredPatterns")
			return getCoveredPatterns();
		throw new IllegalArgumentException(name+" is not a valid property name for InflectionPatternDefinition");
	}

	public String nameSelf() {
		return "inflection-pattern-definition";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof Pattern) {
			Pattern child = (Pattern) childObj;
			if (child == _Pattern) {
				return "pattern";
			}
		}
		if (childObj instanceof CoveredPatterns) {
			CoveredPatterns child = (CoveredPatterns) childObj;
			if (child == _CoveredPatterns) {
				return "coveredPatterns";
			}
		}
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _PatternCode) {
				return "patternCode";
			}
			if (child == _Cores) {
				return "cores";
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
		if (_Pattern != null) {
			if (recursive) {
				_Pattern.childBeans(true, beans);
			}
			beans.add(_Pattern);
		}
		if (_CoveredPatterns != null) {
			if (recursive) {
				_CoveredPatterns.childBeans(true, beans);
			}
			beans.add(_CoveredPatterns);
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition inst = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition) o;
		if (!(_PatternCode == null ? inst._PatternCode == null : _PatternCode.equals(inst._PatternCode))) {
			return false;
		}
		if (!(_Pattern == null ? inst._Pattern == null : _Pattern.equals(inst._Pattern))) {
			return false;
		}
		if (!(_Cores == null ? inst._Cores == null : _Cores.equals(inst._Cores))) {
			return false;
		}
		if (!(_CoveredPatterns == null ? inst._CoveredPatterns == null : _CoveredPatterns.equals(inst._CoveredPatterns))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_PatternCode == null ? 0 : _PatternCode.hashCode());
		result = 37*result + (_Pattern == null ? 0 : _Pattern.hashCode());
		result = 37*result + (_Cores == null ? 0 : _Cores.hashCode());
		result = 37*result + (_CoveredPatterns == null ? 0 : _CoveredPatterns.hashCode());
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

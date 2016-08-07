/**
 *	This generated bean class FormPattern
 *	matches the schema element 'form-pattern'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class FormPattern {
	private String _Affixes;
	private GrammaticalPropertiesList _GrammaticalPropertiesList;

	public FormPattern() {
		_Affixes = "";
		_GrammaticalPropertiesList = new GrammaticalPropertiesList();
	}

	/**
	 * Required parameters constructor
	 */
	public FormPattern(String affixes, org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList grammaticalPropertiesList) {
		_Affixes = affixes;
		_GrammaticalPropertiesList = grammaticalPropertiesList;
	}

	/**
	 * Deep copy
	 */
	public FormPattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern source) {
		_Affixes = source._Affixes;
		_GrammaticalPropertiesList = (source._GrammaticalPropertiesList == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList(source._GrammaticalPropertiesList);
	}

	// This attribute is mandatory
	public void setAffixes(String value) {
		_Affixes = value;
	}

	public String getAffixes() {
		return _Affixes;
	}

	// This attribute is mandatory
	public void setGrammaticalPropertiesList(org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList value) {
		_GrammaticalPropertiesList = value;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList getGrammaticalPropertiesList() {
		return _GrammaticalPropertiesList;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		if (_Affixes != null) {
			out.write(nextIndent);
			out.write("<affixes");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.writeXML(out, _Affixes, false);
			out.write("</affixes>\n");	// NOI18N
		}
		if (_GrammaticalPropertiesList != null) {
			_GrammaticalPropertiesList.writeNode(out, "grammatical-properties-list", nextIndent);
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
			if (childNodeName == "affixes") {
				_Affixes = childNodeValue;
			}
			else if (childNodeName == "grammatical-properties-list") {
				_GrammaticalPropertiesList = new org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList();
				_GrammaticalPropertiesList.readNode(childNode);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property affixes
		if (getAffixes() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getAffixes() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "affixes", this);	// NOI18N
		}
		// Validating property grammaticalPropertiesList
		if (getGrammaticalPropertiesList() == null) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("getGrammaticalPropertiesList() == null", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "grammaticalPropertiesList", this);	// NOI18N
		}
		getGrammaticalPropertiesList().validate();
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "affixes")
			setAffixes((String)value);
		else if (name == "grammaticalPropertiesList")
			setGrammaticalPropertiesList((GrammaticalPropertiesList)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for FormPattern");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "affixes")
			return getAffixes();
		if (name == "grammaticalPropertiesList")
			return getGrammaticalPropertiesList();
		throw new IllegalArgumentException(name+" is not a valid property name for FormPattern");
	}

	public String nameSelf() {
		return "form-pattern";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof GrammaticalPropertiesList) {
			GrammaticalPropertiesList child = (GrammaticalPropertiesList) childObj;
			if (child == _GrammaticalPropertiesList) {
				return "grammaticalPropertiesList";
			}
		}
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _Affixes) {
				return "affixes";
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
		if (_GrammaticalPropertiesList != null) {
			if (recursive) {
				_GrammaticalPropertiesList.childBeans(true, beans);
			}
			beans.add(_GrammaticalPropertiesList);
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.FormPattern))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.FormPattern inst = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern) o;
		if (!(_Affixes == null ? inst._Affixes == null : _Affixes.equals(inst._Affixes))) {
			return false;
		}
		if (!(_GrammaticalPropertiesList == null ? inst._GrammaticalPropertiesList == null : _GrammaticalPropertiesList.equals(inst._GrammaticalPropertiesList))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_Affixes == null ? 0 : _Affixes.hashCode());
		result = 37*result + (_GrammaticalPropertiesList == null ? 0 : _GrammaticalPropertiesList.hashCode());
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

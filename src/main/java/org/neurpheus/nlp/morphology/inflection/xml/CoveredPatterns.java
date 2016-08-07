/**
 *	This generated bean class CoveredPatterns
 *	matches the schema element 'covered-patterns'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class CoveredPatterns {
	private java.util.List _CoveredPattern = new java.util.ArrayList();	// List<CoveredPattern>

	public CoveredPatterns() {
	}

	/**
	 * Required parameters constructor
	 */
	public CoveredPatterns(org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern[] coveredPattern) {
		if (coveredPattern!= null) {
			for (int i = 0; i < coveredPattern.length; ++i) {
				_CoveredPattern.add(coveredPattern[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public CoveredPatterns(org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns source) {
		for (java.util.Iterator it = source._CoveredPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it.next();
			_CoveredPattern.add((element == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern(element));
		}
	}

	// This attribute is an array containing at least one element
	public void setCoveredPattern(org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern[] value) {
		if (value == null)
			value = new CoveredPattern[0];
		_CoveredPattern.clear();
		for (int i = 0; i < value.length; ++i) {
			_CoveredPattern.add(value[i]);
		}
	}

	public void setCoveredPattern(int index, org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern value) {
		_CoveredPattern.set(index, value);
	}

	public org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern[] getCoveredPattern() {
		CoveredPattern[] arr = new CoveredPattern[_CoveredPattern.size()];
		return (CoveredPattern[]) _CoveredPattern.toArray(arr);
	}

	public java.util.List fetchCoveredPatternList() {
		return _CoveredPattern;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern getCoveredPattern(int index) {
		return (CoveredPattern)_CoveredPattern.get(index);
	}

	// Return the number of coveredPattern
	public int sizeCoveredPattern() {
		return _CoveredPattern.size();
	}

	public int addCoveredPattern(org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern value) {
		_CoveredPattern.add(value);
		int positionOfNewItem = _CoveredPattern.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeCoveredPattern(org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern value) {
		int pos = _CoveredPattern.indexOf(value);
		if (pos >= 0) {
			_CoveredPattern.remove(pos);
		}
		return pos;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _CoveredPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it.next();
			if (element != null) {
				element.writeNode(out, "covered-pattern", nextIndent);
			}
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
			if (childNodeName == "covered-pattern") {
				CoveredPattern aCoveredPattern = new org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern();
				aCoveredPattern.readNode(childNode);
				_CoveredPattern.add(aCoveredPattern);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property coveredPattern
		if (sizeCoveredPattern() == 0) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("sizeCoveredPattern() == 0", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "coveredPattern", this);	// NOI18N
		}
		for (int _index = 0; _index < sizeCoveredPattern(); ++_index) {
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = getCoveredPattern(_index);
			if (element != null) {
				element.validate();
			}
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "coveredPattern")
			addCoveredPattern((CoveredPattern)value);
		else if (name == "coveredPattern[]")
			setCoveredPattern((CoveredPattern[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for CoveredPatterns");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "coveredPattern[]")
			return getCoveredPattern();
		throw new IllegalArgumentException(name+" is not a valid property name for CoveredPatterns");
	}

	public String nameSelf() {
		return "covered-patterns";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof CoveredPattern) {
			CoveredPattern child = (CoveredPattern) childObj;
			int index = 0;
			for (java.util.Iterator it = _CoveredPattern.iterator(); 
				it.hasNext(); ) {
				org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it.next();
				if (child == element) {
					return "coveredPattern."+index;
				}
				++index;
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
		for (java.util.Iterator it = _CoveredPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it.next();
			if (element != null) {
				if (recursive) {
					element.childBeans(true, beans);
				}
				beans.add(element);
			}
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns inst = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns) o;
		if (sizeCoveredPattern() != inst.sizeCoveredPattern())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _CoveredPattern.iterator(), it2 = inst._CoveredPattern.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it.next();
			org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern element2 = (org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_CoveredPattern == null ? 0 : _CoveredPattern.hashCode());
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

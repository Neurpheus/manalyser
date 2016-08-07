/**
 *	This generated bean class GrammaticalPropertiesList
 *	matches the schema element 'grammatical-properties-list'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class GrammaticalPropertiesList {
	private java.util.List _GrammaticalPropertiesMark = new java.util.ArrayList();	// List<String>

	public GrammaticalPropertiesList() {
	}

	/**
	 * Required parameters constructor
	 */
	public GrammaticalPropertiesList(String[] grammaticalPropertiesMark) {
		if (grammaticalPropertiesMark!= null) {
			for (int i = 0; i < grammaticalPropertiesMark.length; ++i) {
				_GrammaticalPropertiesMark.add(grammaticalPropertiesMark[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public GrammaticalPropertiesList(org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList source) {
		for (java.util.Iterator it = source._GrammaticalPropertiesMark.iterator(); 
			it.hasNext(); ) {
			String element = (String)it.next();
			_GrammaticalPropertiesMark.add(element);
		}
	}

	// This attribute is an array containing at least one element
	public void setGrammaticalPropertiesMark(String[] value) {
		if (value == null)
			value = new String[0];
		_GrammaticalPropertiesMark.clear();
		for (int i = 0; i < value.length; ++i) {
			_GrammaticalPropertiesMark.add(value[i]);
		}
	}

	public void setGrammaticalPropertiesMark(int index, String value) {
		_GrammaticalPropertiesMark.set(index, value);
	}

	public String[] getGrammaticalPropertiesMark() {
		String[] arr = new String[_GrammaticalPropertiesMark.size()];
		return (String[]) _GrammaticalPropertiesMark.toArray(arr);
	}

	public java.util.List fetchGrammaticalPropertiesMarkList() {
		return _GrammaticalPropertiesMark;
	}

	public String getGrammaticalPropertiesMark(int index) {
		return (String)_GrammaticalPropertiesMark.get(index);
	}

	// Return the number of grammaticalPropertiesMark
	public int sizeGrammaticalPropertiesMark() {
		return _GrammaticalPropertiesMark.size();
	}

	public int addGrammaticalPropertiesMark(String value) {
		_GrammaticalPropertiesMark.add(value);
		int positionOfNewItem = _GrammaticalPropertiesMark.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeGrammaticalPropertiesMark(String value) {
		int pos = _GrammaticalPropertiesMark.indexOf(value);
		if (pos >= 0) {
			_GrammaticalPropertiesMark.remove(pos);
		}
		return pos;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _GrammaticalPropertiesMark.iterator(); 
			it.hasNext(); ) {
			String element = (String)it.next();
			if (element != null) {
				out.write(nextIndent);
				out.write("<grammatical-properties-mark");	// NOI18N
				out.write(">");	// NOI18N
				org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.writeXML(out, element, false);
				out.write("</grammatical-properties-mark>\n");	// NOI18N
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
			if (childNodeName == "grammatical-properties-mark") {
				String aGrammaticalPropertiesMark;
				aGrammaticalPropertiesMark = childNodeValue;
				_GrammaticalPropertiesMark.add(aGrammaticalPropertiesMark);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property grammaticalPropertiesMark
		if (sizeGrammaticalPropertiesMark() == 0) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("sizeGrammaticalPropertiesMark() == 0", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "grammaticalPropertiesMark", this);	// NOI18N
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "grammaticalPropertiesMark")
			addGrammaticalPropertiesMark((String)value);
		else if (name == "grammaticalPropertiesMark[]")
			setGrammaticalPropertiesMark((String[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for GrammaticalPropertiesList");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "grammaticalPropertiesMark[]")
			return getGrammaticalPropertiesMark();
		throw new IllegalArgumentException(name+" is not a valid property name for GrammaticalPropertiesList");
	}

	public String nameSelf() {
		return "grammatical-properties-list";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof String) {
			String child = (String) childObj;
			int index = 0;
			for (java.util.Iterator it = _GrammaticalPropertiesMark.iterator(); 
				it.hasNext(); ) {
				String element = (String)it.next();
				if (child == element) {
					return "grammaticalPropertiesMark."+index;
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
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList inst = (org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList) o;
		if (sizeGrammaticalPropertiesMark() != inst.sizeGrammaticalPropertiesMark())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _GrammaticalPropertiesMark.iterator(), it2 = inst._GrammaticalPropertiesMark.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			String element = (String)it.next();
			String element2 = (String)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_GrammaticalPropertiesMark == null ? 0 : _GrammaticalPropertiesMark.hashCode());
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

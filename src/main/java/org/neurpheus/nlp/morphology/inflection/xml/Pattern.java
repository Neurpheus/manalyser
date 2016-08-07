/**
 *	This generated bean class Pattern
 *	matches the schema element 'pattern'.
 *  The root bean class is InflectionPatternsBase
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class Pattern {
	private java.util.List _FormPattern = new java.util.ArrayList();	// List<FormPattern>

	public Pattern() {
	}

	/**
	 * Required parameters constructor
	 */
	public Pattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern[] formPattern) {
		if (formPattern!= null) {
			for (int i = 0; i < formPattern.length; ++i) {
				_FormPattern.add(formPattern[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public Pattern(org.neurpheus.nlp.morphology.inflection.xml.Pattern source) {
		for (java.util.Iterator it = source._FormPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it.next();
			_FormPattern.add((element == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.FormPattern(element));
		}
	}

	// This attribute is an array containing at least one element
	public void setFormPattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern[] value) {
		if (value == null)
			value = new FormPattern[0];
		_FormPattern.clear();
		for (int i = 0; i < value.length; ++i) {
			_FormPattern.add(value[i]);
		}
	}

	public void setFormPattern(int index, org.neurpheus.nlp.morphology.inflection.xml.FormPattern value) {
		_FormPattern.set(index, value);
	}

	public org.neurpheus.nlp.morphology.inflection.xml.FormPattern[] getFormPattern() {
		FormPattern[] arr = new FormPattern[_FormPattern.size()];
		return (FormPattern[]) _FormPattern.toArray(arr);
	}

	public java.util.List fetchFormPatternList() {
		return _FormPattern;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.FormPattern getFormPattern(int index) {
		return (FormPattern)_FormPattern.get(index);
	}

	// Return the number of formPattern
	public int sizeFormPattern() {
		return _FormPattern.size();
	}

	public int addFormPattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern value) {
		_FormPattern.add(value);
		int positionOfNewItem = _FormPattern.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeFormPattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern value) {
		int pos = _FormPattern.indexOf(value);
		if (pos >= 0) {
			_FormPattern.remove(pos);
		}
		return pos;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _FormPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it.next();
			if (element != null) {
				element.writeNode(out, "form-pattern", nextIndent);
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
			if (childNodeName == "form-pattern") {
				FormPattern aFormPattern = new org.neurpheus.nlp.morphology.inflection.xml.FormPattern();
				aFormPattern.readNode(childNode);
				_FormPattern.add(aFormPattern);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property formPattern
		if (sizeFormPattern() == 0) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("sizeFormPattern() == 0", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "formPattern", this);	// NOI18N
		}
		for (int _index = 0; _index < sizeFormPattern(); ++_index) {
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = getFormPattern(_index);
			if (element != null) {
				element.validate();
			}
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "formPattern")
			addFormPattern((FormPattern)value);
		else if (name == "formPattern[]")
			setFormPattern((FormPattern[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for Pattern");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "formPattern[]")
			return getFormPattern();
		throw new IllegalArgumentException(name+" is not a valid property name for Pattern");
	}

	public String nameSelf() {
		return "pattern";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof FormPattern) {
			FormPattern child = (FormPattern) childObj;
			int index = 0;
			for (java.util.Iterator it = _FormPattern.iterator(); 
				it.hasNext(); ) {
				org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it.next();
				if (child == element) {
					return "formPattern."+index;
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
		for (java.util.Iterator it = _FormPattern.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it.next();
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
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.Pattern))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.Pattern inst = (org.neurpheus.nlp.morphology.inflection.xml.Pattern) o;
		if (sizeFormPattern() != inst.sizeFormPattern())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _FormPattern.iterator(), it2 = inst._FormPattern.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it.next();
			org.neurpheus.nlp.morphology.inflection.xml.FormPattern element2 = (org.neurpheus.nlp.morphology.inflection.xml.FormPattern)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_FormPattern == null ? 0 : _FormPattern.hashCode());
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

/**
 *	This generated bean class TagModification
 *	matches the schema element 'tag-modification'.
 *  The root bean class is TaggingInfo
 *
 *	Generated on Sun Aug 07 18:18:26 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.builder.xml;

public class TagModification {
	private java.util.List _Replacement = new java.util.ArrayList();	// List<Replacement>

	public TagModification() {
	}

	/**
	 * Deep copy
	 */
	public TagModification(org.neurpheus.nlp.morphology.builder.xml.TagModification source) {
		for (java.util.Iterator it = source._Replacement.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.builder.xml.Replacement element = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it.next();
			_Replacement.add((element == null) ? null : new org.neurpheus.nlp.morphology.builder.xml.Replacement(element));
		}
	}

	// This attribute is an array, possibly empty
	public void setReplacement(org.neurpheus.nlp.morphology.builder.xml.Replacement[] value) {
		if (value == null)
			value = new Replacement[0];
		_Replacement.clear();
		for (int i = 0; i < value.length; ++i) {
			_Replacement.add(value[i]);
		}
	}

	public void setReplacement(int index, org.neurpheus.nlp.morphology.builder.xml.Replacement value) {
		_Replacement.set(index, value);
	}

	public org.neurpheus.nlp.morphology.builder.xml.Replacement[] getReplacement() {
		Replacement[] arr = new Replacement[_Replacement.size()];
		return (Replacement[]) _Replacement.toArray(arr);
	}

	public java.util.List fetchReplacementList() {
		return _Replacement;
	}

	public org.neurpheus.nlp.morphology.builder.xml.Replacement getReplacement(int index) {
		return (Replacement)_Replacement.get(index);
	}

	// Return the number of replacement
	public int sizeReplacement() {
		return _Replacement.size();
	}

	public int addReplacement(org.neurpheus.nlp.morphology.builder.xml.Replacement value) {
		_Replacement.add(value);
		int positionOfNewItem = _Replacement.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeReplacement(org.neurpheus.nlp.morphology.builder.xml.Replacement value) {
		int pos = _Replacement.indexOf(value);
		if (pos >= 0) {
			_Replacement.remove(pos);
		}
		return pos;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _Replacement.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.builder.xml.Replacement element = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it.next();
			if (element != null) {
				element.writeNode(out, "replacement", nextIndent);
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
			if (childNodeName == "replacement") {
				Replacement aReplacement = new org.neurpheus.nlp.morphology.builder.xml.Replacement();
				aReplacement.readNode(childNode);
				_Replacement.add(aReplacement);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException {
		boolean restrictionFailure = false;
		// Validating property replacement
		for (int _index = 0; _index < sizeReplacement(); ++_index) {
			org.neurpheus.nlp.morphology.builder.xml.Replacement element = getReplacement(_index);
			if (element != null) {
				element.validate();
			}
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "replacement")
			addReplacement((Replacement)value);
		else if (name == "replacement[]")
			setReplacement((Replacement[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for TagModification");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "replacement[]")
			return getReplacement();
		throw new IllegalArgumentException(name+" is not a valid property name for TagModification");
	}

	public String nameSelf() {
		return "tag-modification";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof Replacement) {
			Replacement child = (Replacement) childObj;
			int index = 0;
			for (java.util.Iterator it = _Replacement.iterator(); 
				it.hasNext(); ) {
				org.neurpheus.nlp.morphology.builder.xml.Replacement element = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it.next();
				if (child == element) {
					return "replacement."+index;
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
		for (java.util.Iterator it = _Replacement.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.builder.xml.Replacement element = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it.next();
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
		if (!(o instanceof org.neurpheus.nlp.morphology.builder.xml.TagModification))
			return false;
		org.neurpheus.nlp.morphology.builder.xml.TagModification inst = (org.neurpheus.nlp.morphology.builder.xml.TagModification) o;
		if (sizeReplacement() != inst.sizeReplacement())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _Replacement.iterator(), it2 = inst._Replacement.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			org.neurpheus.nlp.morphology.builder.xml.Replacement element = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it.next();
			org.neurpheus.nlp.morphology.builder.xml.Replacement element2 = (org.neurpheus.nlp.morphology.builder.xml.Replacement)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_Replacement == null ? 0 : _Replacement.hashCode());
		return result;
	}

}


/*
		The following schema file has been used for generation:

<?xml version='1.0' encoding='UTF-8'?>
<!--- 
	Information about tagging of inflection pattern using a file 
	containing example tagged forms. 
-->
<!ELEMENT tagging-info (encoding, tag-modification?)>

<!--- Encoding of a file with examples. -->
<!ELEMENT encoding (#PCDATA)>

<!--- Defines modification of tags. -->
<!ELEMENT tag-modification (replacement)*>

<!--- Defines a replacement of one tag with another. -->
<!ELEMENT replacement (source-tag, target-tag)>

<!--- Tag to replace. -->
<!ELEMENT source-tag (#PCDATA)>

<!--- Replacement tag. -->
<!ELEMENT target-tag (#PCDATA)>

*/

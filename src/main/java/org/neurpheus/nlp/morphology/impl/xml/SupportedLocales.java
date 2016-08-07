/**
 *	This generated bean class SupportedLocales
 *	matches the schema element 'supportedLocales'.
 *  The root bean class is MorphologicalAnalysers
 *
 *	Generated on Sun Aug 07 18:18:14 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.impl.xml;

public class SupportedLocales {
	private java.util.List _Supportedlocale = new java.util.ArrayList();	// List<String>

	public SupportedLocales() {
	}

	/**
	 * Required parameters constructor
	 */
	public SupportedLocales(String[] supportedlocale) {
		if (supportedlocale!= null) {
			for (int i = 0; i < supportedlocale.length; ++i) {
				_Supportedlocale.add(supportedlocale[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public SupportedLocales(org.neurpheus.nlp.morphology.impl.xml.SupportedLocales source) {
		for (java.util.Iterator it = source._Supportedlocale.iterator(); 
			it.hasNext(); ) {
			String element = (String)it.next();
			_Supportedlocale.add(element);
		}
	}

	// This attribute is an array containing at least one element
	public void setSupportedlocale(String[] value) {
		if (value == null)
			value = new String[0];
		_Supportedlocale.clear();
		for (int i = 0; i < value.length; ++i) {
			_Supportedlocale.add(value[i]);
		}
	}

	public void setSupportedlocale(int index, String value) {
		_Supportedlocale.set(index, value);
	}

	public String[] getSupportedlocale() {
		String[] arr = new String[_Supportedlocale.size()];
		return (String[]) _Supportedlocale.toArray(arr);
	}

	public java.util.List fetchSupportedlocaleList() {
		return _Supportedlocale;
	}

	public String getSupportedlocale(int index) {
		return (String)_Supportedlocale.get(index);
	}

	// Return the number of supportedlocale
	public int sizeSupportedlocale() {
		return _Supportedlocale.size();
	}

	public int addSupportedlocale(String value) {
		_Supportedlocale.add(value);
		int positionOfNewItem = _Supportedlocale.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeSupportedlocale(String value) {
		int pos = _Supportedlocale.indexOf(value);
		if (pos >= 0) {
			_Supportedlocale.remove(pos);
		}
		return pos;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _Supportedlocale.iterator(); 
			it.hasNext(); ) {
			String element = (String)it.next();
			if (element != null) {
				out.write(nextIndent);
				out.write("<supportedlocale");	// NOI18N
				out.write(">");	// NOI18N
				org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, element, false);
				out.write("</supportedlocale>\n");	// NOI18N
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
			if (childNodeName == "supportedlocale") {
				String aSupportedlocale;
				aSupportedlocale = childNodeValue;
				_Supportedlocale.add(aSupportedlocale);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException {
		boolean restrictionFailure = false;
		// Validating property supportedlocale
		if (sizeSupportedlocale() == 0) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("sizeSupportedlocale() == 0", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "supportedlocale", this);	// NOI18N
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "supportedlocale")
			addSupportedlocale((String)value);
		else if (name == "supportedlocale[]")
			setSupportedlocale((String[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for SupportedLocales");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "supportedlocale[]")
			return getSupportedlocale();
		throw new IllegalArgumentException(name+" is not a valid property name for SupportedLocales");
	}

	public String nameSelf() {
		return "supportedLocales";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof String) {
			String child = (String) childObj;
			int index = 0;
			for (java.util.Iterator it = _Supportedlocale.iterator(); 
				it.hasNext(); ) {
				String element = (String)it.next();
				if (child == element) {
					return "supportedlocale."+index;
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
		if (!(o instanceof org.neurpheus.nlp.morphology.impl.xml.SupportedLocales))
			return false;
		org.neurpheus.nlp.morphology.impl.xml.SupportedLocales inst = (org.neurpheus.nlp.morphology.impl.xml.SupportedLocales) o;
		if (sizeSupportedlocale() != inst.sizeSupportedlocale())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _Supportedlocale.iterator(), it2 = inst._Supportedlocale.iterator(); 
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
		result = 37*result + (_Supportedlocale == null ? 0 : _Supportedlocale.hashCode());
		return result;
	}

}


/*
		The following schema file has been used for generation:

<?xml version='1.0' encoding='UTF-8'?>

<!--- Put your DTDDoc comment here. -->
<!ELEMENT morphological-analysers (morphological-analyser-info)+>

<!--- Put your DTDDoc comment here. -->
<!ELEMENT morphological-analyser-info (name, supportedLocales, dataPath, tagged, quality, speed, version, vendor, authors, buildData, licence, webPage, description, copyright)>

<!ELEMENT supportedLocales (supportedlocale+)>

<!ELEMENT name (#PCDATA)>
<!ELEMENT dataPath (#PCDATA)>
<!ELEMENT tagged (#PCDATA)>
<!ELEMENT quality (#PCDATA)>
<!ELEMENT speed (#PCDATA)>
<!ELEMENT version (#PCDATA)>
<!ELEMENT vendor (#PCDATA)> 
<!ELEMENT authors (#PCDATA)>
<!ELEMENT buildData (#PCDATA)>
<!ELEMENT licence (#PCDATA)>
<!ELEMENT webPage (#PCDATA)>
<!ELEMENT description (#PCDATA)>
<!ELEMENT copyright (#PCDATA)>
<!ELEMENT supportedlocale (#PCDATA)>



*/

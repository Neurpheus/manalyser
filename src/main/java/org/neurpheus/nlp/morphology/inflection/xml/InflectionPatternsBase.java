/**
 *	This generated bean class InflectionPatternsBase
 *	matches the schema element 'inflection-patterns-base'.
 *
 *	Generated on Sun Aug 07 18:18:29 CEST 2016
 *
 *	This class matches the root element of the DTD,
 *	and is the root of the bean graph.
 *
 * 	inflection-patterns-base : InflectionPatternsBase
 * 		(
 * 		  inflection-pattern-definition : InflectionPatternDefinition
 * 		  	pattern-code : String
 * 		  	pattern : Pattern
 * 		  		(
 * 		  		  form-pattern : FormPattern
 * 		  		  	affixes : String
 * 		  		  	grammatical-properties-list : GrammaticalPropertiesList
 * 		  		  		(
 * 		  		  		  grammatical-properties-mark : String
 * 		  		  		)[1,n]
 * 		  		)[1,n]
 * 		  	cores : String
 * 		  	covered-patterns : CoveredPatterns
 * 		  		(
 * 		  		  covered-pattern : CoveredPattern
 * 		  		  	pattern-code : String
 * 		  		)[1,n]
 * 		)[1,n]
 *
 * @generated
 */

package org.neurpheus.nlp.morphology.inflection.xml;

public class InflectionPatternsBase {
	private java.util.List _InflectionPatternDefinition = new java.util.ArrayList();	// List<InflectionPatternDefinition>
	private java.lang.String schemaLocation;

	public InflectionPatternsBase() {
	}

	/**
	 * Required parameters constructor
	 */
	public InflectionPatternsBase(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition[] inflectionPatternDefinition) {
		if (inflectionPatternDefinition!= null) {
			for (int i = 0; i < inflectionPatternDefinition.length; ++i) {
				_InflectionPatternDefinition.add(inflectionPatternDefinition[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public InflectionPatternsBase(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase source) {
		for (java.util.Iterator it = source._InflectionPatternDefinition.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it.next();
			_InflectionPatternDefinition.add((element == null) ? null : new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition(element));
		}
		schemaLocation = source.schemaLocation;
	}

	// This attribute is an array containing at least one element
	public void setInflectionPatternDefinition(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition[] value) {
		if (value == null)
			value = new InflectionPatternDefinition[0];
		_InflectionPatternDefinition.clear();
		for (int i = 0; i < value.length; ++i) {
			_InflectionPatternDefinition.add(value[i]);
		}
	}

	public void setInflectionPatternDefinition(int index, org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition value) {
		_InflectionPatternDefinition.set(index, value);
	}

	public org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition[] getInflectionPatternDefinition() {
		InflectionPatternDefinition[] arr = new InflectionPatternDefinition[_InflectionPatternDefinition.size()];
		return (InflectionPatternDefinition[]) _InflectionPatternDefinition.toArray(arr);
	}

	public java.util.List fetchInflectionPatternDefinitionList() {
		return _InflectionPatternDefinition;
	}

	public org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition getInflectionPatternDefinition(int index) {
		return (InflectionPatternDefinition)_InflectionPatternDefinition.get(index);
	}

	// Return the number of inflectionPatternDefinition
	public int sizeInflectionPatternDefinition() {
		return _InflectionPatternDefinition.size();
	}

	public int addInflectionPatternDefinition(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition value) {
		_InflectionPatternDefinition.add(value);
		int positionOfNewItem = _InflectionPatternDefinition.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeInflectionPatternDefinition(org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition value) {
		int pos = _InflectionPatternDefinition.indexOf(value);
		if (pos >= 0) {
			_InflectionPatternDefinition.remove(pos);
		}
		return pos;
	}

	public void _setSchemaLocation(String location) {
		schemaLocation = location;
	}

	public String _getSchemaLocation() {
		return schemaLocation;
	}

	public void write(java.io.OutputStream out) throws java.io.IOException {
		write(out, null);
	}

	public void write(java.io.OutputStream out, String encoding) throws java.io.IOException {
		java.io.Writer w;
		if (encoding == null) {
			encoding = "UTF-8";	// NOI18N
		}
		w = new java.io.BufferedWriter(new java.io.OutputStreamWriter(out, encoding));
		write(w, encoding);
		w.flush();
	}

	/**
	 * Print this Java Bean to @param out including an XML header.
	 * @param encoding is the encoding style that @param out was opened with.
	 */
	public void write(java.io.Writer out, String encoding) throws java.io.IOException {
		out.write("<?xml version='1.0'");	// NOI18N
		if (encoding != null)
			out.write(" encoding='"+encoding+"'");	// NOI18N
		out.write(" ?>\n");	// NOI18N
		writeNode(out, "inflection-patterns-base", "");	// NOI18N
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		if (schemaLocation != null) {
			out.write(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='");
			out.write(schemaLocation);
			out.write("'");	// NOI18N
		}
		out.write(">\n");
		String nextIndent = indent + "	";
		for (java.util.Iterator it = _InflectionPatternDefinition.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it.next();
			if (element != null) {
				element.writeNode(out, "inflection-pattern-definition", nextIndent);
			}
		}
		out.write(indent);
		out.write("</"+nodeName+">\n");
	}

	public static InflectionPatternsBase read(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false, null, null);
	}

	/**
	 * Warning: in readNoEntityResolver character and entity references will
	 * not be read from any DTD in the XML source.
	 * However, this way is faster since no DTDs are looked up
	 * (possibly skipping network access) or parsed.
	 */
	public static InflectionPatternsBase readNoEntityResolver(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false,
			new org.xml.sax.EntityResolver() {
			public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
				java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(new byte[0]);
				return new org.xml.sax.InputSource(bin);
			}
		}
			, null);
	}

	public static InflectionPatternsBase read(org.xml.sax.InputSource in, boolean validate, org.xml.sax.EntityResolver er, org.xml.sax.ErrorHandler eh) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setValidating(validate);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		if (er != null)	db.setEntityResolver(er);
		if (eh != null)	db.setErrorHandler(eh);
		org.w3c.dom.Document doc = db.parse(in);
		return read(doc);
	}

	public static InflectionPatternsBase read(org.w3c.dom.Document document) {
		InflectionPatternsBase aInflectionPatternsBase = new InflectionPatternsBase();
		aInflectionPatternsBase.readNode(document.getDocumentElement());
		return aInflectionPatternsBase;
	}

	public void readNode(org.w3c.dom.Node node) {
		if (node.hasAttributes()) {
			org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
			org.w3c.dom.Attr attr;
			java.lang.String attrValue;
			attr = (org.w3c.dom.Attr) attrs.getNamedItem("xsi:schemaLocation");
			if (attr != null) {
				attrValue = attr.getValue();
			} else {
				attrValue = null;
			}
			schemaLocation = attrValue;
		}
		org.w3c.dom.NodeList children = node.getChildNodes();
		for (int i = 0, size = children.getLength(); i < size; ++i) {
			org.w3c.dom.Node childNode = children.item(i);
			String childNodeName = (childNode.getLocalName() == null ? childNode.getNodeName().intern() : childNode.getLocalName().intern());
			String childNodeValue = "";
			if (childNode.getFirstChild() != null) {
				childNodeValue = childNode.getFirstChild().getNodeValue();
			}
			if (childNodeName == "inflection-pattern-definition") {
				InflectionPatternDefinition aInflectionPatternDefinition = new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition();
				aInflectionPatternDefinition.readNode(childNode);
				_InflectionPatternDefinition.add(aInflectionPatternDefinition);
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	/**
	 * Takes some text to be printed into an XML stream and escapes any
	 * characters that might make it invalid XML (like '<').
	 */
	public static void writeXML(java.io.Writer out, String msg) throws java.io.IOException {
		writeXML(out, msg, true);
	}

	public static void writeXML(java.io.Writer out, String msg, boolean attribute) throws java.io.IOException {
		if (msg == null)
			return;
		int msgLength = msg.length();
		for (int i = 0; i < msgLength; ++i) {
			char c = msg.charAt(i);
			writeXML(out, c, attribute);
		}
	}

	public static void writeXML(java.io.Writer out, char msg, boolean attribute) throws java.io.IOException {
		if (msg == '&')
			out.write("&amp;");
		else if (msg == '<')
			out.write("&lt;");
		else if (msg == '>')
			out.write("&gt;");
		else if (attribute && msg == '"')
			out.write("&quot;");
		else if (attribute && msg == '\'')
			out.write("&apos;");
		else if (attribute && msg == '\n')
			out.write("&#xA;");
		else if (attribute && msg == '\t')
			out.write("&#x9;");
		else
			out.write(msg);
	}

	public static class ValidateException extends Exception {
		private java.lang.Object failedBean;
		private String failedPropertyName;
		private FailureType failureType;
		public ValidateException(String msg, String failedPropertyName, java.lang.Object failedBean) {
			super(msg);
			this.failedBean = failedBean;
			this.failedPropertyName = failedPropertyName;
		}
		public ValidateException(String msg, FailureType ft, String failedPropertyName, java.lang.Object failedBean) {
			super(msg);
			this.failureType = ft;
			this.failedBean = failedBean;
			this.failedPropertyName = failedPropertyName;
		}
		public String getFailedPropertyName() {return failedPropertyName;}
		public FailureType getFailureType() {return failureType;}
		public java.lang.Object getFailedBean() {return failedBean;}
		public static class FailureType {
			private final String name;
			private FailureType(String name) {this.name = name;}
			public String toString() { return name;}
			public static final FailureType NULL_VALUE = new FailureType("NULL_VALUE");
			public static final FailureType DATA_RESTRICTION = new FailureType("DATA_RESTRICTION");
			public static final FailureType ENUM_RESTRICTION = new FailureType("ENUM_RESTRICTION");
			public static final FailureType MUTUALLY_EXCLUSIVE = new FailureType("MUTUALLY_EXCLUSIVE");
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException {
		boolean restrictionFailure = false;
		// Validating property inflectionPatternDefinition
		if (sizeInflectionPatternDefinition() == 0) {
			throw new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException("sizeInflectionPatternDefinition() == 0", org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.ValidateException.FailureType.NULL_VALUE, "inflectionPatternDefinition", this);	// NOI18N
		}
		for (int _index = 0; _index < sizeInflectionPatternDefinition(); 
			++_index) {
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = getInflectionPatternDefinition(_index);
			if (element != null) {
				element.validate();
			}
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "inflectionPatternDefinition")
			addInflectionPatternDefinition((InflectionPatternDefinition)value);
		else if (name == "inflectionPatternDefinition[]")
			setInflectionPatternDefinition((InflectionPatternDefinition[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for InflectionPatternsBase");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "inflectionPatternDefinition[]")
			return getInflectionPatternDefinition();
		throw new IllegalArgumentException(name+" is not a valid property name for InflectionPatternsBase");
	}

	public String nameSelf() {
		return "/inflection-patterns-base";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof InflectionPatternDefinition) {
			InflectionPatternDefinition child = (InflectionPatternDefinition) childObj;
			int index = 0;
			for (java.util.Iterator it = _InflectionPatternDefinition.iterator(); 
				it.hasNext(); ) {
				org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it.next();
				if (child == element) {
					return "inflectionPatternDefinition."+index;
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
		for (java.util.Iterator it = _InflectionPatternDefinition.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it.next();
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
		if (!(o instanceof org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase))
			return false;
		org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase inst = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase) o;
		if (sizeInflectionPatternDefinition() != inst.sizeInflectionPatternDefinition())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _InflectionPatternDefinition.iterator(), it2 = inst._InflectionPatternDefinition.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it.next();
			org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition element2 = (org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_InflectionPatternDefinition == null ? 0 : _InflectionPatternDefinition.hashCode());
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

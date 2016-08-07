/**
 *	This generated bean class MorphologicalAnalysers
 *	matches the schema element 'morphological-analysers'.
 *
 *	Generated on Sun Aug 07 18:18:14 CEST 2016
 *
 *	This class matches the root element of the DTD,
 *	and is the root of the bean graph.
 *
 * 	morphological-analysers : MorphologicalAnalysers
 * 		(
 * 		  morphological-analyser-info : MorphologicalAnalyserInfo
 * 		  	name : String
 * 		  	supportedLocales : SupportedLocales
 * 		  		supportedlocale : String[1,n]
 * 		  	dataPath : String
 * 		  	tagged : String
 * 		  	quality : String
 * 		  	speed : String
 * 		  	version : String
 * 		  	vendor : String
 * 		  	authors : String
 * 		  	buildData : String
 * 		  	licence : String
 * 		  	webPage : String
 * 		  	description : String
 * 		  	copyright : String
 * 		)[1,n]
 *
 * @generated
 */

package org.neurpheus.nlp.morphology.impl.xml;

public class MorphologicalAnalysers {
	private java.util.List _MorphologicalAnalyserInfo = new java.util.ArrayList();	// List<MorphologicalAnalyserInfo>
	private java.lang.String schemaLocation;

	public MorphologicalAnalysers() {
	}

	/**
	 * Required parameters constructor
	 */
	public MorphologicalAnalysers(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo[] morphologicalAnalyserInfo) {
		if (morphologicalAnalyserInfo!= null) {
			for (int i = 0; i < morphologicalAnalyserInfo.length; ++i) {
				_MorphologicalAnalyserInfo.add(morphologicalAnalyserInfo[i]);
			}
		}
	}

	/**
	 * Deep copy
	 */
	public MorphologicalAnalysers(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers source) {
		for (java.util.Iterator it = source._MorphologicalAnalyserInfo.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it.next();
			_MorphologicalAnalyserInfo.add((element == null) ? null : new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo(element));
		}
		schemaLocation = source.schemaLocation;
	}

	// This attribute is an array containing at least one element
	public void setMorphologicalAnalyserInfo(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo[] value) {
		if (value == null)
			value = new MorphologicalAnalyserInfo[0];
		_MorphologicalAnalyserInfo.clear();
		for (int i = 0; i < value.length; ++i) {
			_MorphologicalAnalyserInfo.add(value[i]);
		}
	}

	public void setMorphologicalAnalyserInfo(int index, org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo value) {
		_MorphologicalAnalyserInfo.set(index, value);
	}

	public org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo[] getMorphologicalAnalyserInfo() {
		MorphologicalAnalyserInfo[] arr = new MorphologicalAnalyserInfo[_MorphologicalAnalyserInfo.size()];
		return (MorphologicalAnalyserInfo[]) _MorphologicalAnalyserInfo.toArray(arr);
	}

	public java.util.List fetchMorphologicalAnalyserInfoList() {
		return _MorphologicalAnalyserInfo;
	}

	public org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo getMorphologicalAnalyserInfo(int index) {
		return (MorphologicalAnalyserInfo)_MorphologicalAnalyserInfo.get(index);
	}

	// Return the number of morphologicalAnalyserInfo
	public int sizeMorphologicalAnalyserInfo() {
		return _MorphologicalAnalyserInfo.size();
	}

	public int addMorphologicalAnalyserInfo(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo value) {
		_MorphologicalAnalyserInfo.add(value);
		int positionOfNewItem = _MorphologicalAnalyserInfo.size()-1;
		return positionOfNewItem;
	}

	/**
	 * Search from the end looking for @param value, and then remove it.
	 */
	public int removeMorphologicalAnalyserInfo(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo value) {
		int pos = _MorphologicalAnalyserInfo.indexOf(value);
		if (pos >= 0) {
			_MorphologicalAnalyserInfo.remove(pos);
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
		writeNode(out, "morphological-analysers", "");	// NOI18N
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
		for (java.util.Iterator it = _MorphologicalAnalyserInfo.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it.next();
			if (element != null) {
				element.writeNode(out, "morphological-analyser-info", nextIndent);
			}
		}
		out.write(indent);
		out.write("</"+nodeName+">\n");
	}

	public static MorphologicalAnalysers read(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false, null, null);
	}

	/**
	 * Warning: in readNoEntityResolver character and entity references will
	 * not be read from any DTD in the XML source.
	 * However, this way is faster since no DTDs are looked up
	 * (possibly skipping network access) or parsed.
	 */
	public static MorphologicalAnalysers readNoEntityResolver(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false,
			new org.xml.sax.EntityResolver() {
			public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
				java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(new byte[0]);
				return new org.xml.sax.InputSource(bin);
			}
		}
			, null);
	}

	public static MorphologicalAnalysers read(org.xml.sax.InputSource in, boolean validate, org.xml.sax.EntityResolver er, org.xml.sax.ErrorHandler eh) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setValidating(validate);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		if (er != null)	db.setEntityResolver(er);
		if (eh != null)	db.setErrorHandler(eh);
		org.w3c.dom.Document doc = db.parse(in);
		return read(doc);
	}

	public static MorphologicalAnalysers read(org.w3c.dom.Document document) {
		MorphologicalAnalysers aMorphologicalAnalysers = new MorphologicalAnalysers();
		aMorphologicalAnalysers.readNode(document.getDocumentElement());
		return aMorphologicalAnalysers;
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
			if (childNodeName == "morphological-analyser-info") {
				MorphologicalAnalyserInfo aMorphologicalAnalyserInfo = new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo();
				aMorphologicalAnalyserInfo.readNode(childNode);
				_MorphologicalAnalyserInfo.add(aMorphologicalAnalyserInfo);
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

	public void validate() throws org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException {
		boolean restrictionFailure = false;
		// Validating property morphologicalAnalyserInfo
		if (sizeMorphologicalAnalyserInfo() == 0) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("sizeMorphologicalAnalyserInfo() == 0", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "morphologicalAnalyserInfo", this);	// NOI18N
		}
		for (int _index = 0; _index < sizeMorphologicalAnalyserInfo(); 
			++_index) {
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = getMorphologicalAnalyserInfo(_index);
			if (element != null) {
				element.validate();
			}
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "morphologicalAnalyserInfo")
			addMorphologicalAnalyserInfo((MorphologicalAnalyserInfo)value);
		else if (name == "morphologicalAnalyserInfo[]")
			setMorphologicalAnalyserInfo((MorphologicalAnalyserInfo[]) value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for MorphologicalAnalysers");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "morphologicalAnalyserInfo[]")
			return getMorphologicalAnalyserInfo();
		throw new IllegalArgumentException(name+" is not a valid property name for MorphologicalAnalysers");
	}

	public String nameSelf() {
		return "/morphological-analysers";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof MorphologicalAnalyserInfo) {
			MorphologicalAnalyserInfo child = (MorphologicalAnalyserInfo) childObj;
			int index = 0;
			for (java.util.Iterator it = _MorphologicalAnalyserInfo.iterator(); 
				it.hasNext(); ) {
				org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it.next();
				if (child == element) {
					return "morphologicalAnalyserInfo."+index;
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
		for (java.util.Iterator it = _MorphologicalAnalyserInfo.iterator(); 
			it.hasNext(); ) {
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it.next();
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
		if (!(o instanceof org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers))
			return false;
		org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers inst = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers) o;
		if (sizeMorphologicalAnalyserInfo() != inst.sizeMorphologicalAnalyserInfo())
			return false;
		// Compare every element.
		for (java.util.Iterator it = _MorphologicalAnalyserInfo.iterator(), it2 = inst._MorphologicalAnalyserInfo.iterator(); 
			it.hasNext() && it2.hasNext(); ) {
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it.next();
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo element2 = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo)it2.next();
			if (!(element == null ? element2 == null : element.equals(element2))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_MorphologicalAnalyserInfo == null ? 0 : _MorphologicalAnalyserInfo.hashCode());
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

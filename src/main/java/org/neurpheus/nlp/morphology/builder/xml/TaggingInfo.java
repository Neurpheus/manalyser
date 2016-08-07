/**
 *	This generated bean class TaggingInfo
 *	matches the schema element 'tagging-info'.
 *
 *	Generated on Sun Aug 07 18:18:26 CEST 2016
 *
 *	This class matches the root element of the DTD,
 *	and is the root of the bean graph.
 *
 * 	tagging-info : TaggingInfo
 * 		encoding : String
 * 		tag-modification : TagModification[0,1]
 * 			(
 * 			  replacement : Replacement
 * 			  	source-tag : String
 * 			  	target-tag : String
 * 			)[0,n]
 *
 * @generated
 */

package org.neurpheus.nlp.morphology.builder.xml;

public class TaggingInfo {
	private String _Encoding;
	private TagModification _TagModification;
	private java.lang.String schemaLocation;

	public TaggingInfo() {
		_Encoding = "";
	}

	/**
	 * Required parameters constructor
	 */
	public TaggingInfo(String encoding) {
		_Encoding = encoding;
	}

	/**
	 * Deep copy
	 */
	public TaggingInfo(org.neurpheus.nlp.morphology.builder.xml.TaggingInfo source) {
		_Encoding = source._Encoding;
		_TagModification = (source._TagModification == null) ? null : new org.neurpheus.nlp.morphology.builder.xml.TagModification(source._TagModification);
		schemaLocation = source.schemaLocation;
	}

	// This attribute is mandatory
	public void setEncoding(String value) {
		_Encoding = value;
	}

	public String getEncoding() {
		return _Encoding;
	}

	// This attribute is optional
	public void setTagModification(org.neurpheus.nlp.morphology.builder.xml.TagModification value) {
		_TagModification = value;
	}

	public org.neurpheus.nlp.morphology.builder.xml.TagModification getTagModification() {
		return _TagModification;
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
		writeNode(out, "tagging-info", "");	// NOI18N
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
		if (_Encoding != null) {
			out.write(nextIndent);
			out.write("<encoding");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.writeXML(out, _Encoding, false);
			out.write("</encoding>\n");	// NOI18N
		}
		if (_TagModification != null) {
			_TagModification.writeNode(out, "tag-modification", nextIndent);
		}
		out.write(indent);
		out.write("</"+nodeName+">\n");
	}

	public static TaggingInfo read(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false, null, null);
	}

	/**
	 * Warning: in readNoEntityResolver character and entity references will
	 * not be read from any DTD in the XML source.
	 * However, this way is faster since no DTDs are looked up
	 * (possibly skipping network access) or parsed.
	 */
	public static TaggingInfo readNoEntityResolver(java.io.InputStream in) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		return read(new org.xml.sax.InputSource(in), false,
			new org.xml.sax.EntityResolver() {
			public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
				java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(new byte[0]);
				return new org.xml.sax.InputSource(bin);
			}
		}
			, null);
	}

	public static TaggingInfo read(org.xml.sax.InputSource in, boolean validate, org.xml.sax.EntityResolver er, org.xml.sax.ErrorHandler eh) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setValidating(validate);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		if (er != null)	db.setEntityResolver(er);
		if (eh != null)	db.setErrorHandler(eh);
		org.w3c.dom.Document doc = db.parse(in);
		return read(doc);
	}

	public static TaggingInfo read(org.w3c.dom.Document document) {
		TaggingInfo aTaggingInfo = new TaggingInfo();
		aTaggingInfo.readNode(document.getDocumentElement());
		return aTaggingInfo;
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
			if (childNodeName == "encoding") {
				_Encoding = childNodeValue;
			}
			else if (childNodeName == "tag-modification") {
				_TagModification = new org.neurpheus.nlp.morphology.builder.xml.TagModification();
				_TagModification.readNode(childNode);
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

	public void validate() throws org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException {
		boolean restrictionFailure = false;
		// Validating property encoding
		if (getEncoding() == null) {
			throw new org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException("getEncoding() == null", org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException.FailureType.NULL_VALUE, "encoding", this);	// NOI18N
		}
		// Validating property tagModification
		if (getTagModification() != null) {
			getTagModification().validate();
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "encoding")
			setEncoding((String)value);
		else if (name == "tagModification")
			setTagModification((TagModification)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for TaggingInfo");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "encoding")
			return getEncoding();
		if (name == "tagModification")
			return getTagModification();
		throw new IllegalArgumentException(name+" is not a valid property name for TaggingInfo");
	}

	public String nameSelf() {
		return "/tagging-info";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _Encoding) {
				return "encoding";
			}
		}
		if (childObj instanceof TagModification) {
			TagModification child = (TagModification) childObj;
			if (child == _TagModification) {
				return "tagModification";
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
		if (_TagModification != null) {
			if (recursive) {
				_TagModification.childBeans(true, beans);
			}
			beans.add(_TagModification);
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.builder.xml.TaggingInfo))
			return false;
		org.neurpheus.nlp.morphology.builder.xml.TaggingInfo inst = (org.neurpheus.nlp.morphology.builder.xml.TaggingInfo) o;
		if (!(_Encoding == null ? inst._Encoding == null : _Encoding.equals(inst._Encoding))) {
			return false;
		}
		if (!(_TagModification == null ? inst._TagModification == null : _TagModification.equals(inst._TagModification))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_Encoding == null ? 0 : _Encoding.hashCode());
		result = 37*result + (_TagModification == null ? 0 : _TagModification.hashCode());
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

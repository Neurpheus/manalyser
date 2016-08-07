/**
 *	This generated bean class Replacement
 *	matches the schema element 'replacement'.
 *  The root bean class is TaggingInfo
 *
 *	Generated on Sun Aug 07 18:18:26 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.builder.xml;

public class Replacement {
	private String _SourceTag;
	private String _TargetTag;

	public Replacement() {
		_SourceTag = "";
		_TargetTag = "";
	}

	/**
	 * Required parameters constructor
	 */
	public Replacement(String sourceTag, String targetTag) {
		_SourceTag = sourceTag;
		_TargetTag = targetTag;
	}

	/**
	 * Deep copy
	 */
	public Replacement(org.neurpheus.nlp.morphology.builder.xml.Replacement source) {
		_SourceTag = source._SourceTag;
		_TargetTag = source._TargetTag;
	}

	// This attribute is mandatory
	public void setSourceTag(String value) {
		_SourceTag = value;
	}

	public String getSourceTag() {
		return _SourceTag;
	}

	// This attribute is mandatory
	public void setTargetTag(String value) {
		_TargetTag = value;
	}

	public String getTargetTag() {
		return _TargetTag;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		if (_SourceTag != null) {
			out.write(nextIndent);
			out.write("<source-tag");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.writeXML(out, _SourceTag, false);
			out.write("</source-tag>\n");	// NOI18N
		}
		if (_TargetTag != null) {
			out.write(nextIndent);
			out.write("<target-tag");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.writeXML(out, _TargetTag, false);
			out.write("</target-tag>\n");	// NOI18N
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
			if (childNodeName == "source-tag") {
				_SourceTag = childNodeValue;
			}
			else if (childNodeName == "target-tag") {
				_TargetTag = childNodeValue;
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException {
		boolean restrictionFailure = false;
		// Validating property sourceTag
		if (getSourceTag() == null) {
			throw new org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException("getSourceTag() == null", org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException.FailureType.NULL_VALUE, "sourceTag", this);	// NOI18N
		}
		// Validating property targetTag
		if (getTargetTag() == null) {
			throw new org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException("getTargetTag() == null", org.neurpheus.nlp.morphology.builder.xml.TaggingInfo.ValidateException.FailureType.NULL_VALUE, "targetTag", this);	// NOI18N
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "sourceTag")
			setSourceTag((String)value);
		else if (name == "targetTag")
			setTargetTag((String)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for Replacement");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "sourceTag")
			return getSourceTag();
		if (name == "targetTag")
			return getTargetTag();
		throw new IllegalArgumentException(name+" is not a valid property name for Replacement");
	}

	public String nameSelf() {
		return "replacement";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _SourceTag) {
				return "sourceTag";
			}
			if (child == _TargetTag) {
				return "targetTag";
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
		if (!(o instanceof org.neurpheus.nlp.morphology.builder.xml.Replacement))
			return false;
		org.neurpheus.nlp.morphology.builder.xml.Replacement inst = (org.neurpheus.nlp.morphology.builder.xml.Replacement) o;
		if (!(_SourceTag == null ? inst._SourceTag == null : _SourceTag.equals(inst._SourceTag))) {
			return false;
		}
		if (!(_TargetTag == null ? inst._TargetTag == null : _TargetTag.equals(inst._TargetTag))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_SourceTag == null ? 0 : _SourceTag.hashCode());
		result = 37*result + (_TargetTag == null ? 0 : _TargetTag.hashCode());
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

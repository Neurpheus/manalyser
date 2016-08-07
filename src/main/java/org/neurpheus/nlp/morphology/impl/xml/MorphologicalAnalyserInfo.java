/**
 *	This generated bean class MorphologicalAnalyserInfo
 *	matches the schema element 'morphological-analyser-info'.
 *  The root bean class is MorphologicalAnalysers
 *
 *	Generated on Sun Aug 07 18:18:14 CEST 2016
 * @generated
 */

package org.neurpheus.nlp.morphology.impl.xml;

public class MorphologicalAnalyserInfo {
	private String _Name;
	private SupportedLocales _SupportedLocales;
	private String _DataPath;
	private String _Tagged;
	private String _Quality;
	private String _Speed;
	private String _Version;
	private String _Vendor;
	private String _Authors;
	private String _BuildData;
	private String _Licence;
	private String _WebPage;
	private String _Description;
	private String _Copyright;

	public MorphologicalAnalyserInfo() {
		_Name = "";
		_SupportedLocales = new SupportedLocales();
		_DataPath = "";
		_Tagged = "";
		_Quality = "";
		_Speed = "";
		_Version = "";
		_Vendor = "";
		_Authors = "";
		_BuildData = "";
		_Licence = "";
		_WebPage = "";
		_Description = "";
		_Copyright = "";
	}

	/**
	 * Required parameters constructor
	 */
	public MorphologicalAnalyserInfo(String name, org.neurpheus.nlp.morphology.impl.xml.SupportedLocales supportedLocales, String dataPath, String tagged, String quality, String speed, String version, String vendor, String authors, String buildData, String licence, String webPage, String description, String copyright) {
		_Name = name;
		_SupportedLocales = supportedLocales;
		_DataPath = dataPath;
		_Tagged = tagged;
		_Quality = quality;
		_Speed = speed;
		_Version = version;
		_Vendor = vendor;
		_Authors = authors;
		_BuildData = buildData;
		_Licence = licence;
		_WebPage = webPage;
		_Description = description;
		_Copyright = copyright;
	}

	/**
	 * Deep copy
	 */
	public MorphologicalAnalyserInfo(org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo source) {
		_Name = source._Name;
		_SupportedLocales = (source._SupportedLocales == null) ? null : new org.neurpheus.nlp.morphology.impl.xml.SupportedLocales(source._SupportedLocales);
		_DataPath = source._DataPath;
		_Tagged = source._Tagged;
		_Quality = source._Quality;
		_Speed = source._Speed;
		_Version = source._Version;
		_Vendor = source._Vendor;
		_Authors = source._Authors;
		_BuildData = source._BuildData;
		_Licence = source._Licence;
		_WebPage = source._WebPage;
		_Description = source._Description;
		_Copyright = source._Copyright;
	}

	// This attribute is mandatory
	public void setName(String value) {
		_Name = value;
	}

	public String getName() {
		return _Name;
	}

	// This attribute is mandatory
	public void setSupportedLocales(org.neurpheus.nlp.morphology.impl.xml.SupportedLocales value) {
		_SupportedLocales = value;
	}

	public org.neurpheus.nlp.morphology.impl.xml.SupportedLocales getSupportedLocales() {
		return _SupportedLocales;
	}

	// This attribute is mandatory
	public void setDataPath(String value) {
		_DataPath = value;
	}

	public String getDataPath() {
		return _DataPath;
	}

	// This attribute is mandatory
	public void setTagged(String value) {
		_Tagged = value;
	}

	public String getTagged() {
		return _Tagged;
	}

	// This attribute is mandatory
	public void setQuality(String value) {
		_Quality = value;
	}

	public String getQuality() {
		return _Quality;
	}

	// This attribute is mandatory
	public void setSpeed(String value) {
		_Speed = value;
	}

	public String getSpeed() {
		return _Speed;
	}

	// This attribute is mandatory
	public void setVersion(String value) {
		_Version = value;
	}

	public String getVersion() {
		return _Version;
	}

	// This attribute is mandatory
	public void setVendor(String value) {
		_Vendor = value;
	}

	public String getVendor() {
		return _Vendor;
	}

	// This attribute is mandatory
	public void setAuthors(String value) {
		_Authors = value;
	}

	public String getAuthors() {
		return _Authors;
	}

	// This attribute is mandatory
	public void setBuildData(String value) {
		_BuildData = value;
	}

	public String getBuildData() {
		return _BuildData;
	}

	// This attribute is mandatory
	public void setLicence(String value) {
		_Licence = value;
	}

	public String getLicence() {
		return _Licence;
	}

	// This attribute is mandatory
	public void setWebPage(String value) {
		_WebPage = value;
	}

	public String getWebPage() {
		return _WebPage;
	}

	// This attribute is mandatory
	public void setDescription(String value) {
		_Description = value;
	}

	public String getDescription() {
		return _Description;
	}

	// This attribute is mandatory
	public void setCopyright(String value) {
		_Copyright = value;
	}

	public String getCopyright() {
		return _Copyright;
	}

	public void writeNode(java.io.Writer out, String nodeName, String indent) throws java.io.IOException {
		out.write(indent);
		out.write("<");
		out.write(nodeName);
		out.write(">\n");
		String nextIndent = indent + "	";
		if (_Name != null) {
			out.write(nextIndent);
			out.write("<name");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Name, false);
			out.write("</name>\n");	// NOI18N
		}
		if (_SupportedLocales != null) {
			_SupportedLocales.writeNode(out, "supportedLocales", nextIndent);
		}
		if (_DataPath != null) {
			out.write(nextIndent);
			out.write("<dataPath");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _DataPath, false);
			out.write("</dataPath>\n");	// NOI18N
		}
		if (_Tagged != null) {
			out.write(nextIndent);
			out.write("<tagged");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Tagged, false);
			out.write("</tagged>\n");	// NOI18N
		}
		if (_Quality != null) {
			out.write(nextIndent);
			out.write("<quality");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Quality, false);
			out.write("</quality>\n");	// NOI18N
		}
		if (_Speed != null) {
			out.write(nextIndent);
			out.write("<speed");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Speed, false);
			out.write("</speed>\n");	// NOI18N
		}
		if (_Version != null) {
			out.write(nextIndent);
			out.write("<version");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Version, false);
			out.write("</version>\n");	// NOI18N
		}
		if (_Vendor != null) {
			out.write(nextIndent);
			out.write("<vendor");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Vendor, false);
			out.write("</vendor>\n");	// NOI18N
		}
		if (_Authors != null) {
			out.write(nextIndent);
			out.write("<authors");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Authors, false);
			out.write("</authors>\n");	// NOI18N
		}
		if (_BuildData != null) {
			out.write(nextIndent);
			out.write("<buildData");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _BuildData, false);
			out.write("</buildData>\n");	// NOI18N
		}
		if (_Licence != null) {
			out.write(nextIndent);
			out.write("<licence");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Licence, false);
			out.write("</licence>\n");	// NOI18N
		}
		if (_WebPage != null) {
			out.write(nextIndent);
			out.write("<webPage");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _WebPage, false);
			out.write("</webPage>\n");	// NOI18N
		}
		if (_Description != null) {
			out.write(nextIndent);
			out.write("<description");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Description, false);
			out.write("</description>\n");	// NOI18N
		}
		if (_Copyright != null) {
			out.write(nextIndent);
			out.write("<copyright");	// NOI18N
			out.write(">");	// NOI18N
			org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.writeXML(out, _Copyright, false);
			out.write("</copyright>\n");	// NOI18N
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
			if (childNodeName == "name") {
				_Name = childNodeValue;
			}
			else if (childNodeName == "supportedLocales") {
				_SupportedLocales = new org.neurpheus.nlp.morphology.impl.xml.SupportedLocales();
				_SupportedLocales.readNode(childNode);
			}
			else if (childNodeName == "dataPath") {
				_DataPath = childNodeValue;
			}
			else if (childNodeName == "tagged") {
				_Tagged = childNodeValue;
			}
			else if (childNodeName == "quality") {
				_Quality = childNodeValue;
			}
			else if (childNodeName == "speed") {
				_Speed = childNodeValue;
			}
			else if (childNodeName == "version") {
				_Version = childNodeValue;
			}
			else if (childNodeName == "vendor") {
				_Vendor = childNodeValue;
			}
			else if (childNodeName == "authors") {
				_Authors = childNodeValue;
			}
			else if (childNodeName == "buildData") {
				_BuildData = childNodeValue;
			}
			else if (childNodeName == "licence") {
				_Licence = childNodeValue;
			}
			else if (childNodeName == "webPage") {
				_WebPage = childNodeValue;
			}
			else if (childNodeName == "description") {
				_Description = childNodeValue;
			}
			else if (childNodeName == "copyright") {
				_Copyright = childNodeValue;
			}
			else {
				// Found extra unrecognized childNode
			}
		}
	}

	public void validate() throws org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException {
		boolean restrictionFailure = false;
		// Validating property name
		if (getName() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getName() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "name", this);	// NOI18N
		}
		// Validating property supportedLocales
		if (getSupportedLocales() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getSupportedLocales() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "supportedLocales", this);	// NOI18N
		}
		getSupportedLocales().validate();
		// Validating property dataPath
		if (getDataPath() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getDataPath() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "dataPath", this);	// NOI18N
		}
		// Validating property tagged
		if (getTagged() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getTagged() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "tagged", this);	// NOI18N
		}
		// Validating property quality
		if (getQuality() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getQuality() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "quality", this);	// NOI18N
		}
		// Validating property speed
		if (getSpeed() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getSpeed() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "speed", this);	// NOI18N
		}
		// Validating property version
		if (getVersion() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getVersion() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "version", this);	// NOI18N
		}
		// Validating property vendor
		if (getVendor() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getVendor() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "vendor", this);	// NOI18N
		}
		// Validating property authors
		if (getAuthors() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getAuthors() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "authors", this);	// NOI18N
		}
		// Validating property buildData
		if (getBuildData() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getBuildData() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "buildData", this);	// NOI18N
		}
		// Validating property licence
		if (getLicence() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getLicence() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "licence", this);	// NOI18N
		}
		// Validating property webPage
		if (getWebPage() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getWebPage() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "webPage", this);	// NOI18N
		}
		// Validating property description
		if (getDescription() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getDescription() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "description", this);	// NOI18N
		}
		// Validating property copyright
		if (getCopyright() == null) {
			throw new org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException("getCopyright() == null", org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers.ValidateException.FailureType.NULL_VALUE, "copyright", this);	// NOI18N
		}
	}

	public void changePropertyByName(String name, Object value) {
		if (name == null) return;
		name = name.intern();
		if (name == "name")
			setName((String)value);
		else if (name == "supportedLocales")
			setSupportedLocales((SupportedLocales)value);
		else if (name == "dataPath")
			setDataPath((String)value);
		else if (name == "tagged")
			setTagged((String)value);
		else if (name == "quality")
			setQuality((String)value);
		else if (name == "speed")
			setSpeed((String)value);
		else if (name == "version")
			setVersion((String)value);
		else if (name == "vendor")
			setVendor((String)value);
		else if (name == "authors")
			setAuthors((String)value);
		else if (name == "buildData")
			setBuildData((String)value);
		else if (name == "licence")
			setLicence((String)value);
		else if (name == "webPage")
			setWebPage((String)value);
		else if (name == "description")
			setDescription((String)value);
		else if (name == "copyright")
			setCopyright((String)value);
		else
			throw new IllegalArgumentException(name+" is not a valid property name for MorphologicalAnalyserInfo");
	}

	public Object fetchPropertyByName(String name) {
		if (name == "name")
			return getName();
		if (name == "supportedLocales")
			return getSupportedLocales();
		if (name == "dataPath")
			return getDataPath();
		if (name == "tagged")
			return getTagged();
		if (name == "quality")
			return getQuality();
		if (name == "speed")
			return getSpeed();
		if (name == "version")
			return getVersion();
		if (name == "vendor")
			return getVendor();
		if (name == "authors")
			return getAuthors();
		if (name == "buildData")
			return getBuildData();
		if (name == "licence")
			return getLicence();
		if (name == "webPage")
			return getWebPage();
		if (name == "description")
			return getDescription();
		if (name == "copyright")
			return getCopyright();
		throw new IllegalArgumentException(name+" is not a valid property name for MorphologicalAnalyserInfo");
	}

	public String nameSelf() {
		return "morphological-analyser-info";
	}

	public String nameChild(Object childObj) {
		if (childObj instanceof SupportedLocales) {
			SupportedLocales child = (SupportedLocales) childObj;
			if (child == _SupportedLocales) {
				return "supportedLocales";
			}
		}
		if (childObj instanceof String) {
			String child = (String) childObj;
			if (child == _Name) {
				return "name";
			}
			if (child == _DataPath) {
				return "dataPath";
			}
			if (child == _Tagged) {
				return "tagged";
			}
			if (child == _Quality) {
				return "quality";
			}
			if (child == _Speed) {
				return "speed";
			}
			if (child == _Version) {
				return "version";
			}
			if (child == _Vendor) {
				return "vendor";
			}
			if (child == _Authors) {
				return "authors";
			}
			if (child == _BuildData) {
				return "buildData";
			}
			if (child == _Licence) {
				return "licence";
			}
			if (child == _WebPage) {
				return "webPage";
			}
			if (child == _Description) {
				return "description";
			}
			if (child == _Copyright) {
				return "copyright";
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
		if (_SupportedLocales != null) {
			if (recursive) {
				_SupportedLocales.childBeans(true, beans);
			}
			beans.add(_SupportedLocales);
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo))
			return false;
		org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo inst = (org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo) o;
		if (!(_Name == null ? inst._Name == null : _Name.equals(inst._Name))) {
			return false;
		}
		if (!(_SupportedLocales == null ? inst._SupportedLocales == null : _SupportedLocales.equals(inst._SupportedLocales))) {
			return false;
		}
		if (!(_DataPath == null ? inst._DataPath == null : _DataPath.equals(inst._DataPath))) {
			return false;
		}
		if (!(_Tagged == null ? inst._Tagged == null : _Tagged.equals(inst._Tagged))) {
			return false;
		}
		if (!(_Quality == null ? inst._Quality == null : _Quality.equals(inst._Quality))) {
			return false;
		}
		if (!(_Speed == null ? inst._Speed == null : _Speed.equals(inst._Speed))) {
			return false;
		}
		if (!(_Version == null ? inst._Version == null : _Version.equals(inst._Version))) {
			return false;
		}
		if (!(_Vendor == null ? inst._Vendor == null : _Vendor.equals(inst._Vendor))) {
			return false;
		}
		if (!(_Authors == null ? inst._Authors == null : _Authors.equals(inst._Authors))) {
			return false;
		}
		if (!(_BuildData == null ? inst._BuildData == null : _BuildData.equals(inst._BuildData))) {
			return false;
		}
		if (!(_Licence == null ? inst._Licence == null : _Licence.equals(inst._Licence))) {
			return false;
		}
		if (!(_WebPage == null ? inst._WebPage == null : _WebPage.equals(inst._WebPage))) {
			return false;
		}
		if (!(_Description == null ? inst._Description == null : _Description.equals(inst._Description))) {
			return false;
		}
		if (!(_Copyright == null ? inst._Copyright == null : _Copyright.equals(inst._Copyright))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + (_Name == null ? 0 : _Name.hashCode());
		result = 37*result + (_SupportedLocales == null ? 0 : _SupportedLocales.hashCode());
		result = 37*result + (_DataPath == null ? 0 : _DataPath.hashCode());
		result = 37*result + (_Tagged == null ? 0 : _Tagged.hashCode());
		result = 37*result + (_Quality == null ? 0 : _Quality.hashCode());
		result = 37*result + (_Speed == null ? 0 : _Speed.hashCode());
		result = 37*result + (_Version == null ? 0 : _Version.hashCode());
		result = 37*result + (_Vendor == null ? 0 : _Vendor.hashCode());
		result = 37*result + (_Authors == null ? 0 : _Authors.hashCode());
		result = 37*result + (_BuildData == null ? 0 : _BuildData.hashCode());
		result = 37*result + (_Licence == null ? 0 : _Licence.hashCode());
		result = 37*result + (_WebPage == null ? 0 : _WebPage.hashCode());
		result = 37*result + (_Description == null ? 0 : _Description.hashCode());
		result = 37*result + (_Copyright == null ? 0 : _Copyright.hashCode());
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

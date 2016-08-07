@echo off
echo ******************************************************************************
echo *                         SCHEMA TO BEANS GENERATOR                          *
echo *                 See following page for more informations :                 *
echo *                      http://schema2beans.netbeans.org                      *
echo * this script was created by Jakub Strychowski for Neurpheus project         *
echo *                           www.neurpheus.org                                *
echo ******************************************************************************
echo * Setting up environment                                                     * 
set OPTIONS=-st -javabeans -throw -validate

rem ****************************************************************************************************
rem *                             CHANGE FOLLOWING SECTION TO MIGRATE THIS SCRIPT TO OTHER SCHEMAS                  *
rem ****************************************************************************************************
set PACKAGE=org.neurpheus.nlp.morphology.builder.xml
set SCHEMA_FILE=tagging.dtd
set TARGET_DIR=..\src\main\java
set DIR_TO_CLEAR=%TARGET_DIR%\org\neurpheus\nlp\morphology\builder\xml\*
rem Comment following section for xsd schemas
rem -------------------------------------------------------
set OPTIONS=%OPTIONS% -dtd
echo *   input mode : DTD                                                         *
rem -------------------------------------------------------
rem ****************************************************************************************************

echo ******************************************************************************
echo * Removing previous beans from directory:                                    *
echo        %DIR_TO_CLEAR%
del /Q %DIR_TO_CLEAR%
echo ******************************************************************************
echo *                                                                            *
echo * Generating beans from XML schema                                           *
echo *                                                                            *
echo *                                                                            *
echo *   TARGET PACKAGE :                                                         *
echo *          %PACKAGE%
echo *   SCHEMA FILE :                                                            *
echo *          %SCHEMA_FILE%
echo *   TARGET_DIR :                                                             *
echo *          %TARGET_DIR%
echo *                                                                            *
echo *                                                                            *
echo *----------------------------------------------------------------------------*
set CLASSPATH=org-netbeans-modules-schema2beans.jar;schema2beansdev.jar
java -cp %CLASSPATH% org.netbeans.modules.schema2beansdev.GenBeans -f %SCHEMA_FILE% -p %PACKAGE% -r %TARGET_DIR% %OPTIONS% 
echo *----------------------------------------------------------------------------*
echo ******************************************************************************
echo * End of script                                                              *
echo ******************************************************************************
pause

rem POSSIBLE OPTIONS:
rem
rem -d    DTD root element name (for example webapp or ejb-jar)
rem -p    package name
rem -r    base root directory (root of the package path)
rem -sp    set the indentation to use 'number' spaces instead of the default tab (\t) value
rem -mdd    provides extra information that the dtd cannot provide. If the file doesn't exist, a skeleton file is created and no bean generation happens.
rem -noe    do not throw the NoSuchElement exception when a scalar property has no value, return a default '0' value instead.
rem -ts    the toString() of the bean returns the full content of the bean sub-tree instead of its simple name.
rem -veto    generate vetoable properties (only for non-bean properties).
rem -st    standalone mode - do not generate NetBeans dependencies
rem -throw generate code that prefers to pass exceptions through instead of converting them to RuntimeException (recommended).
rem -dtd    DTD input mode (default)
rem -xmlschema    XML Schema input mode
rem -javabeans    Generate pure JavaBeans that do not need any runtime library support (no BaseBean).
rem -validate    Generate a validate method for doing validation.
rem -propertyevents    Generate methods for dealing with property events (always on for BaseBean type).
rem -attrprop    Attributes become like any other property
rem -delegator    Generate a delegator class for every bean generated.
rem -commoninterface    Generate a common interface between all beans.
rem -premium    The "Premium" Package.  Turn on what ought to be the default switches (but can't be the default due to backwards compatibility).
rem -compile    Compile all generated classes using javac.
rem -defaultsAccessable    Generate methods to be able to get at default values.
rem -useInterfaces    Getters and setters signatures would any defined interface on the bean.
rem -genInterfaces    For every bean generated, generate an interfaces for it's accessors.
rem -keepElementPositions    Keep track of the positions of elements (no BaseBean support).
rem -dumpBeanTree filename    Write out the bean tree to filename.
rem -removeUnreferencedNodes    Do not generate unreferenced nodes from the bean graph.
rem -writeBeanGraph	Write out a beangraph XML file.  Useful for connecting separate bean graphs.
rem -readBeanGraph	Read in and use the results of another bean graph.
rem -genDotGraph    Generate a .dot style file for use with GraphViz.
rem -comments	Process and keep comments (always on for BaseBean type).
rem -doctype	Process and keep Document Types (always on for BaseBean type).
rem -checkUpToDate	Only do generation if the source files are newer than the to be generated files.
rem -t [parse|gen|all]    tracing.
rem -finder	Add a finder method.  Format: "on {start} find {selector} by {key}".  Example: "on /ejb-jar/enterprise-beans find session by ejb-name".
 
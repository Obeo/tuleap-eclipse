/**
 * TuleapTrackerV5APIBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TuleapTrackerV5APIBindingStub extends org.apache.axis.client.Stub implements org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[15];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersion");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        oper.setReturnClass(float.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTrackerList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTracker"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTrackerFields");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTrackerField"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getArtifacts");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "criteria"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfCriteria"), org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "max_rows"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactQueryResult"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addArtifact");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "value"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactFieldValue"), org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateArtifact");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "artifact_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "value"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactFieldValue"), org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "comment"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "comment_format"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getArtifact");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "artifact_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Artifact"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getArtifactsFromReport");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "report_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "max_rows"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactQueryResult"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getArtifactAttachmentChunk");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "artifact_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attachment_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "size"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createTemporaryAttachment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("appendTemporaryAttachmentChunk");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attachment_name"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "content"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("purgeAllTemporaryAttachments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTrackerStructure");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerStructure"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTrackerReports");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "group_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tracker_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTrackerReport"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getArtifactComments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "artifact_id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactComments"));
        oper.setReturnClass(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[14] = oper;

    }

    public TuleapTrackerV5APIBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public TuleapTrackerV5APIBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public TuleapTrackerV5APIBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifact");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Artifact");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactComments");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactComments");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactCrossReferences");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactCrossReferences[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactCrossReferences");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfArtifactFieldValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactFieldValue");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfCriteria");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Criteria");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfFieldValueFileInfo");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.FieldValueFileInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "FieldValueFileInfo");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfInt");
            cachedSerQNames.add(qName);
            cls = int[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTracker");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Tracker");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTrackerField");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerField");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTrackerFieldBindValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerFieldBindValue");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArrayOfTrackerReport");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerReport");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Artifact");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactComments");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactCrossReferences");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactCrossReferences.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactFieldValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactQueryResult");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Criteria");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "CriteriaValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.CriteriaValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "CriteriaValueDate");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.CriteriaValueDate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "CriteriaValueDateAdvanced");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.CriteriaValueDateAdvanced.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "FieldValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.FieldValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "FieldValueFileInfo");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.FieldValueFileInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Tracker");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerField");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerFieldBindValue");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerReport");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerSemantic");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemantic.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerSemanticContributor");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticContributor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerSemanticStatus");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerSemanticTitle");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticTitle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerStructure");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflow");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflow.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleArray");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleArray.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleDate");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleDateArray");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleDate");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleList");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleListArray");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleList");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowTransition");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowTransition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowTransitionArray");
            cachedSerQNames.add(qName);
            cls = org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowTransition[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowTransition");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public float getVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getVersion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Float) _resp).floatValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Float) org.apache.axis.utils.JavaUtils.convert(_resp, float.class)).floatValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[] getTrackerList(java.lang.String sessionKey, int group_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getTrackerList");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getTrackerList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[] getTrackerFields(java.lang.String sessionKey, int group_id, int tracker_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getTrackerFields");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getTrackerFields"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult getArtifacts(java.lang.String sessionKey, int group_id, int tracker_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria[] criteria, int offset, int max_rows) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getArtifacts");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getArtifacts"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id), criteria, new java.lang.Integer(offset), new java.lang.Integer(max_rows)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int addArtifact(java.lang.String sessionKey, int group_id, int tracker_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#addArtifact");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "addArtifact"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id), value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int updateArtifact(java.lang.String sessionKey, int group_id, int tracker_id, int artifact_id, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value, java.lang.String comment, java.lang.String comment_format) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#updateArtifact");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "updateArtifact"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id), new java.lang.Integer(artifact_id), value, comment, comment_format});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact getArtifact(java.lang.String sessionKey, int group_id, int tracker_id, int artifact_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getArtifact");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getArtifact"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id), new java.lang.Integer(artifact_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult getArtifactsFromReport(java.lang.String sessionKey, int report_id, int offset, int max_rows) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getArtifactsFromReport");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getArtifactsFromReport"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(report_id), new java.lang.Integer(offset), new java.lang.Integer(max_rows)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getArtifactAttachmentChunk(java.lang.String sessionKey, int artifact_id, int attachment_id, int offset, int size) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getArtifactAttachmentChunk");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getArtifactAttachmentChunk"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(artifact_id), new java.lang.Integer(attachment_id), new java.lang.Integer(offset), new java.lang.Integer(size)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createTemporaryAttachment(java.lang.String sessionKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#createTemporaryAttachment");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "createTemporaryAttachment"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int appendTemporaryAttachmentChunk(java.lang.String sessionKey, java.lang.String attachment_name, java.lang.String content) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#appendTemporaryAttachmentChunk");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "appendTemporaryAttachmentChunk"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, attachment_name, content});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean purgeAllTemporaryAttachments(java.lang.String sessionKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#purgeAllTemporaryAttachments");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "purgeAllTemporaryAttachments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure getTrackerStructure(java.lang.String sessionKey, int group_id, int tracker_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getTrackerStructure");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getTrackerStructure"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[] getTrackerReports(java.lang.String sessionKey, int group_id, int tracker_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getTrackerReports");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getTrackerReports"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(group_id), new java.lang.Integer(tracker_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[] getArtifactComments(java.lang.String sessionKey, int artifact_id) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://demo.tuleap.net/plugins/tracker/soap#getArtifactComments");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "getArtifactComments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionKey, new java.lang.Integer(artifact_id)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}

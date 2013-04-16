/**
 * CodendiAPILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class CodendiAPILocator extends org.apache.axis.client.Service implements org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPI {

    public CodendiAPILocator() {
    }


    public CodendiAPILocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CodendiAPILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CodendiAPIPort
    private java.lang.String CodendiAPIPort_address = "https://demo.tuleap.net:443/soap/";

    public java.lang.String getCodendiAPIPortAddress() {
        return CodendiAPIPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CodendiAPIPortWSDDServiceName = "CodendiAPIPort";

    public java.lang.String getCodendiAPIPortWSDDServiceName() {
        return CodendiAPIPortWSDDServiceName;
    }

    public void setCodendiAPIPortWSDDServiceName(java.lang.String name) {
        CodendiAPIPortWSDDServiceName = name;
    }

    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType getCodendiAPIPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CodendiAPIPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCodendiAPIPort(endpoint);
    }

    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType getCodendiAPIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIBindingStub _stub = new org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIBindingStub(portAddress, this);
            _stub.setPortName(getCodendiAPIPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCodendiAPIPortEndpointAddress(java.lang.String address) {
        CodendiAPIPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIBindingStub _stub = new org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.CodendiAPIBindingStub(new java.net.URL(CodendiAPIPort_address), this);
                _stub.setPortName(getCodendiAPIPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CodendiAPIPort".equals(inputPortName)) {
            return getCodendiAPIPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://demo.tuleap.net", "CodendiAPI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://demo.tuleap.net", "CodendiAPIPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CodendiAPIPort".equals(portName)) {
            setCodendiAPIPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

/**
 * TuleapTrackerV5APILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TuleapTrackerV5APILocator extends org.apache.axis.client.Service implements org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5API {

	public TuleapTrackerV5APILocator() {
	}

	public TuleapTrackerV5APILocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public TuleapTrackerV5APILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for TuleapTrackerV5APIPort
	private java.lang.String TuleapTrackerV5APIPort_address = "https://demo.tuleap.net:443/plugins/tracker/soap/";

	public java.lang.String getTuleapTrackerV5APIPortAddress() {
		return TuleapTrackerV5APIPort_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String TuleapTrackerV5APIPortWSDDServiceName = "TuleapTrackerV5APIPort";

	public java.lang.String getTuleapTrackerV5APIPortWSDDServiceName() {
		return TuleapTrackerV5APIPortWSDDServiceName;
	}

	public void setTuleapTrackerV5APIPortWSDDServiceName(java.lang.String name) {
		TuleapTrackerV5APIPortWSDDServiceName = name;
	}

	public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType getTuleapTrackerV5APIPort()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(TuleapTrackerV5APIPort_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getTuleapTrackerV5APIPort(endpoint);
	}

	public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType getTuleapTrackerV5APIPort(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIBindingStub _stub = new org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIBindingStub(
					portAddress, this);
			_stub.setPortName(getTuleapTrackerV5APIPortWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setTuleapTrackerV5APIPortEndpointAddress(java.lang.String address) {
		TuleapTrackerV5APIPort_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no port for the given
	 * interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType.class
					.isAssignableFrom(serviceEndpointInterface)) {
				org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIBindingStub _stub = new org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIBindingStub(
						new java.net.URL(TuleapTrackerV5APIPort_address), this);
				_stub.setPortName(getTuleapTrackerV5APIPortWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no port for the given
	 * interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("TuleapTrackerV5APIPort".equals(inputPortName)) {
			return getTuleapTrackerV5APIPort();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub)_stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TuleapTrackerV5API");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
					"TuleapTrackerV5APIPort"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {

		if ("TuleapTrackerV5APIPort".equals(portName)) {
			setTuleapTrackerV5APIPortEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port"
					+ portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}

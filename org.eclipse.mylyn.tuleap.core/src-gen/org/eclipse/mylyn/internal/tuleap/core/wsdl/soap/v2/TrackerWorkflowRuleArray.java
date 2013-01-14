/**
 * TrackerWorkflowRuleArray.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflowRuleArray implements java.io.Serializable {
	private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate[] date;

	private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList[] list;

	public TrackerWorkflowRuleArray() {
	}

	public TrackerWorkflowRuleArray(
			org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate[] date,
			org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList[] list) {
		this.date = date;
		this.list = list;
	}

	/**
	 * Gets the date value for this TrackerWorkflowRuleArray.
	 * 
	 * @return date
	 */
	public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate[] getDate() {
		return date;
	}

	/**
	 * Sets the date value for this TrackerWorkflowRuleArray.
	 * 
	 * @param date
	 */
	public void setDate(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleDate[] date) {
		this.date = date;
	}

	/**
	 * Gets the list value for this TrackerWorkflowRuleArray.
	 * 
	 * @return list
	 */
	public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList[] getList() {
		return list;
	}

	/**
	 * Sets the list value for this TrackerWorkflowRuleArray.
	 * 
	 * @param list
	 */
	public void setList(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleList[] list) {
		this.list = list;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TrackerWorkflowRuleArray)) {
			return false;
		}
		TrackerWorkflowRuleArray other = (TrackerWorkflowRuleArray)obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return __equalsCalc == obj;
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& (this.date == null && other.getDate() == null || this.date != null
						&& java.util.Arrays.equals(this.date, other.getDate()))
				&& (this.list == null && other.getList() == null || this.list != null
						&& java.util.Arrays.equals(this.list, other.getList()));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getDate() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getDate()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getDate(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getList() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getList()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getList(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			TrackerWorkflowRuleArray.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TrackerWorkflowRuleArray"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("date");
		elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
		elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TrackerWorkflowRuleDate"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("list");
		elemField.setXmlName(new javax.xml.namespace.QName("", "list"));
		elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TrackerWorkflowRuleList"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}

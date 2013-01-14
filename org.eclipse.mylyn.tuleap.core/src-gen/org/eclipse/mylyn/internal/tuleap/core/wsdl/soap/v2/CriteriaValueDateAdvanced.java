/**
 * CriteriaValueDateAdvanced.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class CriteriaValueDateAdvanced implements java.io.Serializable {
	private int from_date;

	private int to_date;

	public CriteriaValueDateAdvanced() {
	}

	public CriteriaValueDateAdvanced(int from_date, int to_date) {
		this.from_date = from_date;
		this.to_date = to_date;
	}

	/**
	 * Gets the from_date value for this CriteriaValueDateAdvanced.
	 * 
	 * @return from_date
	 */
	public int getFrom_date() {
		return from_date;
	}

	/**
	 * Sets the from_date value for this CriteriaValueDateAdvanced.
	 * 
	 * @param from_date
	 */
	public void setFrom_date(int from_date) {
		this.from_date = from_date;
	}

	/**
	 * Gets the to_date value for this CriteriaValueDateAdvanced.
	 * 
	 * @return to_date
	 */
	public int getTo_date() {
		return to_date;
	}

	/**
	 * Sets the to_date value for this CriteriaValueDateAdvanced.
	 * 
	 * @param to_date
	 */
	public void setTo_date(int to_date) {
		this.to_date = to_date;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof CriteriaValueDateAdvanced)) {
			return false;
		}
		CriteriaValueDateAdvanced other = (CriteriaValueDateAdvanced)obj;
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
		_equals = true && this.from_date == other.getFrom_date() && this.to_date == other.getTo_date();
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
		_hashCode += getFrom_date();
		_hashCode += getTo_date();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			CriteriaValueDateAdvanced.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"CriteriaValueDateAdvanced"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("from_date");
		elemField.setXmlName(new javax.xml.namespace.QName("", "from_date"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("to_date");
		elemField.setXmlName(new javax.xml.namespace.QName("", "to_date"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

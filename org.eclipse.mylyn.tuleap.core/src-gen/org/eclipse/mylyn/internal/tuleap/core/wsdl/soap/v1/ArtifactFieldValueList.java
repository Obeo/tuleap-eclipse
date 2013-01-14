/**
 * ArtifactFieldValueList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactFieldValueList implements java.io.Serializable {
	private int field_id;

	private int group_artifact_id;

	private int value_id;

	private java.lang.String value;

	private java.lang.String description;

	private int order_id;

	private java.lang.String status;

	public ArtifactFieldValueList() {
	}

	public ArtifactFieldValueList(int field_id, int group_artifact_id, int value_id, java.lang.String value,
			java.lang.String description, int order_id, java.lang.String status) {
		this.field_id = field_id;
		this.group_artifact_id = group_artifact_id;
		this.value_id = value_id;
		this.value = value;
		this.description = description;
		this.order_id = order_id;
		this.status = status;
	}

	/**
	 * Gets the field_id value for this ArtifactFieldValueList.
	 * 
	 * @return field_id
	 */
	public int getField_id() {
		return field_id;
	}

	/**
	 * Sets the field_id value for this ArtifactFieldValueList.
	 * 
	 * @param field_id
	 */
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	/**
	 * Gets the group_artifact_id value for this ArtifactFieldValueList.
	 * 
	 * @return group_artifact_id
	 */
	public int getGroup_artifact_id() {
		return group_artifact_id;
	}

	/**
	 * Sets the group_artifact_id value for this ArtifactFieldValueList.
	 * 
	 * @param group_artifact_id
	 */
	public void setGroup_artifact_id(int group_artifact_id) {
		this.group_artifact_id = group_artifact_id;
	}

	/**
	 * Gets the value_id value for this ArtifactFieldValueList.
	 * 
	 * @return value_id
	 */
	public int getValue_id() {
		return value_id;
	}

	/**
	 * Sets the value_id value for this ArtifactFieldValueList.
	 * 
	 * @param value_id
	 */
	public void setValue_id(int value_id) {
		this.value_id = value_id;
	}

	/**
	 * Gets the value value for this ArtifactFieldValueList.
	 * 
	 * @return value
	 */
	public java.lang.String getValue() {
		return value;
	}

	/**
	 * Sets the value value for this ArtifactFieldValueList.
	 * 
	 * @param value
	 */
	public void setValue(java.lang.String value) {
		this.value = value;
	}

	/**
	 * Gets the description value for this ArtifactFieldValueList.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this ArtifactFieldValueList.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the order_id value for this ArtifactFieldValueList.
	 * 
	 * @return order_id
	 */
	public int getOrder_id() {
		return order_id;
	}

	/**
	 * Sets the order_id value for this ArtifactFieldValueList.
	 * 
	 * @param order_id
	 */
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	/**
	 * Gets the status value for this ArtifactFieldValueList.
	 * 
	 * @return status
	 */
	public java.lang.String getStatus() {
		return status;
	}

	/**
	 * Sets the status value for this ArtifactFieldValueList.
	 * 
	 * @param status
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArtifactFieldValueList)) {
			return false;
		}
		ArtifactFieldValueList other = (ArtifactFieldValueList)obj;
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
				&& this.field_id == other.getField_id()
				&& this.group_artifact_id == other.getGroup_artifact_id()
				&& this.value_id == other.getValue_id()
				&& (this.value == null && other.getValue() == null || this.value != null
						&& this.value.equals(other.getValue()))
				&& (this.description == null && other.getDescription() == null || this.description != null
						&& this.description.equals(other.getDescription()))
				&& this.order_id == other.getOrder_id()
				&& (this.status == null && other.getStatus() == null || this.status != null
						&& this.status.equals(other.getStatus()));
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
		_hashCode += getField_id();
		_hashCode += getGroup_artifact_id();
		_hashCode += getValue_id();
		if (getValue() != null) {
			_hashCode += getValue().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		_hashCode += getOrder_id();
		if (getStatus() != null) {
			_hashCode += getStatus().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ArtifactFieldValueList.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFieldValueList"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("field_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("group_artifact_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "group_artifact_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("value_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "value_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("value");
		elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("order_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "order_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

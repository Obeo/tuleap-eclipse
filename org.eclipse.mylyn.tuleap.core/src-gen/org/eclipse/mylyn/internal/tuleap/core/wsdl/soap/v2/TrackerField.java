/**
 * TrackerField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerField implements java.io.Serializable {
	private int tracker_id;

	private int field_id;

	private java.lang.String short_name;

	private java.lang.String label;

	private java.lang.String type;

	private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue[] values;

	private java.lang.String[] permissions;

	public TrackerField() {
	}

	public TrackerField(int tracker_id, int field_id, java.lang.String short_name, java.lang.String label,
			java.lang.String type,
			org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue[] values,
			java.lang.String[] permissions) {
		this.tracker_id = tracker_id;
		this.field_id = field_id;
		this.short_name = short_name;
		this.label = label;
		this.type = type;
		this.values = values;
		this.permissions = permissions;
	}

	/**
	 * Gets the tracker_id value for this TrackerField.
	 * 
	 * @return tracker_id
	 */
	public int getTracker_id() {
		return tracker_id;
	}

	/**
	 * Sets the tracker_id value for this TrackerField.
	 * 
	 * @param tracker_id
	 */
	public void setTracker_id(int tracker_id) {
		this.tracker_id = tracker_id;
	}

	/**
	 * Gets the field_id value for this TrackerField.
	 * 
	 * @return field_id
	 */
	public int getField_id() {
		return field_id;
	}

	/**
	 * Sets the field_id value for this TrackerField.
	 * 
	 * @param field_id
	 */
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	/**
	 * Gets the short_name value for this TrackerField.
	 * 
	 * @return short_name
	 */
	public java.lang.String getShort_name() {
		return short_name;
	}

	/**
	 * Sets the short_name value for this TrackerField.
	 * 
	 * @param short_name
	 */
	public void setShort_name(java.lang.String short_name) {
		this.short_name = short_name;
	}

	/**
	 * Gets the label value for this TrackerField.
	 * 
	 * @return label
	 */
	public java.lang.String getLabel() {
		return label;
	}

	/**
	 * Sets the label value for this TrackerField.
	 * 
	 * @param label
	 */
	public void setLabel(java.lang.String label) {
		this.label = label;
	}

	/**
	 * Gets the type value for this TrackerField.
	 * 
	 * @return type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Sets the type value for this TrackerField.
	 * 
	 * @param type
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * Gets the values value for this TrackerField.
	 * 
	 * @return values
	 */
	public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue[] getValues() {
		return values;
	}

	/**
	 * Sets the values value for this TrackerField.
	 * 
	 * @param values
	 */
	public void setValues(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue[] values) {
		this.values = values;
	}

	/**
	 * Gets the permissions value for this TrackerField.
	 * 
	 * @return permissions
	 */
	public java.lang.String[] getPermissions() {
		return permissions;
	}

	/**
	 * Sets the permissions value for this TrackerField.
	 * 
	 * @param permissions
	 */
	public void setPermissions(java.lang.String[] permissions) {
		this.permissions = permissions;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TrackerField)) {
			return false;
		}
		TrackerField other = (TrackerField)obj;
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
				&& this.tracker_id == other.getTracker_id()
				&& this.field_id == other.getField_id()
				&& (this.short_name == null && other.getShort_name() == null || this.short_name != null
						&& this.short_name.equals(other.getShort_name()))
				&& (this.label == null && other.getLabel() == null || this.label != null
						&& this.label.equals(other.getLabel()))
				&& (this.type == null && other.getType() == null || this.type != null
						&& this.type.equals(other.getType()))
				&& (this.values == null && other.getValues() == null || this.values != null
						&& java.util.Arrays.equals(this.values, other.getValues()))
				&& (this.permissions == null && other.getPermissions() == null || this.permissions != null
						&& java.util.Arrays.equals(this.permissions, other.getPermissions()));
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
		_hashCode += getTracker_id();
		_hashCode += getField_id();
		if (getShort_name() != null) {
			_hashCode += getShort_name().hashCode();
		}
		if (getLabel() != null) {
			_hashCode += getLabel().hashCode();
		}
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getValues() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getValues()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getValues(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getPermissions() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getPermissions()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getPermissions(), i);
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
			TrackerField.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TrackerField"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tracker_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "tracker_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("field_id");
		elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("short_name");
		elemField.setXmlName(new javax.xml.namespace.QName("", "short_name"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("label");
		elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("values");
		elemField.setXmlName(new javax.xml.namespace.QName("", "values"));
		elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"TrackerFieldBindValue"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("permissions");
		elemField.setXmlName(new javax.xml.namespace.QName("", "permissions"));
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

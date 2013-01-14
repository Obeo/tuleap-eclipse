/**
 * ArtifactComments.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class ArtifactComments implements java.io.Serializable {
	private int submitted_by;

	private java.lang.String email;

	private int submitted_on;

	private java.lang.String body;

	public ArtifactComments() {
	}

	public ArtifactComments(int submitted_by, java.lang.String email, int submitted_on, java.lang.String body) {
		this.submitted_by = submitted_by;
		this.email = email;
		this.submitted_on = submitted_on;
		this.body = body;
	}

	/**
	 * Gets the submitted_by value for this ArtifactComments.
	 * 
	 * @return submitted_by
	 */
	public int getSubmitted_by() {
		return submitted_by;
	}

	/**
	 * Sets the submitted_by value for this ArtifactComments.
	 * 
	 * @param submitted_by
	 */
	public void setSubmitted_by(int submitted_by) {
		this.submitted_by = submitted_by;
	}

	/**
	 * Gets the email value for this ArtifactComments.
	 * 
	 * @return email
	 */
	public java.lang.String getEmail() {
		return email;
	}

	/**
	 * Sets the email value for this ArtifactComments.
	 * 
	 * @param email
	 */
	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	/**
	 * Gets the submitted_on value for this ArtifactComments.
	 * 
	 * @return submitted_on
	 */
	public int getSubmitted_on() {
		return submitted_on;
	}

	/**
	 * Sets the submitted_on value for this ArtifactComments.
	 * 
	 * @param submitted_on
	 */
	public void setSubmitted_on(int submitted_on) {
		this.submitted_on = submitted_on;
	}

	/**
	 * Gets the body value for this ArtifactComments.
	 * 
	 * @return body
	 */
	public java.lang.String getBody() {
		return body;
	}

	/**
	 * Sets the body value for this ArtifactComments.
	 * 
	 * @param body
	 */
	public void setBody(java.lang.String body) {
		this.body = body;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ArtifactComments)) {
			return false;
		}
		ArtifactComments other = (ArtifactComments)obj;
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
				&& this.submitted_by == other.getSubmitted_by()
				&& (this.email == null && other.getEmail() == null || this.email != null
						&& this.email.equals(other.getEmail()))
				&& this.submitted_on == other.getSubmitted_on()
				&& (this.body == null && other.getBody() == null || this.body != null
						&& this.body.equals(other.getBody()));
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
		_hashCode += getSubmitted_by();
		if (getEmail() != null) {
			_hashCode += getEmail().hashCode();
		}
		_hashCode += getSubmitted_on();
		if (getBody() != null) {
			_hashCode += getBody().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ArtifactComments.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap",
				"ArtifactComments"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("submitted_by");
		elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_by"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("email");
		elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("submitted_on");
		elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_on"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("body");
		elemField.setXmlName(new javax.xml.namespace.QName("", "body"));
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

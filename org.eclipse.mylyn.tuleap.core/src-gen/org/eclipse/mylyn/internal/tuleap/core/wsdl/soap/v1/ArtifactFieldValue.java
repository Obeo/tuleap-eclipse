/**
 * ArtifactFieldValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactFieldValue  implements java.io.Serializable {
    private int field_id;

    private int artifact_id;

    private java.lang.String field_value;

    public ArtifactFieldValue() {
    }

    public ArtifactFieldValue(
           int field_id,
           int artifact_id,
           java.lang.String field_value) {
           this.field_id = field_id;
           this.artifact_id = artifact_id;
           this.field_value = field_value;
    }


    /**
     * Gets the field_id value for this ArtifactFieldValue.
     * 
     * @return field_id
     */
    public int getField_id() {
        return field_id;
    }


    /**
     * Sets the field_id value for this ArtifactFieldValue.
     * 
     * @param field_id
     */
    public void setField_id(int field_id) {
        this.field_id = field_id;
    }


    /**
     * Gets the artifact_id value for this ArtifactFieldValue.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this ArtifactFieldValue.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the field_value value for this ArtifactFieldValue.
     * 
     * @return field_value
     */
    public java.lang.String getField_value() {
        return field_value;
    }


    /**
     * Sets the field_value value for this ArtifactFieldValue.
     * 
     * @param field_value
     */
    public void setField_value(java.lang.String field_value) {
        this.field_value = field_value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactFieldValue)) return false;
        ArtifactFieldValue other = (ArtifactFieldValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.field_id == other.getField_id() &&
            this.artifact_id == other.getArtifact_id() &&
            ((this.field_value==null && other.getField_value()==null) || 
             (this.field_value!=null &&
              this.field_value.equals(other.getField_value())));
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
        _hashCode += getArtifact_id();
        if (getField_value() != null) {
            _hashCode += getField_value().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactFieldValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFieldValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_value"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

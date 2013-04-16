/**
 * ArtifactFieldFromReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactFieldFromReport  implements java.io.Serializable {
    private java.lang.String field_name;

    private java.lang.String field_value;

    public ArtifactFieldFromReport() {
    }

    public ArtifactFieldFromReport(
           java.lang.String field_name,
           java.lang.String field_value) {
           this.field_name = field_name;
           this.field_value = field_value;
    }


    /**
     * Gets the field_name value for this ArtifactFieldFromReport.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this ArtifactFieldFromReport.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the field_value value for this ArtifactFieldFromReport.
     * 
     * @return field_value
     */
    public java.lang.String getField_value() {
        return field_value;
    }


    /**
     * Sets the field_value value for this ArtifactFieldFromReport.
     * 
     * @param field_value
     */
    public void setField_value(java.lang.String field_value) {
        this.field_value = field_value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactFieldFromReport)) return false;
        ArtifactFieldFromReport other = (ArtifactFieldFromReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.field_name==null && other.getField_name()==null) || 
             (this.field_name!=null &&
              this.field_name.equals(other.getField_name()))) &&
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
        if (getField_name() != null) {
            _hashCode += getField_name().hashCode();
        }
        if (getField_value() != null) {
            _hashCode += getField_value().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactFieldFromReport.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFieldFromReport"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

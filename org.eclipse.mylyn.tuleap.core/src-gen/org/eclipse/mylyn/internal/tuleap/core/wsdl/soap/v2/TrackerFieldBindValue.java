/**
 * TrackerFieldBindValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerFieldBindValue  implements java.io.Serializable {
    private int field_id;

    private int bind_value_id;

    private java.lang.String bind_value_label;

    public TrackerFieldBindValue() {
    }

    public TrackerFieldBindValue(
           int field_id,
           int bind_value_id,
           java.lang.String bind_value_label) {
           this.field_id = field_id;
           this.bind_value_id = bind_value_id;
           this.bind_value_label = bind_value_label;
    }


    /**
     * Gets the field_id value for this TrackerFieldBindValue.
     * 
     * @return field_id
     */
    public int getField_id() {
        return field_id;
    }


    /**
     * Sets the field_id value for this TrackerFieldBindValue.
     * 
     * @param field_id
     */
    public void setField_id(int field_id) {
        this.field_id = field_id;
    }


    /**
     * Gets the bind_value_id value for this TrackerFieldBindValue.
     * 
     * @return bind_value_id
     */
    public int getBind_value_id() {
        return bind_value_id;
    }


    /**
     * Sets the bind_value_id value for this TrackerFieldBindValue.
     * 
     * @param bind_value_id
     */
    public void setBind_value_id(int bind_value_id) {
        this.bind_value_id = bind_value_id;
    }


    /**
     * Gets the bind_value_label value for this TrackerFieldBindValue.
     * 
     * @return bind_value_label
     */
    public java.lang.String getBind_value_label() {
        return bind_value_label;
    }


    /**
     * Sets the bind_value_label value for this TrackerFieldBindValue.
     * 
     * @param bind_value_label
     */
    public void setBind_value_label(java.lang.String bind_value_label) {
        this.bind_value_label = bind_value_label;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerFieldBindValue)) return false;
        TrackerFieldBindValue other = (TrackerFieldBindValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.field_id == other.getField_id() &&
            this.bind_value_id == other.getBind_value_id() &&
            ((this.bind_value_label==null && other.getBind_value_label()==null) || 
             (this.bind_value_label!=null &&
              this.bind_value_label.equals(other.getBind_value_label())));
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
        _hashCode += getBind_value_id();
        if (getBind_value_label() != null) {
            _hashCode += getBind_value_label().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerFieldBindValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerFieldBindValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bind_value_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bind_value_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bind_value_label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bind_value_label"));
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

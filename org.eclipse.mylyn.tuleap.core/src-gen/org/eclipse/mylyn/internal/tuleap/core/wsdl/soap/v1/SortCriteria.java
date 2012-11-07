/**
 * SortCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class SortCriteria  implements java.io.Serializable {
    private java.lang.String field_name;

    private java.lang.String sort_direction;

    public SortCriteria() {
    }

    public SortCriteria(
           java.lang.String field_name,
           java.lang.String sort_direction) {
           this.field_name = field_name;
           this.sort_direction = sort_direction;
    }


    /**
     * Gets the field_name value for this SortCriteria.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this SortCriteria.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the sort_direction value for this SortCriteria.
     * 
     * @return sort_direction
     */
    public java.lang.String getSort_direction() {
        return sort_direction;
    }


    /**
     * Sets the sort_direction value for this SortCriteria.
     * 
     * @param sort_direction
     */
    public void setSort_direction(java.lang.String sort_direction) {
        this.sort_direction = sort_direction;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SortCriteria)) return false;
        SortCriteria other = (SortCriteria) obj;
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
            ((this.sort_direction==null && other.getSort_direction()==null) || 
             (this.sort_direction!=null &&
              this.sort_direction.equals(other.getSort_direction())));
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
        if (getSort_direction() != null) {
            _hashCode += getSort_direction().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SortCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "SortCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sort_direction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sort_direction"));
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

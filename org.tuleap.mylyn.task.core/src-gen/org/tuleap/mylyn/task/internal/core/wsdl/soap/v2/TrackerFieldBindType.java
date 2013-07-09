/**
 * TrackerFieldBindType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerFieldBindType  implements java.io.Serializable {
    private java.lang.String bind_type;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.UGroup[] bind_list;

    public TrackerFieldBindType() {
    }

    public TrackerFieldBindType(
           java.lang.String bind_type,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.UGroup[] bind_list) {
           this.bind_type = bind_type;
           this.bind_list = bind_list;
    }


    /**
     * Gets the bind_type value for this TrackerFieldBindType.
     * 
     * @return bind_type
     */
    public java.lang.String getBind_type() {
        return bind_type;
    }


    /**
     * Sets the bind_type value for this TrackerFieldBindType.
     * 
     * @param bind_type
     */
    public void setBind_type(java.lang.String bind_type) {
        this.bind_type = bind_type;
    }


    /**
     * Gets the bind_list value for this TrackerFieldBindType.
     * 
     * @return bind_list
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.UGroup[] getBind_list() {
        return bind_list;
    }


    /**
     * Sets the bind_list value for this TrackerFieldBindType.
     * 
     * @param bind_list
     */
    public void setBind_list(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.UGroup[] bind_list) {
        this.bind_list = bind_list;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerFieldBindType)) return false;
        TrackerFieldBindType other = (TrackerFieldBindType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bind_type==null && other.getBind_type()==null) || 
             (this.bind_type!=null &&
              this.bind_type.equals(other.getBind_type()))) &&
            ((this.bind_list==null && other.getBind_list()==null) || 
             (this.bind_list!=null &&
              java.util.Arrays.equals(this.bind_list, other.getBind_list())));
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
        if (getBind_type() != null) {
            _hashCode += getBind_type().hashCode();
        }
        if (getBind_list() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBind_list());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBind_list(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerFieldBindType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerFieldBindType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bind_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bind_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bind_list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bind_list"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "UGroup"));
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

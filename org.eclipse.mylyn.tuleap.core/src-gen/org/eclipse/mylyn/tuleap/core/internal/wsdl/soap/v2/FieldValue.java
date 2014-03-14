/**
 * FieldValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2;

@SuppressWarnings("all")
public class FieldValue  implements java.io.Serializable {
    private java.lang.String value;

    private org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValueFileInfo[] file_info;

    private org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerFieldBindValue[] bind_value;

    public FieldValue() {
    }

    public FieldValue(
           java.lang.String value,
           org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValueFileInfo[] file_info,
           org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerFieldBindValue[] bind_value) {
           this.value = value;
           this.file_info = file_info;
           this.bind_value = bind_value;
    }


    /**
     * Gets the value value for this FieldValue.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this FieldValue.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }


    /**
     * Gets the file_info value for this FieldValue.
     * 
     * @return file_info
     */
    public org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValueFileInfo[] getFile_info() {
        return file_info;
    }


    /**
     * Sets the file_info value for this FieldValue.
     * 
     * @param file_info
     */
    public void setFile_info(org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.FieldValueFileInfo[] file_info) {
        this.file_info = file_info;
    }


    /**
     * Gets the bind_value value for this FieldValue.
     * 
     * @return bind_value
     */
    public org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerFieldBindValue[] getBind_value() {
        return bind_value;
    }


    /**
     * Sets the bind_value value for this FieldValue.
     * 
     * @param bind_value
     */
    public void setBind_value(org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerFieldBindValue[] bind_value) {
        this.bind_value = bind_value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FieldValue)) return false;
        FieldValue other = (FieldValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.file_info==null && other.getFile_info()==null) || 
             (this.file_info!=null &&
              java.util.Arrays.equals(this.file_info, other.getFile_info()))) &&
            ((this.bind_value==null && other.getBind_value()==null) || 
             (this.bind_value!=null &&
              java.util.Arrays.equals(this.bind_value, other.getBind_value())));
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
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getFile_info() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFile_info());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFile_info(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBind_value() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBind_value());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBind_value(), i);
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
        new org.apache.axis.description.TypeDesc(FieldValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "FieldValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file_info");
        elemField.setXmlName(new javax.xml.namespace.QName("", "file_info"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "FieldValueFileInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bind_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bind_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerFieldBindValue"));
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

/**
 * ArtifactHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactHistory  implements java.io.Serializable {
    private java.lang.String field_name;

    private java.lang.String old_value;

    private java.lang.String new_value;

    private java.lang.String modification_by;

    private int date;

    public ArtifactHistory() {
    }

    public ArtifactHistory(
           java.lang.String field_name,
           java.lang.String old_value,
           java.lang.String new_value,
           java.lang.String modification_by,
           int date) {
           this.field_name = field_name;
           this.old_value = old_value;
           this.new_value = new_value;
           this.modification_by = modification_by;
           this.date = date;
    }


    /**
     * Gets the field_name value for this ArtifactHistory.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this ArtifactHistory.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the old_value value for this ArtifactHistory.
     * 
     * @return old_value
     */
    public java.lang.String getOld_value() {
        return old_value;
    }


    /**
     * Sets the old_value value for this ArtifactHistory.
     * 
     * @param old_value
     */
    public void setOld_value(java.lang.String old_value) {
        this.old_value = old_value;
    }


    /**
     * Gets the new_value value for this ArtifactHistory.
     * 
     * @return new_value
     */
    public java.lang.String getNew_value() {
        return new_value;
    }


    /**
     * Sets the new_value value for this ArtifactHistory.
     * 
     * @param new_value
     */
    public void setNew_value(java.lang.String new_value) {
        this.new_value = new_value;
    }


    /**
     * Gets the modification_by value for this ArtifactHistory.
     * 
     * @return modification_by
     */
    public java.lang.String getModification_by() {
        return modification_by;
    }


    /**
     * Sets the modification_by value for this ArtifactHistory.
     * 
     * @param modification_by
     */
    public void setModification_by(java.lang.String modification_by) {
        this.modification_by = modification_by;
    }


    /**
     * Gets the date value for this ArtifactHistory.
     * 
     * @return date
     */
    public int getDate() {
        return date;
    }


    /**
     * Sets the date value for this ArtifactHistory.
     * 
     * @param date
     */
    public void setDate(int date) {
        this.date = date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactHistory)) return false;
        ArtifactHistory other = (ArtifactHistory) obj;
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
            ((this.old_value==null && other.getOld_value()==null) || 
             (this.old_value!=null &&
              this.old_value.equals(other.getOld_value()))) &&
            ((this.new_value==null && other.getNew_value()==null) || 
             (this.new_value!=null &&
              this.new_value.equals(other.getNew_value()))) &&
            ((this.modification_by==null && other.getModification_by()==null) || 
             (this.modification_by!=null &&
              this.modification_by.equals(other.getModification_by()))) &&
            this.date == other.getDate();
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
        if (getOld_value() != null) {
            _hashCode += getOld_value().hashCode();
        }
        if (getNew_value() != null) {
            _hashCode += getNew_value().hashCode();
        }
        if (getModification_by() != null) {
            _hashCode += getModification_by().hashCode();
        }
        _hashCode += getDate();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("old_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "old_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("new_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "new_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modification_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modification_by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
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

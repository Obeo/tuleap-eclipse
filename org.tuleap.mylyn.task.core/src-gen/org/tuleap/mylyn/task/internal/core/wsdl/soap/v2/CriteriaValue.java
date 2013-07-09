/**
 * CriteriaValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class CriteriaValue  implements java.io.Serializable {
    private java.lang.String value;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDate date;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDateAdvanced dateAdvanced;

    public CriteriaValue() {
    }

    public CriteriaValue(
           java.lang.String value,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDate date,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDateAdvanced dateAdvanced) {
           this.value = value;
           this.date = date;
           this.dateAdvanced = dateAdvanced;
    }


    /**
     * Gets the value value for this CriteriaValue.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this CriteriaValue.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }


    /**
     * Gets the date value for this CriteriaValue.
     * 
     * @return date
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDate getDate() {
        return date;
    }


    /**
     * Sets the date value for this CriteriaValue.
     * 
     * @param date
     */
    public void setDate(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDate date) {
        this.date = date;
    }


    /**
     * Gets the dateAdvanced value for this CriteriaValue.
     * 
     * @return dateAdvanced
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDateAdvanced getDateAdvanced() {
        return dateAdvanced;
    }


    /**
     * Sets the dateAdvanced value for this CriteriaValue.
     * 
     * @param dateAdvanced
     */
    public void setDateAdvanced(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.CriteriaValueDateAdvanced dateAdvanced) {
        this.dateAdvanced = dateAdvanced;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CriteriaValue)) return false;
        CriteriaValue other = (CriteriaValue) obj;
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
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.dateAdvanced==null && other.getDateAdvanced()==null) || 
             (this.dateAdvanced!=null &&
              this.dateAdvanced.equals(other.getDateAdvanced())));
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
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getDateAdvanced() != null) {
            _hashCode += getDateAdvanced().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CriteriaValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "CriteriaValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "CriteriaValueDate"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAdvanced");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateAdvanced"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "CriteriaValueDateAdvanced"));
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

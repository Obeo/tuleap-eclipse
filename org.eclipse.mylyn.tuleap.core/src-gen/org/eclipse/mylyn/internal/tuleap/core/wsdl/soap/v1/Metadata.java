/**
 * Metadata.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class Metadata  implements java.io.Serializable {
    private java.lang.String label;

    private java.lang.String name;

    private java.lang.String type;

    private int isMultipleValuesAllowed;

    private int isEmptyAllowed;

    private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataListValue[] listOfValues;

    public Metadata() {
    }

    public Metadata(
           java.lang.String label,
           java.lang.String name,
           java.lang.String type,
           int isMultipleValuesAllowed,
           int isEmptyAllowed,
           org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataListValue[] listOfValues) {
           this.label = label;
           this.name = name;
           this.type = type;
           this.isMultipleValuesAllowed = isMultipleValuesAllowed;
           this.isEmptyAllowed = isEmptyAllowed;
           this.listOfValues = listOfValues;
    }


    /**
     * Gets the label value for this Metadata.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this Metadata.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the name value for this Metadata.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Metadata.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the type value for this Metadata.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this Metadata.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the isMultipleValuesAllowed value for this Metadata.
     * 
     * @return isMultipleValuesAllowed
     */
    public int getIsMultipleValuesAllowed() {
        return isMultipleValuesAllowed;
    }


    /**
     * Sets the isMultipleValuesAllowed value for this Metadata.
     * 
     * @param isMultipleValuesAllowed
     */
    public void setIsMultipleValuesAllowed(int isMultipleValuesAllowed) {
        this.isMultipleValuesAllowed = isMultipleValuesAllowed;
    }


    /**
     * Gets the isEmptyAllowed value for this Metadata.
     * 
     * @return isEmptyAllowed
     */
    public int getIsEmptyAllowed() {
        return isEmptyAllowed;
    }


    /**
     * Sets the isEmptyAllowed value for this Metadata.
     * 
     * @param isEmptyAllowed
     */
    public void setIsEmptyAllowed(int isEmptyAllowed) {
        this.isEmptyAllowed = isEmptyAllowed;
    }


    /**
     * Gets the listOfValues value for this Metadata.
     * 
     * @return listOfValues
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataListValue[] getListOfValues() {
        return listOfValues;
    }


    /**
     * Sets the listOfValues value for this Metadata.
     * 
     * @param listOfValues
     */
    public void setListOfValues(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.MetadataListValue[] listOfValues) {
        this.listOfValues = listOfValues;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Metadata)) return false;
        Metadata other = (Metadata) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            this.isMultipleValuesAllowed == other.getIsMultipleValuesAllowed() &&
            this.isEmptyAllowed == other.getIsEmptyAllowed() &&
            ((this.listOfValues==null && other.getListOfValues()==null) || 
             (this.listOfValues!=null &&
              java.util.Arrays.equals(this.listOfValues, other.getListOfValues())));
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
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        _hashCode += getIsMultipleValuesAllowed();
        _hashCode += getIsEmptyAllowed();
        if (getListOfValues() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListOfValues());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListOfValues(), i);
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
        new org.apache.axis.description.TypeDesc(Metadata.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "Metadata"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
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
        elemField.setFieldName("isMultipleValuesAllowed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isMultipleValuesAllowed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isEmptyAllowed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isEmptyAllowed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listOfValues");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listOfValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "MetadataListValue"));
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

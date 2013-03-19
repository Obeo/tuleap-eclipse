/**
 * FRSPackage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class FRSPackage  implements java.io.Serializable {
    private int package_id;

    private int group_id;

    private java.lang.String name;

    private int status_id;

    private int rank;

    private boolean approve_license;

    public FRSPackage() {
    }

    public FRSPackage(
           int package_id,
           int group_id,
           java.lang.String name,
           int status_id,
           int rank,
           boolean approve_license) {
           this.package_id = package_id;
           this.group_id = group_id;
           this.name = name;
           this.status_id = status_id;
           this.rank = rank;
           this.approve_license = approve_license;
    }


    /**
     * Gets the package_id value for this FRSPackage.
     * 
     * @return package_id
     */
    public int getPackage_id() {
        return package_id;
    }


    /**
     * Sets the package_id value for this FRSPackage.
     * 
     * @param package_id
     */
    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }


    /**
     * Gets the group_id value for this FRSPackage.
     * 
     * @return group_id
     */
    public int getGroup_id() {
        return group_id;
    }


    /**
     * Sets the group_id value for this FRSPackage.
     * 
     * @param group_id
     */
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }


    /**
     * Gets the name value for this FRSPackage.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FRSPackage.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the status_id value for this FRSPackage.
     * 
     * @return status_id
     */
    public int getStatus_id() {
        return status_id;
    }


    /**
     * Sets the status_id value for this FRSPackage.
     * 
     * @param status_id
     */
    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }


    /**
     * Gets the rank value for this FRSPackage.
     * 
     * @return rank
     */
    public int getRank() {
        return rank;
    }


    /**
     * Sets the rank value for this FRSPackage.
     * 
     * @param rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }


    /**
     * Gets the approve_license value for this FRSPackage.
     * 
     * @return approve_license
     */
    public boolean isApprove_license() {
        return approve_license;
    }


    /**
     * Sets the approve_license value for this FRSPackage.
     * 
     * @param approve_license
     */
    public void setApprove_license(boolean approve_license) {
        this.approve_license = approve_license;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FRSPackage)) return false;
        FRSPackage other = (FRSPackage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.package_id == other.getPackage_id() &&
            this.group_id == other.getGroup_id() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.status_id == other.getStatus_id() &&
            this.rank == other.getRank() &&
            this.approve_license == other.isApprove_license();
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
        _hashCode += getPackage_id();
        _hashCode += getGroup_id();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += getStatus_id();
        _hashCode += getRank();
        _hashCode += (isApprove_license() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FRSPackage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "FRSPackage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("package_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "package_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rank");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rank"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approve_license");
        elemField.setXmlName(new javax.xml.namespace.QName("", "approve_license"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

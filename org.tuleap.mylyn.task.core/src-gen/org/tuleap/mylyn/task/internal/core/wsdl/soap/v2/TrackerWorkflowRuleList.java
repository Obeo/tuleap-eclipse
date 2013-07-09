/**
 * TrackerWorkflowRuleList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflowRuleList  implements java.io.Serializable {
    private int source_field_id;

    private int target_field_id;

    private int source_value_id;

    private int target_value_id;

    public TrackerWorkflowRuleList() {
    }

    public TrackerWorkflowRuleList(
           int source_field_id,
           int target_field_id,
           int source_value_id,
           int target_value_id) {
           this.source_field_id = source_field_id;
           this.target_field_id = target_field_id;
           this.source_value_id = source_value_id;
           this.target_value_id = target_value_id;
    }


    /**
     * Gets the source_field_id value for this TrackerWorkflowRuleList.
     * 
     * @return source_field_id
     */
    public int getSource_field_id() {
        return source_field_id;
    }


    /**
     * Sets the source_field_id value for this TrackerWorkflowRuleList.
     * 
     * @param source_field_id
     */
    public void setSource_field_id(int source_field_id) {
        this.source_field_id = source_field_id;
    }


    /**
     * Gets the target_field_id value for this TrackerWorkflowRuleList.
     * 
     * @return target_field_id
     */
    public int getTarget_field_id() {
        return target_field_id;
    }


    /**
     * Sets the target_field_id value for this TrackerWorkflowRuleList.
     * 
     * @param target_field_id
     */
    public void setTarget_field_id(int target_field_id) {
        this.target_field_id = target_field_id;
    }


    /**
     * Gets the source_value_id value for this TrackerWorkflowRuleList.
     * 
     * @return source_value_id
     */
    public int getSource_value_id() {
        return source_value_id;
    }


    /**
     * Sets the source_value_id value for this TrackerWorkflowRuleList.
     * 
     * @param source_value_id
     */
    public void setSource_value_id(int source_value_id) {
        this.source_value_id = source_value_id;
    }


    /**
     * Gets the target_value_id value for this TrackerWorkflowRuleList.
     * 
     * @return target_value_id
     */
    public int getTarget_value_id() {
        return target_value_id;
    }


    /**
     * Sets the target_value_id value for this TrackerWorkflowRuleList.
     * 
     * @param target_value_id
     */
    public void setTarget_value_id(int target_value_id) {
        this.target_value_id = target_value_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerWorkflowRuleList)) return false;
        TrackerWorkflowRuleList other = (TrackerWorkflowRuleList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.source_field_id == other.getSource_field_id() &&
            this.target_field_id == other.getTarget_field_id() &&
            this.source_value_id == other.getSource_value_id() &&
            this.target_value_id == other.getTarget_value_id();
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
        _hashCode += getSource_field_id();
        _hashCode += getTarget_field_id();
        _hashCode += getSource_value_id();
        _hashCode += getTarget_value_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerWorkflowRuleList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source_field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source_field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target_field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "target_field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source_value_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source_value_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target_value_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "target_value_id"));
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

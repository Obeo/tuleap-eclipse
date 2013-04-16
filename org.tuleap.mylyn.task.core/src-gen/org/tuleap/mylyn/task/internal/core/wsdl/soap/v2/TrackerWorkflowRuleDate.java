/**
 * TrackerWorkflowRuleDate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflowRuleDate  implements java.io.Serializable {
    private int source_field_id;

    private int target_field_id;

    private java.lang.String comparator;

    public TrackerWorkflowRuleDate() {
    }

    public TrackerWorkflowRuleDate(
           int source_field_id,
           int target_field_id,
           java.lang.String comparator) {
           this.source_field_id = source_field_id;
           this.target_field_id = target_field_id;
           this.comparator = comparator;
    }


    /**
     * Gets the source_field_id value for this TrackerWorkflowRuleDate.
     * 
     * @return source_field_id
     */
    public int getSource_field_id() {
        return source_field_id;
    }


    /**
     * Sets the source_field_id value for this TrackerWorkflowRuleDate.
     * 
     * @param source_field_id
     */
    public void setSource_field_id(int source_field_id) {
        this.source_field_id = source_field_id;
    }


    /**
     * Gets the target_field_id value for this TrackerWorkflowRuleDate.
     * 
     * @return target_field_id
     */
    public int getTarget_field_id() {
        return target_field_id;
    }


    /**
     * Sets the target_field_id value for this TrackerWorkflowRuleDate.
     * 
     * @param target_field_id
     */
    public void setTarget_field_id(int target_field_id) {
        this.target_field_id = target_field_id;
    }


    /**
     * Gets the comparator value for this TrackerWorkflowRuleDate.
     * 
     * @return comparator
     */
    public java.lang.String getComparator() {
        return comparator;
    }


    /**
     * Sets the comparator value for this TrackerWorkflowRuleDate.
     * 
     * @param comparator
     */
    public void setComparator(java.lang.String comparator) {
        this.comparator = comparator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerWorkflowRuleDate)) return false;
        TrackerWorkflowRuleDate other = (TrackerWorkflowRuleDate) obj;
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
            ((this.comparator==null && other.getComparator()==null) || 
             (this.comparator!=null &&
              this.comparator.equals(other.getComparator())));
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
        if (getComparator() != null) {
            _hashCode += getComparator().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerWorkflowRuleDate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleDate"));
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
        elemField.setFieldName("comparator");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comparator"));
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

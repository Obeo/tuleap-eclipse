/**
 * TrackerWorkflowTransition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflowTransition  implements java.io.Serializable {
    private int from_id;

    private int to_id;

    public TrackerWorkflowTransition() {
    }

    public TrackerWorkflowTransition(
           int from_id,
           int to_id) {
           this.from_id = from_id;
           this.to_id = to_id;
    }


    /**
     * Gets the from_id value for this TrackerWorkflowTransition.
     * 
     * @return from_id
     */
    public int getFrom_id() {
        return from_id;
    }


    /**
     * Sets the from_id value for this TrackerWorkflowTransition.
     * 
     * @param from_id
     */
    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }


    /**
     * Gets the to_id value for this TrackerWorkflowTransition.
     * 
     * @return to_id
     */
    public int getTo_id() {
        return to_id;
    }


    /**
     * Sets the to_id value for this TrackerWorkflowTransition.
     * 
     * @param to_id
     */
    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerWorkflowTransition)) return false;
        TrackerWorkflowTransition other = (TrackerWorkflowTransition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.from_id == other.getFrom_id() &&
            this.to_id == other.getTo_id();
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
        _hashCode += getFrom_id();
        _hashCode += getTo_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerWorkflowTransition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerWorkflowTransition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("from_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "from_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("to_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "to_id"));
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

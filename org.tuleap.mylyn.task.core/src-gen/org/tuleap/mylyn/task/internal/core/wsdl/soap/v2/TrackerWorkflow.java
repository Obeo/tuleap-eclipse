/**
 * TrackerWorkflow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflow  implements java.io.Serializable {
    private int field_id;

    private int is_used;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowRuleArray rules;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowTransition[] transitions;

    public TrackerWorkflow() {
    }

    public TrackerWorkflow(
           int field_id,
           int is_used,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowRuleArray rules,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowTransition[] transitions) {
           this.field_id = field_id;
           this.is_used = is_used;
           this.rules = rules;
           this.transitions = transitions;
    }


    /**
     * Gets the field_id value for this TrackerWorkflow.
     * 
     * @return field_id
     */
    public int getField_id() {
        return field_id;
    }


    /**
     * Sets the field_id value for this TrackerWorkflow.
     * 
     * @param field_id
     */
    public void setField_id(int field_id) {
        this.field_id = field_id;
    }


    /**
     * Gets the is_used value for this TrackerWorkflow.
     * 
     * @return is_used
     */
    public int getIs_used() {
        return is_used;
    }


    /**
     * Sets the is_used value for this TrackerWorkflow.
     * 
     * @param is_used
     */
    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }


    /**
     * Gets the rules value for this TrackerWorkflow.
     * 
     * @return rules
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowRuleArray getRules() {
        return rules;
    }


    /**
     * Sets the rules value for this TrackerWorkflow.
     * 
     * @param rules
     */
    public void setRules(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowRuleArray rules) {
        this.rules = rules;
    }


    /**
     * Gets the transitions value for this TrackerWorkflow.
     * 
     * @return transitions
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowTransition[] getTransitions() {
        return transitions;
    }


    /**
     * Sets the transitions value for this TrackerWorkflow.
     * 
     * @param transitions
     */
    public void setTransitions(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflowTransition[] transitions) {
        this.transitions = transitions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerWorkflow)) return false;
        TrackerWorkflow other = (TrackerWorkflow) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.field_id == other.getField_id() &&
            this.is_used == other.getIs_used() &&
            ((this.rules==null && other.getRules()==null) || 
             (this.rules!=null &&
              this.rules.equals(other.getRules()))) &&
            ((this.transitions==null && other.getTransitions()==null) || 
             (this.transitions!=null &&
              java.util.Arrays.equals(this.transitions, other.getTransitions())));
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
        _hashCode += getField_id();
        _hashCode += getIs_used();
        if (getRules() != null) {
            _hashCode += getRules().hashCode();
        }
        if (getTransitions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransitions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransitions(), i);
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
        new org.apache.axis.description.TypeDesc(TrackerWorkflow.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflow"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("is_used");
        elemField.setXmlName(new javax.xml.namespace.QName("", "is_used"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rules");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rules"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleArray"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transitions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transitions"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflowTransition"));
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

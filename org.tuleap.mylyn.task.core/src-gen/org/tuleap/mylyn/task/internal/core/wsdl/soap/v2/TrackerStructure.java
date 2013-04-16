/**
 * TrackerStructure.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerStructure  implements java.io.Serializable {
    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic semantic;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflow workflow;

    public TrackerStructure() {
    }

    public TrackerStructure(
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic semantic,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflow workflow) {
           this.semantic = semantic;
           this.workflow = workflow;
    }


    /**
     * Gets the semantic value for this TrackerStructure.
     * 
     * @return semantic
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic getSemantic() {
        return semantic;
    }


    /**
     * Sets the semantic value for this TrackerStructure.
     * 
     * @param semantic
     */
    public void setSemantic(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemantic semantic) {
        this.semantic = semantic;
    }


    /**
     * Gets the workflow value for this TrackerStructure.
     * 
     * @return workflow
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflow getWorkflow() {
        return workflow;
    }


    /**
     * Sets the workflow value for this TrackerStructure.
     * 
     * @param workflow
     */
    public void setWorkflow(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerWorkflow workflow) {
        this.workflow = workflow;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerStructure)) return false;
        TrackerStructure other = (TrackerStructure) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.semantic==null && other.getSemantic()==null) || 
             (this.semantic!=null &&
              this.semantic.equals(other.getSemantic()))) &&
            ((this.workflow==null && other.getWorkflow()==null) || 
             (this.workflow!=null &&
              this.workflow.equals(other.getWorkflow())));
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
        if (getSemantic() != null) {
            _hashCode += getSemantic().hashCode();
        }
        if (getWorkflow() != null) {
            _hashCode += getWorkflow().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerStructure.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerStructure"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("semantic");
        elemField.setXmlName(new javax.xml.namespace.QName("", "semantic"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerSemantic"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "TrackerWorkflow"));
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

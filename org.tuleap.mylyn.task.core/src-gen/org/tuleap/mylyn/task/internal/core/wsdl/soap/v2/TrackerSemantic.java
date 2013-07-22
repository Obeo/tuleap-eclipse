/**
 * TrackerSemantic.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerSemantic  implements java.io.Serializable {
    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle title;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus status;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor contributor;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.AgileDashBoardSemanticInitialEffort initial_effort;

    public TrackerSemantic() {
    }

    public TrackerSemantic(
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle title,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus status,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor contributor,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.AgileDashBoardSemanticInitialEffort initial_effort) {
           this.title = title;
           this.status = status;
           this.contributor = contributor;
           this.initial_effort = initial_effort;
    }


    /**
     * Gets the title value for this TrackerSemantic.
     * 
     * @return title
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle getTitle() {
        return title;
    }


    /**
     * Sets the title value for this TrackerSemantic.
     * 
     * @param title
     */
    public void setTitle(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticTitle title) {
        this.title = title;
    }


    /**
     * Gets the status value for this TrackerSemantic.
     * 
     * @return status
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TrackerSemantic.
     * 
     * @param status
     */
    public void setStatus(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticStatus status) {
        this.status = status;
    }


    /**
     * Gets the contributor value for this TrackerSemantic.
     * 
     * @return contributor
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor getContributor() {
        return contributor;
    }


    /**
     * Sets the contributor value for this TrackerSemantic.
     * 
     * @param contributor
     */
    public void setContributor(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerSemanticContributor contributor) {
        this.contributor = contributor;
    }


    /**
     * Gets the initial_effort value for this TrackerSemantic.
     * 
     * @return initial_effort
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.AgileDashBoardSemanticInitialEffort getInitial_effort() {
        return initial_effort;
    }


    /**
     * Sets the initial_effort value for this TrackerSemantic.
     * 
     * @param initial_effort
     */
    public void setInitial_effort(org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.AgileDashBoardSemanticInitialEffort initial_effort) {
        this.initial_effort = initial_effort;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerSemantic)) return false;
        TrackerSemantic other = (TrackerSemantic) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.contributor==null && other.getContributor()==null) || 
             (this.contributor!=null &&
              this.contributor.equals(other.getContributor()))) &&
            ((this.initial_effort==null && other.getInitial_effort()==null) || 
             (this.initial_effort!=null &&
              this.initial_effort.equals(other.getInitial_effort())));
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
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getContributor() != null) {
            _hashCode += getContributor().hashCode();
        }
        if (getInitial_effort() != null) {
            _hashCode += getInitial_effort().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrackerSemantic.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerSemantic"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerSemanticTitle"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerSemanticStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contributor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contributor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerSemanticContributor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initial_effort");
        elemField.setXmlName(new javax.xml.namespace.QName("", "initial_effort"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "AgileDashBoardSemanticInitialEffort"));
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

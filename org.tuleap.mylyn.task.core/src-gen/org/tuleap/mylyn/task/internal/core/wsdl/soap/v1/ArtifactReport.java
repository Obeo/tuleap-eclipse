/**
 * ArtifactReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactReport  implements java.io.Serializable {
    private int report_id;

    private int group_artifact_id;

    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String scope;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactReportField[] fields;

    public ArtifactReport() {
    }

    public ArtifactReport(
           int report_id,
           int group_artifact_id,
           java.lang.String name,
           java.lang.String description,
           java.lang.String scope,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactReportField[] fields) {
           this.report_id = report_id;
           this.group_artifact_id = group_artifact_id;
           this.name = name;
           this.description = description;
           this.scope = scope;
           this.fields = fields;
    }


    /**
     * Gets the report_id value for this ArtifactReport.
     * 
     * @return report_id
     */
    public int getReport_id() {
        return report_id;
    }


    /**
     * Sets the report_id value for this ArtifactReport.
     * 
     * @param report_id
     */
    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }


    /**
     * Gets the group_artifact_id value for this ArtifactReport.
     * 
     * @return group_artifact_id
     */
    public int getGroup_artifact_id() {
        return group_artifact_id;
    }


    /**
     * Sets the group_artifact_id value for this ArtifactReport.
     * 
     * @param group_artifact_id
     */
    public void setGroup_artifact_id(int group_artifact_id) {
        this.group_artifact_id = group_artifact_id;
    }


    /**
     * Gets the name value for this ArtifactReport.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ArtifactReport.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this ArtifactReport.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ArtifactReport.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the scope value for this ArtifactReport.
     * 
     * @return scope
     */
    public java.lang.String getScope() {
        return scope;
    }


    /**
     * Sets the scope value for this ArtifactReport.
     * 
     * @param scope
     */
    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }


    /**
     * Gets the fields value for this ArtifactReport.
     * 
     * @return fields
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactReportField[] getFields() {
        return fields;
    }


    /**
     * Sets the fields value for this ArtifactReport.
     * 
     * @param fields
     */
    public void setFields(org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactReportField[] fields) {
        this.fields = fields;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactReport)) return false;
        ArtifactReport other = (ArtifactReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.report_id == other.getReport_id() &&
            this.group_artifact_id == other.getGroup_artifact_id() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.scope==null && other.getScope()==null) || 
             (this.scope!=null &&
              this.scope.equals(other.getScope()))) &&
            ((this.fields==null && other.getFields()==null) || 
             (this.fields!=null &&
              java.util.Arrays.equals(this.fields, other.getFields())));
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
        _hashCode += getReport_id();
        _hashCode += getGroup_artifact_id();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getScope() != null) {
            _hashCode += getScope().hashCode();
        }
        if (getFields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFields());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFields(), i);
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
        new org.apache.axis.description.TypeDesc(ArtifactReport.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactReport"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("report_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "report_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_artifact_id"));
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
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scope");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scope"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fields");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fields"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactReportField"));
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

/**
 * Artifact.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class Artifact  implements java.io.Serializable {
    private int artifact_id;

    private int group_artifact_id;

    private int status_id;

    private int submitted_by;

    private int open_date;

    private int close_date;

    private int last_update_date;

    private java.lang.String summary;

    private java.lang.String details;

    private int severity;

    private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] extra_fields;

    public Artifact() {
    }

    public Artifact(
           int artifact_id,
           int group_artifact_id,
           int status_id,
           int submitted_by,
           int open_date,
           int close_date,
           int last_update_date,
           java.lang.String summary,
           java.lang.String details,
           int severity,
           org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] extra_fields) {
           this.artifact_id = artifact_id;
           this.group_artifact_id = group_artifact_id;
           this.status_id = status_id;
           this.submitted_by = submitted_by;
           this.open_date = open_date;
           this.close_date = close_date;
           this.last_update_date = last_update_date;
           this.summary = summary;
           this.details = details;
           this.severity = severity;
           this.extra_fields = extra_fields;
    }


    /**
     * Gets the artifact_id value for this Artifact.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this Artifact.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the group_artifact_id value for this Artifact.
     * 
     * @return group_artifact_id
     */
    public int getGroup_artifact_id() {
        return group_artifact_id;
    }


    /**
     * Sets the group_artifact_id value for this Artifact.
     * 
     * @param group_artifact_id
     */
    public void setGroup_artifact_id(int group_artifact_id) {
        this.group_artifact_id = group_artifact_id;
    }


    /**
     * Gets the status_id value for this Artifact.
     * 
     * @return status_id
     */
    public int getStatus_id() {
        return status_id;
    }


    /**
     * Sets the status_id value for this Artifact.
     * 
     * @param status_id
     */
    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }


    /**
     * Gets the submitted_by value for this Artifact.
     * 
     * @return submitted_by
     */
    public int getSubmitted_by() {
        return submitted_by;
    }


    /**
     * Sets the submitted_by value for this Artifact.
     * 
     * @param submitted_by
     */
    public void setSubmitted_by(int submitted_by) {
        this.submitted_by = submitted_by;
    }


    /**
     * Gets the open_date value for this Artifact.
     * 
     * @return open_date
     */
    public int getOpen_date() {
        return open_date;
    }


    /**
     * Sets the open_date value for this Artifact.
     * 
     * @param open_date
     */
    public void setOpen_date(int open_date) {
        this.open_date = open_date;
    }


    /**
     * Gets the close_date value for this Artifact.
     * 
     * @return close_date
     */
    public int getClose_date() {
        return close_date;
    }


    /**
     * Sets the close_date value for this Artifact.
     * 
     * @param close_date
     */
    public void setClose_date(int close_date) {
        this.close_date = close_date;
    }


    /**
     * Gets the last_update_date value for this Artifact.
     * 
     * @return last_update_date
     */
    public int getLast_update_date() {
        return last_update_date;
    }


    /**
     * Sets the last_update_date value for this Artifact.
     * 
     * @param last_update_date
     */
    public void setLast_update_date(int last_update_date) {
        this.last_update_date = last_update_date;
    }


    /**
     * Gets the summary value for this Artifact.
     * 
     * @return summary
     */
    public java.lang.String getSummary() {
        return summary;
    }


    /**
     * Sets the summary value for this Artifact.
     * 
     * @param summary
     */
    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }


    /**
     * Gets the details value for this Artifact.
     * 
     * @return details
     */
    public java.lang.String getDetails() {
        return details;
    }


    /**
     * Sets the details value for this Artifact.
     * 
     * @param details
     */
    public void setDetails(java.lang.String details) {
        this.details = details;
    }


    /**
     * Gets the severity value for this Artifact.
     * 
     * @return severity
     */
    public int getSeverity() {
        return severity;
    }


    /**
     * Sets the severity value for this Artifact.
     * 
     * @param severity
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }


    /**
     * Gets the extra_fields value for this Artifact.
     * 
     * @return extra_fields
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] getExtra_fields() {
        return extra_fields;
    }


    /**
     * Sets the extra_fields value for this Artifact.
     * 
     * @param extra_fields
     */
    public void setExtra_fields(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactFieldValue[] extra_fields) {
        this.extra_fields = extra_fields;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Artifact)) return false;
        Artifact other = (Artifact) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.artifact_id == other.getArtifact_id() &&
            this.group_artifact_id == other.getGroup_artifact_id() &&
            this.status_id == other.getStatus_id() &&
            this.submitted_by == other.getSubmitted_by() &&
            this.open_date == other.getOpen_date() &&
            this.close_date == other.getClose_date() &&
            this.last_update_date == other.getLast_update_date() &&
            ((this.summary==null && other.getSummary()==null) || 
             (this.summary!=null &&
              this.summary.equals(other.getSummary()))) &&
            ((this.details==null && other.getDetails()==null) || 
             (this.details!=null &&
              this.details.equals(other.getDetails()))) &&
            this.severity == other.getSeverity() &&
            ((this.extra_fields==null && other.getExtra_fields()==null) || 
             (this.extra_fields!=null &&
              java.util.Arrays.equals(this.extra_fields, other.getExtra_fields())));
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
        _hashCode += getArtifact_id();
        _hashCode += getGroup_artifact_id();
        _hashCode += getStatus_id();
        _hashCode += getSubmitted_by();
        _hashCode += getOpen_date();
        _hashCode += getClose_date();
        _hashCode += getLast_update_date();
        if (getSummary() != null) {
            _hashCode += getSummary().hashCode();
        }
        if (getDetails() != null) {
            _hashCode += getDetails().hashCode();
        }
        _hashCode += getSeverity();
        if (getExtra_fields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtra_fields());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtra_fields(), i);
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
        new org.apache.axis.description.TypeDesc(Artifact.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "Artifact"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_id"));
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
        elemField.setFieldName("status_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("submitted_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("open_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "open_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("close_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "close_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("last_update_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "last_update_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("summary");
        elemField.setXmlName(new javax.xml.namespace.QName("", "summary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("details");
        elemField.setXmlName(new javax.xml.namespace.QName("", "details"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("severity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "severity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extra_fields");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extra_fields"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFieldValue"));
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

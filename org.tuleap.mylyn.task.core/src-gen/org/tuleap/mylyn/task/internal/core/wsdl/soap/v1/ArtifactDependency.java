/**
 * ArtifactDependency.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactDependency  implements java.io.Serializable {
    private int artifact_depend_id;

    private int artifact_id;

    private int is_dependent_on_artifact_id;

    private java.lang.String summary;

    private int tracker_id;

    private java.lang.String tracker_name;

    private int group_id;

    private java.lang.String group_name;

    public ArtifactDependency() {
    }

    public ArtifactDependency(
           int artifact_depend_id,
           int artifact_id,
           int is_dependent_on_artifact_id,
           java.lang.String summary,
           int tracker_id,
           java.lang.String tracker_name,
           int group_id,
           java.lang.String group_name) {
           this.artifact_depend_id = artifact_depend_id;
           this.artifact_id = artifact_id;
           this.is_dependent_on_artifact_id = is_dependent_on_artifact_id;
           this.summary = summary;
           this.tracker_id = tracker_id;
           this.tracker_name = tracker_name;
           this.group_id = group_id;
           this.group_name = group_name;
    }


    /**
     * Gets the artifact_depend_id value for this ArtifactDependency.
     * 
     * @return artifact_depend_id
     */
    public int getArtifact_depend_id() {
        return artifact_depend_id;
    }


    /**
     * Sets the artifact_depend_id value for this ArtifactDependency.
     * 
     * @param artifact_depend_id
     */
    public void setArtifact_depend_id(int artifact_depend_id) {
        this.artifact_depend_id = artifact_depend_id;
    }


    /**
     * Gets the artifact_id value for this ArtifactDependency.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this ArtifactDependency.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the is_dependent_on_artifact_id value for this ArtifactDependency.
     * 
     * @return is_dependent_on_artifact_id
     */
    public int getIs_dependent_on_artifact_id() {
        return is_dependent_on_artifact_id;
    }


    /**
     * Sets the is_dependent_on_artifact_id value for this ArtifactDependency.
     * 
     * @param is_dependent_on_artifact_id
     */
    public void setIs_dependent_on_artifact_id(int is_dependent_on_artifact_id) {
        this.is_dependent_on_artifact_id = is_dependent_on_artifact_id;
    }


    /**
     * Gets the summary value for this ArtifactDependency.
     * 
     * @return summary
     */
    public java.lang.String getSummary() {
        return summary;
    }


    /**
     * Sets the summary value for this ArtifactDependency.
     * 
     * @param summary
     */
    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }


    /**
     * Gets the tracker_id value for this ArtifactDependency.
     * 
     * @return tracker_id
     */
    public int getTracker_id() {
        return tracker_id;
    }


    /**
     * Sets the tracker_id value for this ArtifactDependency.
     * 
     * @param tracker_id
     */
    public void setTracker_id(int tracker_id) {
        this.tracker_id = tracker_id;
    }


    /**
     * Gets the tracker_name value for this ArtifactDependency.
     * 
     * @return tracker_name
     */
    public java.lang.String getTracker_name() {
        return tracker_name;
    }


    /**
     * Sets the tracker_name value for this ArtifactDependency.
     * 
     * @param tracker_name
     */
    public void setTracker_name(java.lang.String tracker_name) {
        this.tracker_name = tracker_name;
    }


    /**
     * Gets the group_id value for this ArtifactDependency.
     * 
     * @return group_id
     */
    public int getGroup_id() {
        return group_id;
    }


    /**
     * Sets the group_id value for this ArtifactDependency.
     * 
     * @param group_id
     */
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }


    /**
     * Gets the group_name value for this ArtifactDependency.
     * 
     * @return group_name
     */
    public java.lang.String getGroup_name() {
        return group_name;
    }


    /**
     * Sets the group_name value for this ArtifactDependency.
     * 
     * @param group_name
     */
    public void setGroup_name(java.lang.String group_name) {
        this.group_name = group_name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactDependency)) return false;
        ArtifactDependency other = (ArtifactDependency) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.artifact_depend_id == other.getArtifact_depend_id() &&
            this.artifact_id == other.getArtifact_id() &&
            this.is_dependent_on_artifact_id == other.getIs_dependent_on_artifact_id() &&
            ((this.summary==null && other.getSummary()==null) || 
             (this.summary!=null &&
              this.summary.equals(other.getSummary()))) &&
            this.tracker_id == other.getTracker_id() &&
            ((this.tracker_name==null && other.getTracker_name()==null) || 
             (this.tracker_name!=null &&
              this.tracker_name.equals(other.getTracker_name()))) &&
            this.group_id == other.getGroup_id() &&
            ((this.group_name==null && other.getGroup_name()==null) || 
             (this.group_name!=null &&
              this.group_name.equals(other.getGroup_name())));
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
        _hashCode += getArtifact_depend_id();
        _hashCode += getArtifact_id();
        _hashCode += getIs_dependent_on_artifact_id();
        if (getSummary() != null) {
            _hashCode += getSummary().hashCode();
        }
        _hashCode += getTracker_id();
        if (getTracker_name() != null) {
            _hashCode += getTracker_name().hashCode();
        }
        _hashCode += getGroup_id();
        if (getGroup_name() != null) {
            _hashCode += getGroup_name().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactDependency.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "ArtifactDependency"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_depend_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_depend_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("is_dependent_on_artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "is_dependent_on_artifact_id"));
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
        elemField.setFieldName("tracker_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tracker_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tracker_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tracker_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_name"));
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

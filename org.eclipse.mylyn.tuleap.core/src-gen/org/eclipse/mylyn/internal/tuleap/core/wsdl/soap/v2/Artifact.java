/**
 * Artifact.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class Artifact  implements java.io.Serializable {
    private int artifact_id;

    private int tracker_id;

    private int submitted_by;

    private int submitted_on;

    private int last_update_date;

    private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value;

    public Artifact() {
    }

    public Artifact(
           int artifact_id,
           int tracker_id,
           int submitted_by,
           int submitted_on,
           int last_update_date,
           org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value) {
           this.artifact_id = artifact_id;
           this.tracker_id = tracker_id;
           this.submitted_by = submitted_by;
           this.submitted_on = submitted_on;
           this.last_update_date = last_update_date;
           this.value = value;
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
     * Gets the tracker_id value for this Artifact.
     * 
     * @return tracker_id
     */
    public int getTracker_id() {
        return tracker_id;
    }


    /**
     * Sets the tracker_id value for this Artifact.
     * 
     * @param tracker_id
     */
    public void setTracker_id(int tracker_id) {
        this.tracker_id = tracker_id;
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
     * Gets the submitted_on value for this Artifact.
     * 
     * @return submitted_on
     */
    public int getSubmitted_on() {
        return submitted_on;
    }


    /**
     * Sets the submitted_on value for this Artifact.
     * 
     * @param submitted_on
     */
    public void setSubmitted_on(int submitted_on) {
        this.submitted_on = submitted_on;
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
     * Gets the value value for this Artifact.
     * 
     * @return value
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] getValue() {
        return value;
    }


    /**
     * Sets the value value for this Artifact.
     * 
     * @param value
     */
    public void setValue(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[] value) {
        this.value = value;
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
            this.tracker_id == other.getTracker_id() &&
            this.submitted_by == other.getSubmitted_by() &&
            this.submitted_on == other.getSubmitted_on() &&
            this.last_update_date == other.getLast_update_date() &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              java.util.Arrays.equals(this.value, other.getValue())));
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
        _hashCode += getTracker_id();
        _hashCode += getSubmitted_by();
        _hashCode += getSubmitted_on();
        _hashCode += getLast_update_date();
        if (getValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
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
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "Artifact"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tracker_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tracker_id"));
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
        elemField.setFieldName("submitted_on");
        elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_on"));
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
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net/plugins/tracker/soap", "ArtifactFieldValue"));
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

/**
 * ArtifactCC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactCC  implements java.io.Serializable {
    private int artifact_cc_id;

    private int artifact_id;

    private java.lang.String email;

    private int added_by;

    private java.lang.String added_by_name;

    private java.lang.String comment;

    private int date;

    public ArtifactCC() {
    }

    public ArtifactCC(
           int artifact_cc_id,
           int artifact_id,
           java.lang.String email,
           int added_by,
           java.lang.String added_by_name,
           java.lang.String comment,
           int date) {
           this.artifact_cc_id = artifact_cc_id;
           this.artifact_id = artifact_id;
           this.email = email;
           this.added_by = added_by;
           this.added_by_name = added_by_name;
           this.comment = comment;
           this.date = date;
    }


    /**
     * Gets the artifact_cc_id value for this ArtifactCC.
     * 
     * @return artifact_cc_id
     */
    public int getArtifact_cc_id() {
        return artifact_cc_id;
    }


    /**
     * Sets the artifact_cc_id value for this ArtifactCC.
     * 
     * @param artifact_cc_id
     */
    public void setArtifact_cc_id(int artifact_cc_id) {
        this.artifact_cc_id = artifact_cc_id;
    }


    /**
     * Gets the artifact_id value for this ArtifactCC.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this ArtifactCC.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the email value for this ArtifactCC.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this ArtifactCC.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the added_by value for this ArtifactCC.
     * 
     * @return added_by
     */
    public int getAdded_by() {
        return added_by;
    }


    /**
     * Sets the added_by value for this ArtifactCC.
     * 
     * @param added_by
     */
    public void setAdded_by(int added_by) {
        this.added_by = added_by;
    }


    /**
     * Gets the added_by_name value for this ArtifactCC.
     * 
     * @return added_by_name
     */
    public java.lang.String getAdded_by_name() {
        return added_by_name;
    }


    /**
     * Sets the added_by_name value for this ArtifactCC.
     * 
     * @param added_by_name
     */
    public void setAdded_by_name(java.lang.String added_by_name) {
        this.added_by_name = added_by_name;
    }


    /**
     * Gets the comment value for this ArtifactCC.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this ArtifactCC.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }


    /**
     * Gets the date value for this ArtifactCC.
     * 
     * @return date
     */
    public int getDate() {
        return date;
    }


    /**
     * Sets the date value for this ArtifactCC.
     * 
     * @param date
     */
    public void setDate(int date) {
        this.date = date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactCC)) return false;
        ArtifactCC other = (ArtifactCC) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.artifact_cc_id == other.getArtifact_cc_id() &&
            this.artifact_id == other.getArtifact_id() &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            this.added_by == other.getAdded_by() &&
            ((this.added_by_name==null && other.getAdded_by_name()==null) || 
             (this.added_by_name!=null &&
              this.added_by_name.equals(other.getAdded_by_name()))) &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              this.comment.equals(other.getComment()))) &&
            this.date == other.getDate();
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
        _hashCode += getArtifact_cc_id();
        _hashCode += getArtifact_id();
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        _hashCode += getAdded_by();
        if (getAdded_by_name() != null) {
            _hashCode += getAdded_by_name().hashCode();
        }
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        _hashCode += getDate();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactCC.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactCC"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_cc_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_cc_id"));
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
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("added_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "added_by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("added_by_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "added_by_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
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

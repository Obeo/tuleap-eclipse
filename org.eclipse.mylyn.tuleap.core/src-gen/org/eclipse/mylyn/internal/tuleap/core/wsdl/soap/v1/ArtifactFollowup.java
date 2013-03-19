/**
 * ArtifactFollowup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactFollowup  implements java.io.Serializable {
    private int artifact_id;

    private int follow_up_id;

    private java.lang.String comment;

    private int date;

    private int original_date;

    private java.lang.String by;

    private java.lang.String original_by;

    private int comment_type_id;

    private java.lang.String comment_type;

    private java.lang.String field_name;

    private int user_can_edit;

    public ArtifactFollowup() {
    }

    public ArtifactFollowup(
           int artifact_id,
           int follow_up_id,
           java.lang.String comment,
           int date,
           int original_date,
           java.lang.String by,
           java.lang.String original_by,
           int comment_type_id,
           java.lang.String comment_type,
           java.lang.String field_name,
           int user_can_edit) {
           this.artifact_id = artifact_id;
           this.follow_up_id = follow_up_id;
           this.comment = comment;
           this.date = date;
           this.original_date = original_date;
           this.by = by;
           this.original_by = original_by;
           this.comment_type_id = comment_type_id;
           this.comment_type = comment_type;
           this.field_name = field_name;
           this.user_can_edit = user_can_edit;
    }


    /**
     * Gets the artifact_id value for this ArtifactFollowup.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this ArtifactFollowup.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the follow_up_id value for this ArtifactFollowup.
     * 
     * @return follow_up_id
     */
    public int getFollow_up_id() {
        return follow_up_id;
    }


    /**
     * Sets the follow_up_id value for this ArtifactFollowup.
     * 
     * @param follow_up_id
     */
    public void setFollow_up_id(int follow_up_id) {
        this.follow_up_id = follow_up_id;
    }


    /**
     * Gets the comment value for this ArtifactFollowup.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this ArtifactFollowup.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }


    /**
     * Gets the date value for this ArtifactFollowup.
     * 
     * @return date
     */
    public int getDate() {
        return date;
    }


    /**
     * Sets the date value for this ArtifactFollowup.
     * 
     * @param date
     */
    public void setDate(int date) {
        this.date = date;
    }


    /**
     * Gets the original_date value for this ArtifactFollowup.
     * 
     * @return original_date
     */
    public int getOriginal_date() {
        return original_date;
    }


    /**
     * Sets the original_date value for this ArtifactFollowup.
     * 
     * @param original_date
     */
    public void setOriginal_date(int original_date) {
        this.original_date = original_date;
    }


    /**
     * Gets the by value for this ArtifactFollowup.
     * 
     * @return by
     */
    public java.lang.String getBy() {
        return by;
    }


    /**
     * Sets the by value for this ArtifactFollowup.
     * 
     * @param by
     */
    public void setBy(java.lang.String by) {
        this.by = by;
    }


    /**
     * Gets the original_by value for this ArtifactFollowup.
     * 
     * @return original_by
     */
    public java.lang.String getOriginal_by() {
        return original_by;
    }


    /**
     * Sets the original_by value for this ArtifactFollowup.
     * 
     * @param original_by
     */
    public void setOriginal_by(java.lang.String original_by) {
        this.original_by = original_by;
    }


    /**
     * Gets the comment_type_id value for this ArtifactFollowup.
     * 
     * @return comment_type_id
     */
    public int getComment_type_id() {
        return comment_type_id;
    }


    /**
     * Sets the comment_type_id value for this ArtifactFollowup.
     * 
     * @param comment_type_id
     */
    public void setComment_type_id(int comment_type_id) {
        this.comment_type_id = comment_type_id;
    }


    /**
     * Gets the comment_type value for this ArtifactFollowup.
     * 
     * @return comment_type
     */
    public java.lang.String getComment_type() {
        return comment_type;
    }


    /**
     * Sets the comment_type value for this ArtifactFollowup.
     * 
     * @param comment_type
     */
    public void setComment_type(java.lang.String comment_type) {
        this.comment_type = comment_type;
    }


    /**
     * Gets the field_name value for this ArtifactFollowup.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this ArtifactFollowup.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the user_can_edit value for this ArtifactFollowup.
     * 
     * @return user_can_edit
     */
    public int getUser_can_edit() {
        return user_can_edit;
    }


    /**
     * Sets the user_can_edit value for this ArtifactFollowup.
     * 
     * @param user_can_edit
     */
    public void setUser_can_edit(int user_can_edit) {
        this.user_can_edit = user_can_edit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactFollowup)) return false;
        ArtifactFollowup other = (ArtifactFollowup) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.artifact_id == other.getArtifact_id() &&
            this.follow_up_id == other.getFollow_up_id() &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              this.comment.equals(other.getComment()))) &&
            this.date == other.getDate() &&
            this.original_date == other.getOriginal_date() &&
            ((this.by==null && other.getBy()==null) || 
             (this.by!=null &&
              this.by.equals(other.getBy()))) &&
            ((this.original_by==null && other.getOriginal_by()==null) || 
             (this.original_by!=null &&
              this.original_by.equals(other.getOriginal_by()))) &&
            this.comment_type_id == other.getComment_type_id() &&
            ((this.comment_type==null && other.getComment_type()==null) || 
             (this.comment_type!=null &&
              this.comment_type.equals(other.getComment_type()))) &&
            ((this.field_name==null && other.getField_name()==null) || 
             (this.field_name!=null &&
              this.field_name.equals(other.getField_name()))) &&
            this.user_can_edit == other.getUser_can_edit();
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
        _hashCode += getFollow_up_id();
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        _hashCode += getDate();
        _hashCode += getOriginal_date();
        if (getBy() != null) {
            _hashCode += getBy().hashCode();
        }
        if (getOriginal_by() != null) {
            _hashCode += getOriginal_by().hashCode();
        }
        _hashCode += getComment_type_id();
        if (getComment_type() != null) {
            _hashCode += getComment_type().hashCode();
        }
        if (getField_name() != null) {
            _hashCode += getField_name().hashCode();
        }
        _hashCode += getUser_can_edit();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactFollowup.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFollowup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("follow_up_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "follow_up_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("original_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "original_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("original_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "original_by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comment_type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comment_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_can_edit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_can_edit"));
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

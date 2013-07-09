/**
 * FieldValueFileInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v2;

@SuppressWarnings("all")
public class FieldValueFileInfo  implements java.io.Serializable {
    private java.lang.String id;

    private int submitted_by;

    private java.lang.String description;

    private java.lang.String filename;

    private int filesize;

    private java.lang.String filetype;

    private java.lang.String action;

    public FieldValueFileInfo() {
    }

    public FieldValueFileInfo(
           java.lang.String id,
           int submitted_by,
           java.lang.String description,
           java.lang.String filename,
           int filesize,
           java.lang.String filetype,
           java.lang.String action) {
           this.id = id;
           this.submitted_by = submitted_by;
           this.description = description;
           this.filename = filename;
           this.filesize = filesize;
           this.filetype = filetype;
           this.action = action;
    }


    /**
     * Gets the id value for this FieldValueFileInfo.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this FieldValueFileInfo.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the submitted_by value for this FieldValueFileInfo.
     * 
     * @return submitted_by
     */
    public int getSubmitted_by() {
        return submitted_by;
    }


    /**
     * Sets the submitted_by value for this FieldValueFileInfo.
     * 
     * @param submitted_by
     */
    public void setSubmitted_by(int submitted_by) {
        this.submitted_by = submitted_by;
    }


    /**
     * Gets the description value for this FieldValueFileInfo.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this FieldValueFileInfo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the filename value for this FieldValueFileInfo.
     * 
     * @return filename
     */
    public java.lang.String getFilename() {
        return filename;
    }


    /**
     * Sets the filename value for this FieldValueFileInfo.
     * 
     * @param filename
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }


    /**
     * Gets the filesize value for this FieldValueFileInfo.
     * 
     * @return filesize
     */
    public int getFilesize() {
        return filesize;
    }


    /**
     * Sets the filesize value for this FieldValueFileInfo.
     * 
     * @param filesize
     */
    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }


    /**
     * Gets the filetype value for this FieldValueFileInfo.
     * 
     * @return filetype
     */
    public java.lang.String getFiletype() {
        return filetype;
    }


    /**
     * Sets the filetype value for this FieldValueFileInfo.
     * 
     * @param filetype
     */
    public void setFiletype(java.lang.String filetype) {
        this.filetype = filetype;
    }


    /**
     * Gets the action value for this FieldValueFileInfo.
     * 
     * @return action
     */
    public java.lang.String getAction() {
        return action;
    }


    /**
     * Sets the action value for this FieldValueFileInfo.
     * 
     * @param action
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FieldValueFileInfo)) return false;
        FieldValueFileInfo other = (FieldValueFileInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            this.submitted_by == other.getSubmitted_by() &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.filename==null && other.getFilename()==null) || 
             (this.filename!=null &&
              this.filename.equals(other.getFilename()))) &&
            this.filesize == other.getFilesize() &&
            ((this.filetype==null && other.getFiletype()==null) || 
             (this.filetype!=null &&
              this.filetype.equals(other.getFiletype()))) &&
            ((this.action==null && other.getAction()==null) || 
             (this.action!=null &&
              this.action.equals(other.getAction())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        _hashCode += getSubmitted_by();
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getFilename() != null) {
            _hashCode += getFilename().hashCode();
        }
        _hashCode += getFilesize();
        if (getFiletype() != null) {
            _hashCode += getFiletype().hashCode();
        }
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FieldValueFileInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "FieldValueFileInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("submitted_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_by"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filesize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filesize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filetype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action");
        elemField.setXmlName(new javax.xml.namespace.QName("", "action"));
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

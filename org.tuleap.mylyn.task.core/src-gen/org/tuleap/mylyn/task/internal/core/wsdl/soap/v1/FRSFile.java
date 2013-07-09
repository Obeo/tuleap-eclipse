/**
 * FRSFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class FRSFile  implements java.io.Serializable {
    private int file_id;

    private int release_id;

    private java.lang.String file_name;

    private int file_size;

    private int type_id;

    private int processor_id;

    private int release_time;

    private int post_date;

    private java.lang.String computed_md5;

    private java.lang.String reference_md5;

    private int user_id;

    public FRSFile() {
    }

    public FRSFile(
           int file_id,
           int release_id,
           java.lang.String file_name,
           int file_size,
           int type_id,
           int processor_id,
           int release_time,
           int post_date,
           java.lang.String computed_md5,
           java.lang.String reference_md5,
           int user_id) {
           this.file_id = file_id;
           this.release_id = release_id;
           this.file_name = file_name;
           this.file_size = file_size;
           this.type_id = type_id;
           this.processor_id = processor_id;
           this.release_time = release_time;
           this.post_date = post_date;
           this.computed_md5 = computed_md5;
           this.reference_md5 = reference_md5;
           this.user_id = user_id;
    }


    /**
     * Gets the file_id value for this FRSFile.
     * 
     * @return file_id
     */
    public int getFile_id() {
        return file_id;
    }


    /**
     * Sets the file_id value for this FRSFile.
     * 
     * @param file_id
     */
    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }


    /**
     * Gets the release_id value for this FRSFile.
     * 
     * @return release_id
     */
    public int getRelease_id() {
        return release_id;
    }


    /**
     * Sets the release_id value for this FRSFile.
     * 
     * @param release_id
     */
    public void setRelease_id(int release_id) {
        this.release_id = release_id;
    }


    /**
     * Gets the file_name value for this FRSFile.
     * 
     * @return file_name
     */
    public java.lang.String getFile_name() {
        return file_name;
    }


    /**
     * Sets the file_name value for this FRSFile.
     * 
     * @param file_name
     */
    public void setFile_name(java.lang.String file_name) {
        this.file_name = file_name;
    }


    /**
     * Gets the file_size value for this FRSFile.
     * 
     * @return file_size
     */
    public int getFile_size() {
        return file_size;
    }


    /**
     * Sets the file_size value for this FRSFile.
     * 
     * @param file_size
     */
    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }


    /**
     * Gets the type_id value for this FRSFile.
     * 
     * @return type_id
     */
    public int getType_id() {
        return type_id;
    }


    /**
     * Sets the type_id value for this FRSFile.
     * 
     * @param type_id
     */
    public void setType_id(int type_id) {
        this.type_id = type_id;
    }


    /**
     * Gets the processor_id value for this FRSFile.
     * 
     * @return processor_id
     */
    public int getProcessor_id() {
        return processor_id;
    }


    /**
     * Sets the processor_id value for this FRSFile.
     * 
     * @param processor_id
     */
    public void setProcessor_id(int processor_id) {
        this.processor_id = processor_id;
    }


    /**
     * Gets the release_time value for this FRSFile.
     * 
     * @return release_time
     */
    public int getRelease_time() {
        return release_time;
    }


    /**
     * Sets the release_time value for this FRSFile.
     * 
     * @param release_time
     */
    public void setRelease_time(int release_time) {
        this.release_time = release_time;
    }


    /**
     * Gets the post_date value for this FRSFile.
     * 
     * @return post_date
     */
    public int getPost_date() {
        return post_date;
    }


    /**
     * Sets the post_date value for this FRSFile.
     * 
     * @param post_date
     */
    public void setPost_date(int post_date) {
        this.post_date = post_date;
    }


    /**
     * Gets the computed_md5 value for this FRSFile.
     * 
     * @return computed_md5
     */
    public java.lang.String getComputed_md5() {
        return computed_md5;
    }


    /**
     * Sets the computed_md5 value for this FRSFile.
     * 
     * @param computed_md5
     */
    public void setComputed_md5(java.lang.String computed_md5) {
        this.computed_md5 = computed_md5;
    }


    /**
     * Gets the reference_md5 value for this FRSFile.
     * 
     * @return reference_md5
     */
    public java.lang.String getReference_md5() {
        return reference_md5;
    }


    /**
     * Sets the reference_md5 value for this FRSFile.
     * 
     * @param reference_md5
     */
    public void setReference_md5(java.lang.String reference_md5) {
        this.reference_md5 = reference_md5;
    }


    /**
     * Gets the user_id value for this FRSFile.
     * 
     * @return user_id
     */
    public int getUser_id() {
        return user_id;
    }


    /**
     * Sets the user_id value for this FRSFile.
     * 
     * @param user_id
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FRSFile)) return false;
        FRSFile other = (FRSFile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.file_id == other.getFile_id() &&
            this.release_id == other.getRelease_id() &&
            ((this.file_name==null && other.getFile_name()==null) || 
             (this.file_name!=null &&
              this.file_name.equals(other.getFile_name()))) &&
            this.file_size == other.getFile_size() &&
            this.type_id == other.getType_id() &&
            this.processor_id == other.getProcessor_id() &&
            this.release_time == other.getRelease_time() &&
            this.post_date == other.getPost_date() &&
            ((this.computed_md5==null && other.getComputed_md5()==null) || 
             (this.computed_md5!=null &&
              this.computed_md5.equals(other.getComputed_md5()))) &&
            ((this.reference_md5==null && other.getReference_md5()==null) || 
             (this.reference_md5!=null &&
              this.reference_md5.equals(other.getReference_md5()))) &&
            this.user_id == other.getUser_id();
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
        _hashCode += getFile_id();
        _hashCode += getRelease_id();
        if (getFile_name() != null) {
            _hashCode += getFile_name().hashCode();
        }
        _hashCode += getFile_size();
        _hashCode += getType_id();
        _hashCode += getProcessor_id();
        _hashCode += getRelease_time();
        _hashCode += getPost_date();
        if (getComputed_md5() != null) {
            _hashCode += getComputed_md5().hashCode();
        }
        if (getReference_md5() != null) {
            _hashCode += getReference_md5().hashCode();
        }
        _hashCode += getUser_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FRSFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "FRSFile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "file_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("release_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "release_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "file_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file_size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "file_size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processor_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "processor_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("release_time");
        elemField.setXmlName(new javax.xml.namespace.QName("", "release_time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("post_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "post_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("computed_md5");
        elemField.setXmlName(new javax.xml.namespace.QName("", "computed_md5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference_md5");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reference_md5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_id"));
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

/**
 * ArtifactFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactFile  implements java.io.Serializable {
    private int id;

    private int artifact_id;

    private java.lang.String filename;

    private java.lang.String description;

    private byte[] bin_data;

    private int filesize;

    private java.lang.String filetype;

    private int adddate;

    private java.lang.String submitted_by;

    public ArtifactFile() {
    }

    public ArtifactFile(
           int id,
           int artifact_id,
           java.lang.String filename,
           java.lang.String description,
           byte[] bin_data,
           int filesize,
           java.lang.String filetype,
           int adddate,
           java.lang.String submitted_by) {
           this.id = id;
           this.artifact_id = artifact_id;
           this.filename = filename;
           this.description = description;
           this.bin_data = bin_data;
           this.filesize = filesize;
           this.filetype = filetype;
           this.adddate = adddate;
           this.submitted_by = submitted_by;
    }


    /**
     * Gets the id value for this ArtifactFile.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this ArtifactFile.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the artifact_id value for this ArtifactFile.
     * 
     * @return artifact_id
     */
    public int getArtifact_id() {
        return artifact_id;
    }


    /**
     * Sets the artifact_id value for this ArtifactFile.
     * 
     * @param artifact_id
     */
    public void setArtifact_id(int artifact_id) {
        this.artifact_id = artifact_id;
    }


    /**
     * Gets the filename value for this ArtifactFile.
     * 
     * @return filename
     */
    public java.lang.String getFilename() {
        return filename;
    }


    /**
     * Sets the filename value for this ArtifactFile.
     * 
     * @param filename
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }


    /**
     * Gets the description value for this ArtifactFile.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ArtifactFile.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the bin_data value for this ArtifactFile.
     * 
     * @return bin_data
     */
    public byte[] getBin_data() {
        return bin_data;
    }


    /**
     * Sets the bin_data value for this ArtifactFile.
     * 
     * @param bin_data
     */
    public void setBin_data(byte[] bin_data) {
        this.bin_data = bin_data;
    }


    /**
     * Gets the filesize value for this ArtifactFile.
     * 
     * @return filesize
     */
    public int getFilesize() {
        return filesize;
    }


    /**
     * Sets the filesize value for this ArtifactFile.
     * 
     * @param filesize
     */
    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }


    /**
     * Gets the filetype value for this ArtifactFile.
     * 
     * @return filetype
     */
    public java.lang.String getFiletype() {
        return filetype;
    }


    /**
     * Sets the filetype value for this ArtifactFile.
     * 
     * @param filetype
     */
    public void setFiletype(java.lang.String filetype) {
        this.filetype = filetype;
    }


    /**
     * Gets the adddate value for this ArtifactFile.
     * 
     * @return adddate
     */
    public int getAdddate() {
        return adddate;
    }


    /**
     * Sets the adddate value for this ArtifactFile.
     * 
     * @param adddate
     */
    public void setAdddate(int adddate) {
        this.adddate = adddate;
    }


    /**
     * Gets the submitted_by value for this ArtifactFile.
     * 
     * @return submitted_by
     */
    public java.lang.String getSubmitted_by() {
        return submitted_by;
    }


    /**
     * Sets the submitted_by value for this ArtifactFile.
     * 
     * @param submitted_by
     */
    public void setSubmitted_by(java.lang.String submitted_by) {
        this.submitted_by = submitted_by;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactFile)) return false;
        ArtifactFile other = (ArtifactFile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.artifact_id == other.getArtifact_id() &&
            ((this.filename==null && other.getFilename()==null) || 
             (this.filename!=null &&
              this.filename.equals(other.getFilename()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.bin_data==null && other.getBin_data()==null) || 
             (this.bin_data!=null &&
              java.util.Arrays.equals(this.bin_data, other.getBin_data()))) &&
            this.filesize == other.getFilesize() &&
            ((this.filetype==null && other.getFiletype()==null) || 
             (this.filetype!=null &&
              this.filetype.equals(other.getFiletype()))) &&
            this.adddate == other.getAdddate() &&
            ((this.submitted_by==null && other.getSubmitted_by()==null) || 
             (this.submitted_by!=null &&
              this.submitted_by.equals(other.getSubmitted_by())));
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
        _hashCode += getId();
        _hashCode += getArtifact_id();
        if (getFilename() != null) {
            _hashCode += getFilename().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getBin_data() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBin_data());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBin_data(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getFilesize();
        if (getFiletype() != null) {
            _hashCode += getFiletype().hashCode();
        }
        _hashCode += getAdddate();
        if (getSubmitted_by() != null) {
            _hashCode += getSubmitted_by().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "ArtifactFile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
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
        elemField.setFieldName("filename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filename"));
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
        elemField.setFieldName("bin_data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bin_data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
        elemField.setFieldName("adddate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adddate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("submitted_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "submitted_by"));
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

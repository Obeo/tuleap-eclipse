/**
 * FRSRelease.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class FRSRelease  implements java.io.Serializable {
    private int release_id;

    private int package_id;

    private java.lang.String name;

    private java.lang.String notes;

    private java.lang.String changes;

    private java.lang.String status_id;

    private int release_date;

    private java.lang.String released_by;

    public FRSRelease() {
    }

    public FRSRelease(
           int release_id,
           int package_id,
           java.lang.String name,
           java.lang.String notes,
           java.lang.String changes,
           java.lang.String status_id,
           int release_date,
           java.lang.String released_by) {
           this.release_id = release_id;
           this.package_id = package_id;
           this.name = name;
           this.notes = notes;
           this.changes = changes;
           this.status_id = status_id;
           this.release_date = release_date;
           this.released_by = released_by;
    }


    /**
     * Gets the release_id value for this FRSRelease.
     * 
     * @return release_id
     */
    public int getRelease_id() {
        return release_id;
    }


    /**
     * Sets the release_id value for this FRSRelease.
     * 
     * @param release_id
     */
    public void setRelease_id(int release_id) {
        this.release_id = release_id;
    }


    /**
     * Gets the package_id value for this FRSRelease.
     * 
     * @return package_id
     */
    public int getPackage_id() {
        return package_id;
    }


    /**
     * Sets the package_id value for this FRSRelease.
     * 
     * @param package_id
     */
    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }


    /**
     * Gets the name value for this FRSRelease.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FRSRelease.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the notes value for this FRSRelease.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this FRSRelease.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }


    /**
     * Gets the changes value for this FRSRelease.
     * 
     * @return changes
     */
    public java.lang.String getChanges() {
        return changes;
    }


    /**
     * Sets the changes value for this FRSRelease.
     * 
     * @param changes
     */
    public void setChanges(java.lang.String changes) {
        this.changes = changes;
    }


    /**
     * Gets the status_id value for this FRSRelease.
     * 
     * @return status_id
     */
    public java.lang.String getStatus_id() {
        return status_id;
    }


    /**
     * Sets the status_id value for this FRSRelease.
     * 
     * @param status_id
     */
    public void setStatus_id(java.lang.String status_id) {
        this.status_id = status_id;
    }


    /**
     * Gets the release_date value for this FRSRelease.
     * 
     * @return release_date
     */
    public int getRelease_date() {
        return release_date;
    }


    /**
     * Sets the release_date value for this FRSRelease.
     * 
     * @param release_date
     */
    public void setRelease_date(int release_date) {
        this.release_date = release_date;
    }


    /**
     * Gets the released_by value for this FRSRelease.
     * 
     * @return released_by
     */
    public java.lang.String getReleased_by() {
        return released_by;
    }


    /**
     * Sets the released_by value for this FRSRelease.
     * 
     * @param released_by
     */
    public void setReleased_by(java.lang.String released_by) {
        this.released_by = released_by;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FRSRelease)) return false;
        FRSRelease other = (FRSRelease) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.release_id == other.getRelease_id() &&
            this.package_id == other.getPackage_id() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.notes==null && other.getNotes()==null) || 
             (this.notes!=null &&
              this.notes.equals(other.getNotes()))) &&
            ((this.changes==null && other.getChanges()==null) || 
             (this.changes!=null &&
              this.changes.equals(other.getChanges()))) &&
            ((this.status_id==null && other.getStatus_id()==null) || 
             (this.status_id!=null &&
              this.status_id.equals(other.getStatus_id()))) &&
            this.release_date == other.getRelease_date() &&
            ((this.released_by==null && other.getReleased_by()==null) || 
             (this.released_by!=null &&
              this.released_by.equals(other.getReleased_by())));
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
        _hashCode += getRelease_id();
        _hashCode += getPackage_id();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNotes() != null) {
            _hashCode += getNotes().hashCode();
        }
        if (getChanges() != null) {
            _hashCode += getChanges().hashCode();
        }
        if (getStatus_id() != null) {
            _hashCode += getStatus_id().hashCode();
        }
        _hashCode += getRelease_date();
        if (getReleased_by() != null) {
            _hashCode += getReleased_by().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FRSRelease.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "FRSRelease"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("release_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "release_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("package_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "package_id"));
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
        elemField.setFieldName("notes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "notes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("release_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "release_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("released_by");
        elemField.setXmlName(new javax.xml.namespace.QName("", "released_by"));
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

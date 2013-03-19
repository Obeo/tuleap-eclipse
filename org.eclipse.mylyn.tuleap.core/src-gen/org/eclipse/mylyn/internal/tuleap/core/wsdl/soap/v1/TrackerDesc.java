/**
 * TrackerDesc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class TrackerDesc  implements java.io.Serializable {
    private int group_artifact_id;

    private int group_id;

    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String item_name;

    private int open_count;

    private int total_count;

    private org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactReportDesc[] reports_desc;

    public TrackerDesc() {
    }

    public TrackerDesc(
           int group_artifact_id,
           int group_id,
           java.lang.String name,
           java.lang.String description,
           java.lang.String item_name,
           int open_count,
           int total_count,
           org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactReportDesc[] reports_desc) {
           this.group_artifact_id = group_artifact_id;
           this.group_id = group_id;
           this.name = name;
           this.description = description;
           this.item_name = item_name;
           this.open_count = open_count;
           this.total_count = total_count;
           this.reports_desc = reports_desc;
    }


    /**
     * Gets the group_artifact_id value for this TrackerDesc.
     * 
     * @return group_artifact_id
     */
    public int getGroup_artifact_id() {
        return group_artifact_id;
    }


    /**
     * Sets the group_artifact_id value for this TrackerDesc.
     * 
     * @param group_artifact_id
     */
    public void setGroup_artifact_id(int group_artifact_id) {
        this.group_artifact_id = group_artifact_id;
    }


    /**
     * Gets the group_id value for this TrackerDesc.
     * 
     * @return group_id
     */
    public int getGroup_id() {
        return group_id;
    }


    /**
     * Sets the group_id value for this TrackerDesc.
     * 
     * @param group_id
     */
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }


    /**
     * Gets the name value for this TrackerDesc.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this TrackerDesc.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this TrackerDesc.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this TrackerDesc.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the item_name value for this TrackerDesc.
     * 
     * @return item_name
     */
    public java.lang.String getItem_name() {
        return item_name;
    }


    /**
     * Sets the item_name value for this TrackerDesc.
     * 
     * @param item_name
     */
    public void setItem_name(java.lang.String item_name) {
        this.item_name = item_name;
    }


    /**
     * Gets the open_count value for this TrackerDesc.
     * 
     * @return open_count
     */
    public int getOpen_count() {
        return open_count;
    }


    /**
     * Sets the open_count value for this TrackerDesc.
     * 
     * @param open_count
     */
    public void setOpen_count(int open_count) {
        this.open_count = open_count;
    }


    /**
     * Gets the total_count value for this TrackerDesc.
     * 
     * @return total_count
     */
    public int getTotal_count() {
        return total_count;
    }


    /**
     * Sets the total_count value for this TrackerDesc.
     * 
     * @param total_count
     */
    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }


    /**
     * Gets the reports_desc value for this TrackerDesc.
     * 
     * @return reports_desc
     */
    public org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactReportDesc[] getReports_desc() {
        return reports_desc;
    }


    /**
     * Sets the reports_desc value for this TrackerDesc.
     * 
     * @param reports_desc
     */
    public void setReports_desc(org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v1.ArtifactReportDesc[] reports_desc) {
        this.reports_desc = reports_desc;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerDesc)) return false;
        TrackerDesc other = (TrackerDesc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.group_artifact_id == other.getGroup_artifact_id() &&
            this.group_id == other.getGroup_id() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.item_name==null && other.getItem_name()==null) || 
             (this.item_name!=null &&
              this.item_name.equals(other.getItem_name()))) &&
            this.open_count == other.getOpen_count() &&
            this.total_count == other.getTotal_count() &&
            ((this.reports_desc==null && other.getReports_desc()==null) || 
             (this.reports_desc!=null &&
              java.util.Arrays.equals(this.reports_desc, other.getReports_desc())));
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
        _hashCode += getGroup_artifact_id();
        _hashCode += getGroup_id();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getItem_name() != null) {
            _hashCode += getItem_name().hashCode();
        }
        _hashCode += getOpen_count();
        _hashCode += getTotal_count();
        if (getReports_desc() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReports_desc());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReports_desc(), i);
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
        new org.apache.axis.description.TypeDesc(TrackerDesc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "TrackerDesc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_id"));
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
        elemField.setFieldName("item_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("open_count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "open_count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total_count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "total_count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reports_desc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reports_desc"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactReportDesc"));
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

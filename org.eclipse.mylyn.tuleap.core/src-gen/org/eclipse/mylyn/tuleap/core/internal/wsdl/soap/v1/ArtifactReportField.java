/**
 * ArtifactReportField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactReportField  implements java.io.Serializable {
    private int report_id;

    private java.lang.String field_name;

    private int show_on_query;

    private int show_on_result;

    private int place_query;

    private int place_result;

    private int col_width;

    public ArtifactReportField() {
    }

    public ArtifactReportField(
           int report_id,
           java.lang.String field_name,
           int show_on_query,
           int show_on_result,
           int place_query,
           int place_result,
           int col_width) {
           this.report_id = report_id;
           this.field_name = field_name;
           this.show_on_query = show_on_query;
           this.show_on_result = show_on_result;
           this.place_query = place_query;
           this.place_result = place_result;
           this.col_width = col_width;
    }


    /**
     * Gets the report_id value for this ArtifactReportField.
     * 
     * @return report_id
     */
    public int getReport_id() {
        return report_id;
    }


    /**
     * Sets the report_id value for this ArtifactReportField.
     * 
     * @param report_id
     */
    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }


    /**
     * Gets the field_name value for this ArtifactReportField.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this ArtifactReportField.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the show_on_query value for this ArtifactReportField.
     * 
     * @return show_on_query
     */
    public int getShow_on_query() {
        return show_on_query;
    }


    /**
     * Sets the show_on_query value for this ArtifactReportField.
     * 
     * @param show_on_query
     */
    public void setShow_on_query(int show_on_query) {
        this.show_on_query = show_on_query;
    }


    /**
     * Gets the show_on_result value for this ArtifactReportField.
     * 
     * @return show_on_result
     */
    public int getShow_on_result() {
        return show_on_result;
    }


    /**
     * Sets the show_on_result value for this ArtifactReportField.
     * 
     * @param show_on_result
     */
    public void setShow_on_result(int show_on_result) {
        this.show_on_result = show_on_result;
    }


    /**
     * Gets the place_query value for this ArtifactReportField.
     * 
     * @return place_query
     */
    public int getPlace_query() {
        return place_query;
    }


    /**
     * Sets the place_query value for this ArtifactReportField.
     * 
     * @param place_query
     */
    public void setPlace_query(int place_query) {
        this.place_query = place_query;
    }


    /**
     * Gets the place_result value for this ArtifactReportField.
     * 
     * @return place_result
     */
    public int getPlace_result() {
        return place_result;
    }


    /**
     * Sets the place_result value for this ArtifactReportField.
     * 
     * @param place_result
     */
    public void setPlace_result(int place_result) {
        this.place_result = place_result;
    }


    /**
     * Gets the col_width value for this ArtifactReportField.
     * 
     * @return col_width
     */
    public int getCol_width() {
        return col_width;
    }


    /**
     * Sets the col_width value for this ArtifactReportField.
     * 
     * @param col_width
     */
    public void setCol_width(int col_width) {
        this.col_width = col_width;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactReportField)) return false;
        ArtifactReportField other = (ArtifactReportField) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.report_id == other.getReport_id() &&
            ((this.field_name==null && other.getField_name()==null) || 
             (this.field_name!=null &&
              this.field_name.equals(other.getField_name()))) &&
            this.show_on_query == other.getShow_on_query() &&
            this.show_on_result == other.getShow_on_result() &&
            this.place_query == other.getPlace_query() &&
            this.place_result == other.getPlace_result() &&
            this.col_width == other.getCol_width();
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
        _hashCode += getReport_id();
        if (getField_name() != null) {
            _hashCode += getField_name().hashCode();
        }
        _hashCode += getShow_on_query();
        _hashCode += getShow_on_result();
        _hashCode += getPlace_query();
        _hashCode += getPlace_result();
        _hashCode += getCol_width();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactReportField.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "ArtifactReportField"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("report_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "report_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("show_on_query");
        elemField.setXmlName(new javax.xml.namespace.QName("", "show_on_query"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("show_on_result");
        elemField.setXmlName(new javax.xml.namespace.QName("", "show_on_result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("place_query");
        elemField.setXmlName(new javax.xml.namespace.QName("", "place_query"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("place_result");
        elemField.setXmlName(new javax.xml.namespace.QName("", "place_result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("col_width");
        elemField.setXmlName(new javax.xml.namespace.QName("", "col_width"));
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

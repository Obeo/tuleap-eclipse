/**
 * TrackerWorkflowRuleArray.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2;

@SuppressWarnings("all")
public class TrackerWorkflowRuleArray  implements java.io.Serializable {
    private org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleDate[] dates;

    private org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleList[] lists;

    public TrackerWorkflowRuleArray() {
    }

    public TrackerWorkflowRuleArray(
           org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleDate[] dates,
           org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleList[] lists) {
           this.dates = dates;
           this.lists = lists;
    }


    /**
     * Gets the dates value for this TrackerWorkflowRuleArray.
     * 
     * @return dates
     */
    public org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleDate[] getDates() {
        return dates;
    }


    /**
     * Sets the dates value for this TrackerWorkflowRuleArray.
     * 
     * @param dates
     */
    public void setDates(org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleDate[] dates) {
        this.dates = dates;
    }


    /**
     * Gets the lists value for this TrackerWorkflowRuleArray.
     * 
     * @return lists
     */
    public org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleList[] getLists() {
        return lists;
    }


    /**
     * Sets the lists value for this TrackerWorkflowRuleArray.
     * 
     * @param lists
     */
    public void setLists(org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v2.TrackerWorkflowRuleList[] lists) {
        this.lists = lists;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrackerWorkflowRuleArray)) return false;
        TrackerWorkflowRuleArray other = (TrackerWorkflowRuleArray) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dates==null && other.getDates()==null) || 
             (this.dates!=null &&
              java.util.Arrays.equals(this.dates, other.getDates()))) &&
            ((this.lists==null && other.getLists()==null) || 
             (this.lists!=null &&
              java.util.Arrays.equals(this.lists, other.getLists())));
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
        if (getDates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDates(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLists() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLists());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLists(), i);
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
        new org.apache.axis.description.TypeDesc(TrackerWorkflowRuleArray.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleArray"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dates");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dates"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleDate"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lists");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lists"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net/plugins/tracker/soap", "TrackerWorkflowRuleList"));
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

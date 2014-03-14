/**
 * ArtifactQueryResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactQueryResult  implements java.io.Serializable {
    private int total_artifacts_number;

    private org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1.Artifact[] artifacts;

    public ArtifactQueryResult() {
    }

    public ArtifactQueryResult(
           int total_artifacts_number,
           org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1.Artifact[] artifacts) {
           this.total_artifacts_number = total_artifacts_number;
           this.artifacts = artifacts;
    }


    /**
     * Gets the total_artifacts_number value for this ArtifactQueryResult.
     * 
     * @return total_artifacts_number
     */
    public int getTotal_artifacts_number() {
        return total_artifacts_number;
    }


    /**
     * Sets the total_artifacts_number value for this ArtifactQueryResult.
     * 
     * @param total_artifacts_number
     */
    public void setTotal_artifacts_number(int total_artifacts_number) {
        this.total_artifacts_number = total_artifacts_number;
    }


    /**
     * Gets the artifacts value for this ArtifactQueryResult.
     * 
     * @return artifacts
     */
    public org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1.Artifact[] getArtifacts() {
        return artifacts;
    }


    /**
     * Sets the artifacts value for this ArtifactQueryResult.
     * 
     * @param artifacts
     */
    public void setArtifacts(org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1.Artifact[] artifacts) {
        this.artifacts = artifacts;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactQueryResult)) return false;
        ArtifactQueryResult other = (ArtifactQueryResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.total_artifacts_number == other.getTotal_artifacts_number() &&
            ((this.artifacts==null && other.getArtifacts()==null) || 
             (this.artifacts!=null &&
              java.util.Arrays.equals(this.artifacts, other.getArtifacts())));
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
        _hashCode += getTotal_artifacts_number();
        if (getArtifacts() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getArtifacts());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getArtifacts(), i);
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
        new org.apache.axis.description.TypeDesc(ArtifactQueryResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "ArtifactQueryResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total_artifacts_number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "total_artifacts_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artifacts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artifacts"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "Artifact"));
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

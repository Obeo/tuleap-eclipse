/**
 * ArtifactField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tuleap.mylyn.task.internal.core.wsdl.soap.v1;

@SuppressWarnings("all")
public class ArtifactField  implements java.io.Serializable {
    private int field_id;

    private int group_artifact_id;

    private int field_set_id;

    private java.lang.String field_name;

    private int data_type;

    private java.lang.String display_type;

    private java.lang.String display_size;

    private java.lang.String label;

    private java.lang.String description;

    private java.lang.String scope;

    private int required;

    private int empty_ok;

    private int keep_history;

    private int special;

    private java.lang.String value_function;

    private org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValueList[] available_values;

    private java.lang.String default_value;

    private boolean user_can_submit;

    private boolean user_can_update;

    private boolean user_can_read;

    private boolean is_standard_field;

    public ArtifactField() {
    }

    public ArtifactField(
           int field_id,
           int group_artifact_id,
           int field_set_id,
           java.lang.String field_name,
           int data_type,
           java.lang.String display_type,
           java.lang.String display_size,
           java.lang.String label,
           java.lang.String description,
           java.lang.String scope,
           int required,
           int empty_ok,
           int keep_history,
           int special,
           java.lang.String value_function,
           org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValueList[] available_values,
           java.lang.String default_value,
           boolean user_can_submit,
           boolean user_can_update,
           boolean user_can_read,
           boolean is_standard_field) {
           this.field_id = field_id;
           this.group_artifact_id = group_artifact_id;
           this.field_set_id = field_set_id;
           this.field_name = field_name;
           this.data_type = data_type;
           this.display_type = display_type;
           this.display_size = display_size;
           this.label = label;
           this.description = description;
           this.scope = scope;
           this.required = required;
           this.empty_ok = empty_ok;
           this.keep_history = keep_history;
           this.special = special;
           this.value_function = value_function;
           this.available_values = available_values;
           this.default_value = default_value;
           this.user_can_submit = user_can_submit;
           this.user_can_update = user_can_update;
           this.user_can_read = user_can_read;
           this.is_standard_field = is_standard_field;
    }


    /**
     * Gets the field_id value for this ArtifactField.
     * 
     * @return field_id
     */
    public int getField_id() {
        return field_id;
    }


    /**
     * Sets the field_id value for this ArtifactField.
     * 
     * @param field_id
     */
    public void setField_id(int field_id) {
        this.field_id = field_id;
    }


    /**
     * Gets the group_artifact_id value for this ArtifactField.
     * 
     * @return group_artifact_id
     */
    public int getGroup_artifact_id() {
        return group_artifact_id;
    }


    /**
     * Sets the group_artifact_id value for this ArtifactField.
     * 
     * @param group_artifact_id
     */
    public void setGroup_artifact_id(int group_artifact_id) {
        this.group_artifact_id = group_artifact_id;
    }


    /**
     * Gets the field_set_id value for this ArtifactField.
     * 
     * @return field_set_id
     */
    public int getField_set_id() {
        return field_set_id;
    }


    /**
     * Sets the field_set_id value for this ArtifactField.
     * 
     * @param field_set_id
     */
    public void setField_set_id(int field_set_id) {
        this.field_set_id = field_set_id;
    }


    /**
     * Gets the field_name value for this ArtifactField.
     * 
     * @return field_name
     */
    public java.lang.String getField_name() {
        return field_name;
    }


    /**
     * Sets the field_name value for this ArtifactField.
     * 
     * @param field_name
     */
    public void setField_name(java.lang.String field_name) {
        this.field_name = field_name;
    }


    /**
     * Gets the data_type value for this ArtifactField.
     * 
     * @return data_type
     */
    public int getData_type() {
        return data_type;
    }


    /**
     * Sets the data_type value for this ArtifactField.
     * 
     * @param data_type
     */
    public void setData_type(int data_type) {
        this.data_type = data_type;
    }


    /**
     * Gets the display_type value for this ArtifactField.
     * 
     * @return display_type
     */
    public java.lang.String getDisplay_type() {
        return display_type;
    }


    /**
     * Sets the display_type value for this ArtifactField.
     * 
     * @param display_type
     */
    public void setDisplay_type(java.lang.String display_type) {
        this.display_type = display_type;
    }


    /**
     * Gets the display_size value for this ArtifactField.
     * 
     * @return display_size
     */
    public java.lang.String getDisplay_size() {
        return display_size;
    }


    /**
     * Sets the display_size value for this ArtifactField.
     * 
     * @param display_size
     */
    public void setDisplay_size(java.lang.String display_size) {
        this.display_size = display_size;
    }


    /**
     * Gets the label value for this ArtifactField.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this ArtifactField.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the description value for this ArtifactField.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ArtifactField.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the scope value for this ArtifactField.
     * 
     * @return scope
     */
    public java.lang.String getScope() {
        return scope;
    }


    /**
     * Sets the scope value for this ArtifactField.
     * 
     * @param scope
     */
    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }


    /**
     * Gets the required value for this ArtifactField.
     * 
     * @return required
     */
    public int getRequired() {
        return required;
    }


    /**
     * Sets the required value for this ArtifactField.
     * 
     * @param required
     */
    public void setRequired(int required) {
        this.required = required;
    }


    /**
     * Gets the empty_ok value for this ArtifactField.
     * 
     * @return empty_ok
     */
    public int getEmpty_ok() {
        return empty_ok;
    }


    /**
     * Sets the empty_ok value for this ArtifactField.
     * 
     * @param empty_ok
     */
    public void setEmpty_ok(int empty_ok) {
        this.empty_ok = empty_ok;
    }


    /**
     * Gets the keep_history value for this ArtifactField.
     * 
     * @return keep_history
     */
    public int getKeep_history() {
        return keep_history;
    }


    /**
     * Sets the keep_history value for this ArtifactField.
     * 
     * @param keep_history
     */
    public void setKeep_history(int keep_history) {
        this.keep_history = keep_history;
    }


    /**
     * Gets the special value for this ArtifactField.
     * 
     * @return special
     */
    public int getSpecial() {
        return special;
    }


    /**
     * Sets the special value for this ArtifactField.
     * 
     * @param special
     */
    public void setSpecial(int special) {
        this.special = special;
    }


    /**
     * Gets the value_function value for this ArtifactField.
     * 
     * @return value_function
     */
    public java.lang.String getValue_function() {
        return value_function;
    }


    /**
     * Sets the value_function value for this ArtifactField.
     * 
     * @param value_function
     */
    public void setValue_function(java.lang.String value_function) {
        this.value_function = value_function;
    }


    /**
     * Gets the available_values value for this ArtifactField.
     * 
     * @return available_values
     */
    public org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValueList[] getAvailable_values() {
        return available_values;
    }


    /**
     * Sets the available_values value for this ArtifactField.
     * 
     * @param available_values
     */
    public void setAvailable_values(org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.ArtifactFieldValueList[] available_values) {
        this.available_values = available_values;
    }


    /**
     * Gets the default_value value for this ArtifactField.
     * 
     * @return default_value
     */
    public java.lang.String getDefault_value() {
        return default_value;
    }


    /**
     * Sets the default_value value for this ArtifactField.
     * 
     * @param default_value
     */
    public void setDefault_value(java.lang.String default_value) {
        this.default_value = default_value;
    }


    /**
     * Gets the user_can_submit value for this ArtifactField.
     * 
     * @return user_can_submit
     */
    public boolean isUser_can_submit() {
        return user_can_submit;
    }


    /**
     * Sets the user_can_submit value for this ArtifactField.
     * 
     * @param user_can_submit
     */
    public void setUser_can_submit(boolean user_can_submit) {
        this.user_can_submit = user_can_submit;
    }


    /**
     * Gets the user_can_update value for this ArtifactField.
     * 
     * @return user_can_update
     */
    public boolean isUser_can_update() {
        return user_can_update;
    }


    /**
     * Sets the user_can_update value for this ArtifactField.
     * 
     * @param user_can_update
     */
    public void setUser_can_update(boolean user_can_update) {
        this.user_can_update = user_can_update;
    }


    /**
     * Gets the user_can_read value for this ArtifactField.
     * 
     * @return user_can_read
     */
    public boolean isUser_can_read() {
        return user_can_read;
    }


    /**
     * Sets the user_can_read value for this ArtifactField.
     * 
     * @param user_can_read
     */
    public void setUser_can_read(boolean user_can_read) {
        this.user_can_read = user_can_read;
    }


    /**
     * Gets the is_standard_field value for this ArtifactField.
     * 
     * @return is_standard_field
     */
    public boolean isIs_standard_field() {
        return is_standard_field;
    }


    /**
     * Sets the is_standard_field value for this ArtifactField.
     * 
     * @param is_standard_field
     */
    public void setIs_standard_field(boolean is_standard_field) {
        this.is_standard_field = is_standard_field;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtifactField)) return false;
        ArtifactField other = (ArtifactField) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.field_id == other.getField_id() &&
            this.group_artifact_id == other.getGroup_artifact_id() &&
            this.field_set_id == other.getField_set_id() &&
            ((this.field_name==null && other.getField_name()==null) || 
             (this.field_name!=null &&
              this.field_name.equals(other.getField_name()))) &&
            this.data_type == other.getData_type() &&
            ((this.display_type==null && other.getDisplay_type()==null) || 
             (this.display_type!=null &&
              this.display_type.equals(other.getDisplay_type()))) &&
            ((this.display_size==null && other.getDisplay_size()==null) || 
             (this.display_size!=null &&
              this.display_size.equals(other.getDisplay_size()))) &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.scope==null && other.getScope()==null) || 
             (this.scope!=null &&
              this.scope.equals(other.getScope()))) &&
            this.required == other.getRequired() &&
            this.empty_ok == other.getEmpty_ok() &&
            this.keep_history == other.getKeep_history() &&
            this.special == other.getSpecial() &&
            ((this.value_function==null && other.getValue_function()==null) || 
             (this.value_function!=null &&
              this.value_function.equals(other.getValue_function()))) &&
            ((this.available_values==null && other.getAvailable_values()==null) || 
             (this.available_values!=null &&
              java.util.Arrays.equals(this.available_values, other.getAvailable_values()))) &&
            ((this.default_value==null && other.getDefault_value()==null) || 
             (this.default_value!=null &&
              this.default_value.equals(other.getDefault_value()))) &&
            this.user_can_submit == other.isUser_can_submit() &&
            this.user_can_update == other.isUser_can_update() &&
            this.user_can_read == other.isUser_can_read() &&
            this.is_standard_field == other.isIs_standard_field();
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
        _hashCode += getField_id();
        _hashCode += getGroup_artifact_id();
        _hashCode += getField_set_id();
        if (getField_name() != null) {
            _hashCode += getField_name().hashCode();
        }
        _hashCode += getData_type();
        if (getDisplay_type() != null) {
            _hashCode += getDisplay_type().hashCode();
        }
        if (getDisplay_size() != null) {
            _hashCode += getDisplay_size().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getScope() != null) {
            _hashCode += getScope().hashCode();
        }
        _hashCode += getRequired();
        _hashCode += getEmpty_ok();
        _hashCode += getKeep_history();
        _hashCode += getSpecial();
        if (getValue_function() != null) {
            _hashCode += getValue_function().hashCode();
        }
        if (getAvailable_values() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAvailable_values());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAvailable_values(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDefault_value() != null) {
            _hashCode += getDefault_value().hashCode();
        }
        _hashCode += (isUser_can_submit() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUser_can_update() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUser_can_read() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIs_standard_field() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArtifactField.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactField"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group_artifact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "group_artifact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_set_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "field_set_id"));
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
        elemField.setFieldName("data_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("display_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "display_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("display_size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "display_size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
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
        elemField.setFieldName("scope");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scope"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("required");
        elemField.setXmlName(new javax.xml.namespace.QName("", "required"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empty_ok");
        elemField.setXmlName(new javax.xml.namespace.QName("", "empty_ok"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keep_history");
        elemField.setXmlName(new javax.xml.namespace.QName("", "keep_history"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("special");
        elemField.setXmlName(new javax.xml.namespace.QName("", "special"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value_function");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value_function"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("available_values");
        elemField.setXmlName(new javax.xml.namespace.QName("", "available_values"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://demo.tuleap.net", "ArtifactFieldValueList"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("default_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "default_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_can_submit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_can_submit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_can_update");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_can_update"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_can_read");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_can_read"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("is_standard_field");
        elemField.setXmlName(new javax.xml.namespace.QName("", "is_standard_field"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

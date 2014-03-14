/**
 * Docman_Item.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.eclipse.mylyn.tuleap.core.internal.wsdl.soap.v1;

@SuppressWarnings("all")
public class Docman_Item  implements java.io.Serializable {
    private int item_id;

    private int parent_id;

    private int group_id;

    private java.lang.String title;

    private java.lang.String description;

    private int create_date;

    private int update_date;

    private int delete_date;

    private int user_id;

    private int status;

    private int obsolescence_date;

    private int rank;

    private int item_type;

    public Docman_Item() {
    }

    public Docman_Item(
           int item_id,
           int parent_id,
           int group_id,
           java.lang.String title,
           java.lang.String description,
           int create_date,
           int update_date,
           int delete_date,
           int user_id,
           int status,
           int obsolescence_date,
           int rank,
           int item_type) {
           this.item_id = item_id;
           this.parent_id = parent_id;
           this.group_id = group_id;
           this.title = title;
           this.description = description;
           this.create_date = create_date;
           this.update_date = update_date;
           this.delete_date = delete_date;
           this.user_id = user_id;
           this.status = status;
           this.obsolescence_date = obsolescence_date;
           this.rank = rank;
           this.item_type = item_type;
    }


    /**
     * Gets the item_id value for this Docman_Item.
     * 
     * @return item_id
     */
    public int getItem_id() {
        return item_id;
    }


    /**
     * Sets the item_id value for this Docman_Item.
     * 
     * @param item_id
     */
    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }


    /**
     * Gets the parent_id value for this Docman_Item.
     * 
     * @return parent_id
     */
    public int getParent_id() {
        return parent_id;
    }


    /**
     * Sets the parent_id value for this Docman_Item.
     * 
     * @param parent_id
     */
    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }


    /**
     * Gets the group_id value for this Docman_Item.
     * 
     * @return group_id
     */
    public int getGroup_id() {
        return group_id;
    }


    /**
     * Sets the group_id value for this Docman_Item.
     * 
     * @param group_id
     */
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }


    /**
     * Gets the title value for this Docman_Item.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }


    /**
     * Sets the title value for this Docman_Item.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }


    /**
     * Gets the description value for this Docman_Item.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Docman_Item.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the create_date value for this Docman_Item.
     * 
     * @return create_date
     */
    public int getCreate_date() {
        return create_date;
    }


    /**
     * Sets the create_date value for this Docman_Item.
     * 
     * @param create_date
     */
    public void setCreate_date(int create_date) {
        this.create_date = create_date;
    }


    /**
     * Gets the update_date value for this Docman_Item.
     * 
     * @return update_date
     */
    public int getUpdate_date() {
        return update_date;
    }


    /**
     * Sets the update_date value for this Docman_Item.
     * 
     * @param update_date
     */
    public void setUpdate_date(int update_date) {
        this.update_date = update_date;
    }


    /**
     * Gets the delete_date value for this Docman_Item.
     * 
     * @return delete_date
     */
    public int getDelete_date() {
        return delete_date;
    }


    /**
     * Sets the delete_date value for this Docman_Item.
     * 
     * @param delete_date
     */
    public void setDelete_date(int delete_date) {
        this.delete_date = delete_date;
    }


    /**
     * Gets the user_id value for this Docman_Item.
     * 
     * @return user_id
     */
    public int getUser_id() {
        return user_id;
    }


    /**
     * Sets the user_id value for this Docman_Item.
     * 
     * @param user_id
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    /**
     * Gets the status value for this Docman_Item.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Docman_Item.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Gets the obsolescence_date value for this Docman_Item.
     * 
     * @return obsolescence_date
     */
    public int getObsolescence_date() {
        return obsolescence_date;
    }


    /**
     * Sets the obsolescence_date value for this Docman_Item.
     * 
     * @param obsolescence_date
     */
    public void setObsolescence_date(int obsolescence_date) {
        this.obsolescence_date = obsolescence_date;
    }


    /**
     * Gets the rank value for this Docman_Item.
     * 
     * @return rank
     */
    public int getRank() {
        return rank;
    }


    /**
     * Sets the rank value for this Docman_Item.
     * 
     * @param rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }


    /**
     * Gets the item_type value for this Docman_Item.
     * 
     * @return item_type
     */
    public int getItem_type() {
        return item_type;
    }


    /**
     * Sets the item_type value for this Docman_Item.
     * 
     * @param item_type
     */
    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Docman_Item)) return false;
        Docman_Item other = (Docman_Item) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.item_id == other.getItem_id() &&
            this.parent_id == other.getParent_id() &&
            this.group_id == other.getGroup_id() &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.create_date == other.getCreate_date() &&
            this.update_date == other.getUpdate_date() &&
            this.delete_date == other.getDelete_date() &&
            this.user_id == other.getUser_id() &&
            this.status == other.getStatus() &&
            this.obsolescence_date == other.getObsolescence_date() &&
            this.rank == other.getRank() &&
            this.item_type == other.getItem_type();
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
        _hashCode += getItem_id();
        _hashCode += getParent_id();
        _hashCode += getGroup_id();
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getCreate_date();
        _hashCode += getUpdate_date();
        _hashCode += getDelete_date();
        _hashCode += getUser_id();
        _hashCode += getStatus();
        _hashCode += getObsolescence_date();
        _hashCode += getRank();
        _hashCode += getItem_type();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Docman_Item.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://tuleap.net", "Docman_Item"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parent_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parent_id"));
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
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
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
        elemField.setFieldName("create_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "create_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("update_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "update_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("delete_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "delete_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsolescence_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsolescence_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rank");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rank"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item_type"));
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

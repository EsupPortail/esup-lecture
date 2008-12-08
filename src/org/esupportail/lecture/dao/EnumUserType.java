package org.esupportail.lecture.dao;

import java.io.Serializable; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Types; 
import org.hibernate.HibernateException; 
import org.hibernate.usertype.UserType; 

/**
 * Generic Hibernate UserType for java 5 Enun.
 * This code come from http://www.hibernate.org/265.html
 * @author bourges
 *
 * @param <E>
 */
public class EnumUserType<E extends Enum<E>> implements UserType { 
    
    /**
     * SQL Type used by hibernate.
     */
    private static final int[] SQL_TYPES = {Types.VARCHAR}; 
    
	/**
     * the class wrapped by this UserType.
     */
    private Class<E> clazz = null; 
    
    /**
     * Constructor.
     * @param c 
     */
    protected EnumUserType(final Class<E> c) { 
        this.clazz = c; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() { 
        return SQL_TYPES; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class<E> returnedClass() { 
        return clazz; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(final ResultSet resultSet, final String[] names, @SuppressWarnings("unused") final
	Object owner) throws HibernateException, SQLException { 
        String name = resultSet.getString(names[0]); 
        E result = null; 
        if (!resultSet.wasNull()) { 
            result = Enum.valueOf(clazz, name); 
        } 
        return result; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index)
    throws HibernateException, SQLException { 
        if (null == value) { 
            preparedStatement.setNull(index, Types.VARCHAR); 
        } else { 
            preparedStatement.setString(index, ((Enum) value).name()); 
        } 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException{ 
        return value; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() { 
        return false; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
    public Object assemble(Serializable cached, @SuppressWarnings("unused")
	Object owner) throws HibernateException {
         return cached;
    } 

    /**
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object value) throws HibernateException { 
        return (Serializable)value; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object replace(Object original, @SuppressWarnings("unused")
	Object target, @SuppressWarnings("unused")
	Object owner) throws HibernateException { 
        return original; 
    } 
 
    /**
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    public int hashCode(Object x) throws HibernateException { 
        return x.hashCode(); 
    } 

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException { 
        if (x == y) 
            return true; 
        if (null == x || null == y) 
            return false; 
        return x.equals(y); 
    } 
} 
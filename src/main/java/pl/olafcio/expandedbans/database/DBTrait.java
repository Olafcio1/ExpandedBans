package pl.olafcio.expandedbans.database;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public interface DBTrait {
    Connection connection();
    Statement statement();

    @ApiStatus.Experimental
    @FunctionalInterface
    interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    @ApiStatus.Experimental
    default <T> ArrayList<T> map(Statement statement, Mapper<T> mapper) throws SQLException {
        var res = statement.getResultSet();
        var array = new ArrayList<T>();

        while (res.next())
            array.add(mapper.map(res));

        return array;
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    @SuppressWarnings({"unused", "deprecation"})
    record ResultIterator(Statement statement, ResultSet results) implements AutoCloseable {
        public boolean next() throws SQLException {
            return results.next();
        }

        /**
         * Everything just for this method ☠️🗿🥀
         */
        @Override
        public void close() throws SQLException {
            results.close();
            statement.close();
        }

        public boolean wasNull() throws SQLException {
            return results.wasNull();
        }

        public String getString(int columnIndex) throws SQLException {
            return results.getString(columnIndex);
        }

        public boolean getBoolean(int columnIndex) throws SQLException {
            return results.getBoolean(columnIndex);
        }

        public byte getByte(int columnIndex) throws SQLException {
            return results.getByte(columnIndex);
        }

        public short getShort(int columnIndex) throws SQLException {
            return results.getShort(columnIndex);
        }

        public int getInt(int columnIndex) throws SQLException {
            return results.getInt(columnIndex);
        }

        public long getLong(int columnIndex) throws SQLException {
            return results.getLong(columnIndex);
        }

        public float getFloat(int columnIndex) throws SQLException {
            return results.getFloat(columnIndex);
        }

        public double getDouble(int columnIndex) throws SQLException {
            return results.getDouble(columnIndex);
        }

        public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
            return results.getBigDecimal(columnIndex, scale);
        }

        public byte[] getBytes(int columnIndex) throws SQLException {
            return results.getBytes(columnIndex);
        }

        public Date getDate(int columnIndex) throws SQLException {
            return results.getDate(columnIndex);
        }

        public Time getTime(int columnIndex) throws SQLException {
            return results.getTime(columnIndex);
        }

        public Timestamp getTimestamp(int columnIndex) throws SQLException {
            return results.getTimestamp(columnIndex);
        }

        public InputStream getAsciiStream(int columnIndex) throws SQLException {
            return results.getAsciiStream(columnIndex);
        }

        public InputStream getUnicodeStream(int columnIndex) throws SQLException {
            return results.getUnicodeStream(columnIndex);
        }

        public InputStream getBinaryStream(int columnIndex) throws SQLException {
            return results.getBinaryStream(columnIndex);
        }

        public String getString(String columnLabel) throws SQLException {
            return results.getString(columnLabel);
        }

        public boolean getBoolean(String columnLabel) throws SQLException {
            return results.getBoolean(columnLabel);
        }

        public byte getByte(String columnLabel) throws SQLException {
            return results.getByte(columnLabel);
        }

        public short getShort(String columnLabel) throws SQLException {
            return results.getShort(columnLabel);
        }

        public int getInt(String columnLabel) throws SQLException {
            return results.getInt(columnLabel);
        }

        public long getLong(String columnLabel) throws SQLException {
            return results.getLong(columnLabel);
        }

        public float getFloat(String columnLabel) throws SQLException {
            return results.getFloat(columnLabel);
        }

        public double getDouble(String columnLabel) throws SQLException {
            return results.getDouble(columnLabel);
        }

        public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
            return results.getBigDecimal(columnLabel, scale);
        }

        public byte[] getBytes(String columnLabel) throws SQLException {
            return results.getBytes(columnLabel);
        }

        public Date getDate(String columnLabel) throws SQLException {
            return results.getDate(columnLabel);
        }

        public Time getTime(String columnLabel) throws SQLException {
            return results.getTime(columnLabel);
        }

        public Timestamp getTimestamp(String columnLabel) throws SQLException {
            return results.getTimestamp(columnLabel);
        }

        public InputStream getAsciiStream(String columnLabel) throws SQLException {
            return results.getAsciiStream(columnLabel);
        }

        public InputStream getUnicodeStream(String columnLabel) throws SQLException {
            return results.getUnicodeStream(columnLabel);
        }

        public InputStream getBinaryStream(String columnLabel) throws SQLException {
            return results.getBinaryStream(columnLabel);
        }

        public SQLWarning getWarnings() throws SQLException {
            return results.getWarnings();
        }

        public void clearWarnings() throws SQLException {
            results.clearWarnings();
        }

        public String getCursorName() throws SQLException {
            return results.getCursorName();
        }

        public ResultSetMetaData getMetaData() throws SQLException {
            return results.getMetaData();
        }

        public Object getObject(int columnIndex) throws SQLException {
            return results.getObject(columnIndex);
        }

        public Object getObject(String columnLabel) throws SQLException {
            return results.getObject(columnLabel);
        }

        public int findColumn(String columnLabel) throws SQLException {
            return results.findColumn(columnLabel);
        }

        public Reader getCharacterStream(int columnIndex) throws SQLException {
            return results.getCharacterStream(columnIndex);
        }

        public Reader getCharacterStream(String columnLabel) throws SQLException {
            return results.getCharacterStream(columnLabel);
        }

        public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            return results.getBigDecimal(columnIndex);
        }

        public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
            return results.getBigDecimal(columnLabel);
        }

        public boolean isBeforeFirst() throws SQLException {
            return results.isBeforeFirst();
        }

        public boolean isAfterLast() throws SQLException {
            return results.isAfterLast();
        }

        public boolean isFirst() throws SQLException {
            return results.isFirst();
        }

        public boolean isLast() throws SQLException {
            return results.isLast();
        }

        public void beforeFirst() throws SQLException {
            results.beforeFirst();
        }

        public void afterLast() throws SQLException {
            results.afterLast();
        }

        public boolean first() throws SQLException {
            return results.first();
        }

        public boolean last() throws SQLException {
            return results.last();
        }

        public int getRow() throws SQLException {
            return results.getRow();
        }

        public boolean absolute(int row) throws SQLException {
            return results.absolute(row);
        }

        public boolean relative(int rows) throws SQLException {
            return results.relative(rows);
        }

        public boolean previous() throws SQLException {
            return results.previous();
        }

        public void setFetchDirection(@MagicConstant(intValues = {
                ResultSet.FETCH_FORWARD,
                ResultSet.FETCH_REVERSE,
                ResultSet.FETCH_UNKNOWN
        }) int direction) throws SQLException {
            results.setFetchDirection(direction);
        }

        public int getFetchDirection() throws SQLException {
            return results.getFetchDirection();
        }

        public void setFetchSize(int rows) throws SQLException {
            results.setFetchSize(rows);
        }

        public int getFetchSize() throws SQLException {
            return results.getFetchSize();
        }

        public int getType() throws SQLException {
            return results.getType();
        }

        public int getConcurrency() throws SQLException {
            return results.getConcurrency();
        }

        public boolean rowUpdated() throws SQLException {
            return results.rowUpdated();
        }

        public boolean rowInserted() throws SQLException {
            return results.rowInserted();
        }

        public boolean rowDeleted() throws SQLException {
            return results.rowDeleted();
        }

        public void updateNull(int columnIndex) throws SQLException {
            results.updateNull(columnIndex);
        }

        public void updateBoolean(int columnIndex, boolean x) throws SQLException {
            results.updateBoolean(columnIndex, x);
        }

        public void updateByte(int columnIndex, byte x) throws SQLException {
            results.updateByte(columnIndex, x);
        }

        public void updateShort(int columnIndex, short x) throws SQLException {
            results.updateShort(columnIndex, x);
        }

        public void updateInt(int columnIndex, int x) throws SQLException {
            results.updateInt(columnIndex, x);
        }

        public void updateLong(int columnIndex, long x) throws SQLException {
            results.updateLong(columnIndex, x);
        }

        public void updateFloat(int columnIndex, float x) throws SQLException {
            results.updateFloat(columnIndex, x);
        }

        public void updateDouble(int columnIndex, double x) throws SQLException {
            results.updateDouble(columnIndex, x);
        }

        public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            results.updateBigDecimal(columnIndex, x);
        }

        public void updateString(int columnIndex, String x) throws SQLException {
            results.updateString(columnIndex, x);
        }

        public void updateBytes(int columnIndex, byte[] x) throws SQLException {
            results.updateBytes(columnIndex, x);
        }

        public void updateDate(int columnIndex, Date x) throws SQLException {
            results.updateDate(columnIndex, x);
        }

        public void updateTime(int columnIndex, Time x) throws SQLException {
            results.updateTime(columnIndex, x);
        }

        public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            results.updateTimestamp(columnIndex, x);
        }

        public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            results.updateAsciiStream(columnIndex, x, length);
        }

        public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            results.updateBinaryStream(columnIndex, x, length);
        }

        public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            results.updateCharacterStream(columnIndex, x, length);
        }

        public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
            results.updateObject(columnIndex, x, scaleOrLength);
        }

        public void updateObject(int columnIndex, Object x) throws SQLException {
            results.updateObject(columnIndex, x);
        }

        public void updateNull(String columnLabel) throws SQLException {
            results.updateNull(columnLabel);
        }

        public void updateBoolean(String columnLabel, boolean x) throws SQLException {
            results.updateBoolean(columnLabel, x);
        }

        public void updateByte(String columnLabel, byte x) throws SQLException {
            results.updateByte(columnLabel, x);
        }

        public void updateShort(String columnLabel, short x) throws SQLException {
            results.updateShort(columnLabel, x);
        }

        public void updateInt(String columnLabel, int x) throws SQLException {
            results.updateInt(columnLabel, x);
        }

        public void updateLong(String columnLabel, long x) throws SQLException {
            results.updateLong(columnLabel, x);
        }

        public void updateFloat(String columnLabel, float x) throws SQLException {
            results.updateFloat(columnLabel, x);
        }

        public void updateDouble(String columnLabel, double x) throws SQLException {
            results.updateDouble(columnLabel, x);
        }

        public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
            results.updateBigDecimal(columnLabel, x);
        }

        public void updateString(String columnLabel, String x) throws SQLException {
            results.updateString(columnLabel, x);
        }

        public void updateBytes(String columnLabel, byte[] x) throws SQLException {
            results.updateBytes(columnLabel, x);
        }

        public void updateDate(String columnLabel, Date x) throws SQLException {
            results.updateDate(columnLabel, x);
        }

        public void updateTime(String columnLabel, Time x) throws SQLException {
            results.updateTime(columnLabel, x);
        }

        public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
            results.updateTimestamp(columnLabel, x);
        }

        public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
            results.updateAsciiStream(columnLabel, x, length);
        }

        public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
            results.updateBinaryStream(columnLabel, x, length);
        }

        public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
            results.updateCharacterStream(columnLabel, reader, length);
        }

        public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
            results.updateObject(columnLabel, x, scaleOrLength);
        }

        public void updateObject(String columnLabel, Object x) throws SQLException {
            results.updateObject(columnLabel, x);
        }

        public void insertRow() throws SQLException {
            results.insertRow();
        }

        public void updateRow() throws SQLException {
            results.updateRow();
        }

        public void deleteRow() throws SQLException {
            results.deleteRow();
        }

        public void refreshRow() throws SQLException {
            results.refreshRow();
        }

        public void cancelRowUpdates() throws SQLException {
            results.cancelRowUpdates();
        }

        public void moveToInsertRow() throws SQLException {
            results.moveToInsertRow();
        }

        public void moveToCurrentRow() throws SQLException {
            results.moveToCurrentRow();
        }

        public Statement getStatement() throws SQLException {
            return results.getStatement();
        }

        public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
            return results.getObject(columnIndex, map);
        }

        public Ref getRef(int columnIndex) throws SQLException {
            return results.getRef(columnIndex);
        }

        public Blob getBlob(int columnIndex) throws SQLException {
            return results.getBlob(columnIndex);
        }

        public Clob getClob(int columnIndex) throws SQLException {
            return results.getClob(columnIndex);
        }

        public Array getArray(int columnIndex) throws SQLException {
            return results.getArray(columnIndex);
        }

        public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
            return results.getObject(columnLabel, map);
        }

        public Ref getRef(String columnLabel) throws SQLException {
            return results.getRef(columnLabel);
        }

        public Blob getBlob(String columnLabel) throws SQLException {
            return results.getBlob(columnLabel);
        }

        public Clob getClob(String columnLabel) throws SQLException {
            return results.getClob(columnLabel);
        }

        public Array getArray(String columnLabel) throws SQLException {
            return results.getArray(columnLabel);
        }

        public Date getDate(int columnIndex, Calendar cal) throws SQLException {
            return results.getDate(columnIndex, cal);
        }

        public Date getDate(String columnLabel, Calendar cal) throws SQLException {
            return results.getDate(columnLabel, cal);
        }

        public Time getTime(int columnIndex, Calendar cal) throws SQLException {
            return results.getTime(columnIndex, cal);
        }

        public Time getTime(String columnLabel, Calendar cal) throws SQLException {
            return results.getTime(columnLabel, cal);
        }

        public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            return results.getTimestamp(columnIndex, cal);
        }

        public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
            return results.getTimestamp(columnLabel, cal);
        }

        public URL getURL(int columnIndex) throws SQLException {
            return results.getURL(columnIndex);
        }

        public URL getURL(String columnLabel) throws SQLException {
            return results.getURL(columnLabel);
        }

        public void updateRef(int columnIndex, Ref x) throws SQLException {
            results.updateRef(columnIndex, x);
        }

        public void updateRef(String columnLabel, Ref x) throws SQLException {
            results.updateRef(columnLabel, x);
        }

        public void updateBlob(int columnIndex, Blob x) throws SQLException {
            results.updateBlob(columnIndex, x);
        }

        public void updateBlob(String columnLabel, Blob x) throws SQLException {
            results.updateBlob(columnLabel, x);
        }

        public void updateClob(int columnIndex, Clob x) throws SQLException {
            results.updateClob(columnIndex, x);
        }

        public void updateClob(String columnLabel, Clob x) throws SQLException {
            results.updateClob(columnLabel, x);
        }

        public void updateArray(int columnIndex, Array x) throws SQLException {
            results.updateArray(columnIndex, x);
        }

        public void updateArray(String columnLabel, Array x) throws SQLException {
            results.updateArray(columnLabel, x);
        }

        public RowId getRowId(int columnIndex) throws SQLException {
            return results.getRowId(columnIndex);
        }

        public RowId getRowId(String columnLabel) throws SQLException {
            return results.getRowId(columnLabel);
        }

        public void updateRowId(int columnIndex, RowId x) throws SQLException {
            results.updateRowId(columnIndex, x);
        }

        public void updateRowId(String columnLabel, RowId x) throws SQLException {
            results.updateRowId(columnLabel, x);
        }

        public int getHoldability() throws SQLException {
            return results.getHoldability();
        }

        public boolean isClosed() throws SQLException {
            return results.isClosed();
        }

        public void updateNString(int columnIndex, String nString) throws SQLException {
            results.updateNString(columnIndex, nString);
        }

        public void updateNString(String columnLabel, String nString) throws SQLException {
            results.updateNString(columnLabel, nString);
        }

        public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
            results.updateNClob(columnIndex, nClob);
        }

        public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
            results.updateNClob(columnLabel, nClob);
        }

        public NClob getNClob(int columnIndex) throws SQLException {
            return results.getNClob(columnIndex);
        }

        public NClob getNClob(String columnLabel) throws SQLException {
            return results.getNClob(columnLabel);
        }

        public SQLXML getSQLXML(int columnIndex) throws SQLException {
            return results.getSQLXML(columnIndex);
        }

        public SQLXML getSQLXML(String columnLabel) throws SQLException {
            return results.getSQLXML(columnLabel);
        }

        public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
            results.updateSQLXML(columnIndex, xmlObject);
        }

        public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
            results.updateSQLXML(columnLabel, xmlObject);
        }

        public String getNString(int columnIndex) throws SQLException {
            return results.getNString(columnIndex);
        }

        public String getNString(String columnLabel) throws SQLException {
            return results.getNString(columnLabel);
        }

        public Reader getNCharacterStream(int columnIndex) throws SQLException {
            return results.getNCharacterStream(columnIndex);
        }

        public Reader getNCharacterStream(String columnLabel) throws SQLException {
            return results.getNCharacterStream(columnLabel);
        }

        public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            results.updateNCharacterStream(columnIndex, x, length);
        }

        public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            results.updateNCharacterStream(columnLabel, reader, length);
        }

        public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
            results.updateAsciiStream(columnIndex, x, length);
        }

        public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
            results.updateBinaryStream(columnIndex, x, length);
        }

        public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            results.updateCharacterStream(columnIndex, x, length);
        }

        public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
            results.updateAsciiStream(columnLabel, x, length);
        }

        public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
            results.updateBinaryStream(columnLabel, x, length);
        }

        public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            results.updateCharacterStream(columnLabel, reader, length);
        }

        public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
            results.updateBlob(columnIndex, inputStream, length);
        }

        public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
            results.updateBlob(columnLabel, inputStream, length);
        }

        public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
            results.updateClob(columnIndex, reader, length);
        }

        public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
            results.updateClob(columnLabel, reader, length);
        }

        public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
            results.updateNClob(columnIndex, reader, length);
        }

        public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
            results.updateNClob(columnLabel, reader, length);
        }

        public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
            results.updateNCharacterStream(columnIndex, x);
        }

        public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
            results.updateNCharacterStream(columnLabel, reader);
        }

        public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
            results.updateAsciiStream(columnIndex, x);
        }

        public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
            results.updateBinaryStream(columnIndex, x);
        }

        public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
            results.updateCharacterStream(columnIndex, x);
        }

        public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
            results.updateAsciiStream(columnLabel, x);
        }

        public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
            results.updateBinaryStream(columnLabel, x);
        }

        public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
            results.updateCharacterStream(columnLabel, reader);
        }

        public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
            results.updateBlob(columnIndex, inputStream);
        }

        public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
            results.updateBlob(columnLabel, inputStream);
        }

        public void updateClob(int columnIndex, Reader reader) throws SQLException {
            results.updateClob(columnIndex, reader);
        }

        public void updateClob(String columnLabel, Reader reader) throws SQLException {
            results.updateClob(columnLabel, reader);
        }

        public void updateNClob(int columnIndex, Reader reader) throws SQLException {
            results.updateNClob(columnIndex, reader);
        }

        public void updateNClob(String columnLabel, Reader reader) throws SQLException {
            results.updateNClob(columnLabel, reader);
        }

        public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
            return results.getObject(columnIndex, type);
        }

        public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
            return results.getObject(columnLabel, type);
        }

        public <T> T unwrap(Class<T> iface) throws SQLException {
            return results.unwrap(iface);
        }

        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return results.isWrapperFor(iface);
        }
    }

    @ApiStatus.Experimental
    @FunctionalInterface
    interface ResultSupplier {
        ResultSet get() throws SQLException;
    }

    @ApiStatus.Experimental
    default @NonNull ResultIterator results(Statement statement, ResultSupplier supplier) throws SQLException {
        return new ResultIterator(statement, supplier.get());
    }
}

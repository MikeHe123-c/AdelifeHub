package com.adlifehub.adlife.mybatis;
import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler; import org.apache.ibatis.type.JdbcType;
import java.sql.*; import java.util.ArrayList; import java.util.List;
public class JsonStringListTypeHandler extends BaseTypeHandler<List<String>> {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  @Override public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException { try { ps.setString(i, MAPPER.writeValueAsString(parameter)); } catch (Exception e) { throw new SQLException("serialize list", e); } }
  @Override public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException { return parse(rs.getString(columnName)); }
  @Override public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException { return parse(rs.getString(columnIndex)); }
  @Override public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException { return parse(cs.getString(columnIndex)); }
  private List<String> parse(String s) throws SQLException { if (s==null||s.isEmpty()) return new ArrayList<>(); try { return MAPPER.readValue(s, new TypeReference<List<String>>(){}); } catch (Exception e) { throw new SQLException("parse list json", e); } }
}

package com.rolin.orangesmart.configuration;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.context.annotation.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 将java的list内容，按逗号分隔形式写入数据库；从数据库取出后，再转成list
 */
@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
@Configuration
public class ListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        StringBuilder sb = new StringBuilder();
        parameter.stream().forEach(s -> {
            sb.append(s);
            sb.append(",");
        });
        ps.setString(i, sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "");
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        if (result == null || result.length() == 0) {
            return null;
        }
        return Arrays.asList(result.split(","));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        if (result == null || result.length() == 0) {
            return null;
        }
        return Arrays.asList(result.split(","));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        if (result == null || result.length() == 0) {
            return null;
        }
        return Arrays.asList(result.split(","));
    }

}

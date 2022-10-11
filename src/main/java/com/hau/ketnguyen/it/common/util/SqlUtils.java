package com.hau.ketnguyen.it.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Map;


public class SqlUtils {
    private static final String VN_SOURCE = "'áàảãạăắằẳẵặâấầẩẫậđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬĐÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ'";
    private static final String EN_TARGET = "'aaaaaaaaaaaaaaaaadeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyAAAAAAAAAAAAAAAAADEEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYY'";

    public static String buildOrderBy(Pageable pageable, Map<String, String> sortMapper) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) return "";
        StringBuilder orderBy = new StringBuilder();
        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty() != null) {
                String field = order.getProperty();
                if (sortMapper != null) {
                    field = sortMapper.get(order.getProperty());
                }
                if (StringUtils.isNotBlank(field)) {
                    orderBy.append(field)
                            .append(" ")
                            .append(order.getDirection())
                            .append(",");
                }
            }
        }
        int commaIndex = orderBy.lastIndexOf(",");

        if (commaIndex != -1) {
            orderBy.deleteCharAt(commaIndex);
        }

        return orderBy.toString();
    }

    public static String buildOrderBy(Pageable pageable, String entityAlias, Map<String, String> sortMapper) {
        if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) return "";
        StringBuilder orderBy = new StringBuilder();
        String pref = "";
        if (StringUtils.isNotBlank(entityAlias)) {
            pref = entityAlias.trim() + ".";
        }
        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty() != null) {
                String field = order.getProperty();
                if (sortMapper != null) {
                    field = sortMapper.get(order.getProperty());
                }
                if (StringUtils.isNotBlank(field)) {
                    if (field.equals("name") || field.equals("description")) {
                        orderBy.append("nlssort(")
                                .append(pref)
                                .append(field)
                                .append(",'NLS_SORT = VIETNAMESE')")
                                .append(order.getDirection())
                                .append(",");
                    } else {
                        orderBy.append(pref)
                                .append(field)
                                .append(" ")
                                .append(order.getDirection())
                                .append(",");
                    }
                }
            }
        }
        int commaIndex = orderBy.lastIndexOf(",");

        if (commaIndex != -1) {
            orderBy.deleteCharAt(commaIndex);
        }

        return orderBy.toString();
    }

    public static String likeIgnoreCase(String name) {
        StringBuilder sql = new StringBuilder();
        sql.append("lower(")
                .append(name)
                .append(")")
                .append(" like concat(concat('%', ")
                .append("lower(").append(":").append(name)
                .append(")")
                .append("), '%')");
        return sql.toString();
    }

    public static String likeIgnoreCaseWithObject(String objectField, String parameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("lower(")
                .append(objectField)
                .append(")")
                .append(" like concat(concat('%', ")
                .append("lower(").append(":").append(parameter)
                .append(")")
                .append("), '%')");
        return sql.toString();
    }

    public static String likeIgnoreCaseAndVietnamese(String name) {
        StringBuilder sql = new StringBuilder();
        sql.append("lower(TRANSLATE(")
                .append(name)
                .append(",\n")
                .append(VN_SOURCE)
                .append(",\n")
                .append(EN_TARGET)
                .append("))")
                .append(" like concat(concat('%', ")
                .append("lower(TRANSLATE(").append(":").append(name).append(",\n")
                .append(VN_SOURCE)
                .append(",\n")
                .append(EN_TARGET)
                .append("))")
                .append("), '%')");
        return sql.toString();
    }

    public static String likeIgnoreCaseAndVietnameseWithObject(String objectField, String parameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("lower(TRANSLATE(")
                .append(objectField)
                .append(",\n")
                .append(VN_SOURCE)
                .append(",\n")
                .append(EN_TARGET)
                .append("))")
                .append(" like concat(concat('%', ")
                .append("lower(TRANSLATE(").append(":").append(parameter).append(",\n")
                .append(VN_SOURCE)
                .append(",\n")
                .append(EN_TARGET)
                .append("))")
                .append("), '%')");
        return sql.toString();
    }

    public static void buildSqlFilterCreatorAndModifier(StringBuilder sql, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field createdByField = object.getClass().getDeclaredField("createdBy");
        Field lastModifiedField = object.getClass().getDeclaredField("lastModifiedBy");
        createdByField.setAccessible(true);
        lastModifiedField.setAccessible(true);
        if (createdByField.get(object) != null && StringUtils.isNotBlank(createdByField.get(object).toString())) {
            sql.append("AND (")
                    .append(likeIgnoreCaseAndVietnamese("createdBy"))
                    .append(") ");
        }
        if (lastModifiedField.get(object) != null && StringUtils.isNotBlank(lastModifiedField.get(object).toString())) {
            sql.append("AND (")
                    .append(likeIgnoreCaseAndVietnamese("lastModifiedBy"))
                    .append(") ");
        }
    }

    public static void buildParamFilterCreatorAndModifier(Query query, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field createdByField = object.getClass().getDeclaredField("createdBy");
        Field lastModifiedField = object.getClass().getDeclaredField("lastModifiedBy");
        createdByField.setAccessible(true);
        if (createdByField.get(object) != null && StringUtils.isNotBlank(createdByField.get(object).toString())) {
            String createdBy = createdByField.get(object).toString();
            query.setParameter("createdBy", createdBy);
        }
        lastModifiedField.setAccessible(true);
        if (lastModifiedField.get(object) != null && StringUtils.isNotBlank(lastModifiedField.get(object).toString())) {
            String lastModifiedBy = lastModifiedField.get(object).toString();
            query.setParameter("lastModifiedBy", lastModifiedBy);
        }
    }
}

package com.velox.license.irislicenseserver.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.type.SqlTypes;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super(DatabaseVersion.make(3));
    }

    public boolean supportsSequences() {
        return false;
    }

    public boolean supportsIdentityColumns() {
        return true;
    }
    public String getTypeName(int sqlTypeCode) {
        return switch (sqlTypeCode) {
            case SqlTypes.INTEGER -> "integer";
            case SqlTypes.BIGINT -> "integer";
            case SqlTypes.BOOLEAN -> "integer";
            case SqlTypes.VARCHAR -> "text";
            case SqlTypes.TIMESTAMP -> "text";
            default -> "text";
        };
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl() {
            @Override
            public boolean supportsIdentityColumns() {
                return true;
            }

            @Override
            public String getIdentityColumnString(int type) {
                return "integer";
            }

            @Override
            public String getIdentitySelectString(String table, String column, int type) {
                return "select last_insert_rowid()";
            }
        };
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }
}
package com.melissaratos.database.api;

public enum Engine {

    INNODB,
    MYISAM,
    MEMORY,
    MERGE,
    EXAMPLE,
    ARCHIVE,
    CSV,
    BLACKHOLE,
    FEDERATED;

    @Override
    public String toString() {
        if(this == Engine.INNODB)
            return "InnoDB";
        else if(this == Engine.MYISAM)
            return "MyISAM";
        return super.toString();
    }
}

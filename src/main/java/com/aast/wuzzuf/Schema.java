
package com.aast.wuzzuf;


public class Schema {
    private String type;
    private String column;

    public Schema(String type, String column) {
        this.type = type;
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    
}

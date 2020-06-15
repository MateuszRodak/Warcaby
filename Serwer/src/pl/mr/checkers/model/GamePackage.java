package pl.mr.checkers.model;

import java.io.Serializable;

public class GamePackage implements Serializable {
    private PackageType type;
    private Object content;

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

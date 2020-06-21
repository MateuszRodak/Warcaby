package pl.mr.checkers.model;

import java.io.Serializable;

public class GamePackage implements Serializable
{
    private PackageType type;
    private Object content;
    private String user;
    private String result;

    public PackageType getType()
    {
        return type;
    }

    public void setType(PackageType type)
    {
        this.type = type;
    }

    public Object getContent()
    {
        return content;
    }

    public void setContent(Object content)
    {
        this.content = content;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }
}

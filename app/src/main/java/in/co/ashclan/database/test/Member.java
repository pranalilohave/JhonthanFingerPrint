package in.co.ashclan.database.test;

public class Member {
    int id;
    String name,template,bytes;

    public Member() {
    }

    public Member(int id, String name, String template, String bytes) {
        this.id = id;
        this.name = name;
        this.template = template;
        this.bytes = bytes;
    }

    public Member(String name, String template, String bytes) {
        this.name = name;
        this.template = template;
        this.bytes = bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }
}

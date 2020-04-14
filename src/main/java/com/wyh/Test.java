package com.wyh;

public class Test {

    private Integer version;

    public String getVersion() {
        return "v" + version + ".0";
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setVersion(1);
        System.out.println(test.getVersion());
    }

}

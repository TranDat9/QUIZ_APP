package com.example.exampratice;

public class CatagoryModel {
    private  String docID;
    private String name;
    private  int noOfTest;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfTest() {
        return noOfTest;
    }

    public void setNoOfTest(int noOfTest) {
        this.noOfTest = noOfTest;
    }

    public CatagoryModel() {
    }

    public CatagoryModel(String docID, String name, int noOfTest) {
        this.docID = docID;
        this.name = name;
        this.noOfTest = noOfTest;
    }
}

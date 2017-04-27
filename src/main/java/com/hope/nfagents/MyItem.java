/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hope.nfagents;

/**
 *
 * @author Orel
 */
public class MyItem {
    private String inn;
    private String kpp;
    private String ogrn;
    private String name;    
    private String address;
    private String dateReg;

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateReg() {
        return dateReg;
    }

    public void setDateReg(String dateReg) {
        this.dateReg = dateReg;
    }
    
    public void clear(){
        this.inn="";
        this.kpp="";
        this.ogrn="";
        this.name="";
        this.address="";
        this.dateReg="";
    }
    
    public boolean isClear(){
        return 
            "".equals(this.inn)&&
            "".equals(this.kpp)&&
            "".equals(this.ogrn)&&
            "".equals(this.name)&&
            "".equals(this.address)&&
            "".equals(this.dateReg);
    }
}

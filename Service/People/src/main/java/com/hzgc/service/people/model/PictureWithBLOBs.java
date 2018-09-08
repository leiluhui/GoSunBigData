package com.hzgc.service.people.model;

public class PictureWithBLOBs extends Picture {
    private byte[] idcardpic;

    private byte[] capturepic;

    public byte[] getIdcardpic() {
        return idcardpic;
    }

    public void setIdcardpic(byte[] idcardpic) {
        this.idcardpic = idcardpic;
    }

    public byte[] getCapturepic() {
        return capturepic;
    }

    public void setCapturepic(byte[] capturepic) {
        this.capturepic = capturepic;
    }
}
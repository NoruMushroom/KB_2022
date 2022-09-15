package com.example.kb_2022;

public class Comment_Type {
    private String Writer;
    private String Content;
    private String Cno;

    public String getWriter() {
        return Writer;
    }
    public String getContent(){return Content;}
    public String getCno() {return Cno;}
    public void setContent(String content ) { this.Content = content; }
    public void setWriter(String writer) {
        this.Writer = writer;
    }
    public void setCno(String Cno) {this.Cno = Cno;}
}

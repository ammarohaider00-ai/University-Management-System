class Book extends AssignmentObjects {
    private String title;
    private int noofpages;
    public Book() {
        super();
        title=null;
        noofpages=0;
    }
       public Book(String type,String title,int noofpages) {
        super(type);
        this.title=title;
        this.noofpages=noofpages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoofpages() {
        return noofpages;
    }

    public void setNoofpages(int noofpages) {
        this.noofpages = noofpages;
    }
  public String toString() {
    return (super.toString()+"\n"+"Book Title:"+title+"\n" +"No of Pages:"+noofpages);
  }
}
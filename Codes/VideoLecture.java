class VideoLecture extends AssignmentObjects {

    private String title;
    private int duration;
    public VideoLecture() {
        title=null;
        duration=0;
    }

    public VideoLecture(String type, String title, int duration) {
        super(type);
        this.title=title;
        this.duration=duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

 
    public String toString() {
        return (super.toString()+"\n"+"Video Title:"+title+"\n"+"Duration:"+duration+"Minutes");
                
    }
}
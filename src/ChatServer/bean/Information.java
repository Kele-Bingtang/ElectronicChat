package ChatServer.bean;

public class Information {
    //用户名
    private String uid;
    //昵称
    private String nickName;
    //签名
    private String signNature;
    //状态
    private String status;
    //头像ID
    private int iconID;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignNature() {
        return signNature;
    }

    public void setSignNature(String signNature) {
        this.signNature = signNature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
}

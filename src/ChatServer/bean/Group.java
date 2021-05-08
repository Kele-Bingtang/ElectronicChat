package ChatServer.bean;

public class Group {
    //群主id
    private String userid;
    //群id
    private String groupid;
    //群名字
    private String groupName;
    //用户id
    private String groupUserid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUserid() {
        return groupUserid;
    }

    public void setGroupUserid(String groupUserid) {
        this.groupUserid = groupUserid;
    }
}

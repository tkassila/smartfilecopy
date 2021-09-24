package smartfilecopy;

public class ListViewDirItem {
    public ListViewDirItem(){}
    public ListViewDirItem(String p_showname, String p_title, String p_path)
    {
        showname = p_showname;
        path = p_path;
        title = p_title;
    }

    public String showname = " ";
    public String path = null;
    public String title = null;
    public String toString() { return showname; }
}

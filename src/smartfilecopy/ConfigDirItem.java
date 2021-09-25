package smartfilecopy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigDirItem {
    public ConfigDirItem(String p_name, String p_source, String p_target)
    {
        name = p_name;
        source = p_source;
        target = p_target;
    }

    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getTarget() {
        return target;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String toString() { return name;}
    public boolean isNewUnSaved() { return isNewUnSaved; }
    public void setNewUnSaved(boolean value ){ isNewUnSaved = value; }

    private String name;
    private String source;
    private String target;
    private boolean isNewUnSaved = false;

    public static final String cnstConfigDirItems = "ConfigDirItems";
    public static final String cnstselectedtem = "selectedtem";
    public static ConfigDirItem selectedtem = null;

    public static ConfigDirItem [] loadConfigsFromFile(File userConfig)
            throws FileNotFoundException, IOException, ParseException
    {
        if (userConfig == null)
            return null;
        if (!userConfig.exists())
            return null;
        List<ConfigDirItem> listRet = new ArrayList<ConfigDirItem>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(userConfig));
                if (obj == null)
                    return null;
                // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
                JSONObject jsonObject = (JSONObject) obj;

                // A JSON array. JSONObject supports java.util.List interface.
                JSONArray companyList = (JSONArray) jsonObject.get(cnstConfigDirItems);
                Object obj2 = jsonObject.get(ConfigDirItem.cnstselectedtem);
                String jsonSeletecItem = (String) obj2;
                // An iterator over a collection. Iterator takes the place of Enumeration in the Java Collections Framework.
                // Iterators differ from enumerations in two ways:
                // 1. Iterators allow the caller to remove elements from the underlying collection during the iteration with well-defined semantics.
                // 2. Method names have been improved.
                Iterator<JSONObject> iterator = companyList.iterator();
                JSONObject jsonObj;
                String name, source, target;
                ConfigDirItem ci = null;
                int iCounter = 0;
                while (iterator.hasNext()) {
                    jsonObj = iterator.next();
                    System.out.println(jsonObj);
                    name = (String) jsonObj.get("name");
                    source = (String) jsonObj.get("source");
                    target = (String) jsonObj.get("target");
                    ci = new ConfigDirItem(name, source, target);
                    listRet.add(ci);
                }

                ConfigDirItem [] ret = new ConfigDirItem [listRet.size()];
                ret = listRet.toArray(ret);
                if (jsonSeletecItem != null && jsonSeletecItem.toString().trim().length() >0)
                {
                    for (ConfigDirItem cdi : ret)
                    {
                        if (cdi.getName().equals(jsonSeletecItem.toString())) {
                            selectedtem = cdi;
                            break;
                        }
                    }
                }
                return ret;
      /* } catch (Exception e) {
                e.printStackTrace();
                return null;
        }
        */
    }

    public static boolean writeJsonFile(File userConfig, ConfigDirItem [] configitems, ConfigDirItem selectedItem)
    {
        if (userConfig == null)
            return false;
        if (userConfig.exists() && !userConfig.isFile())
            return false;

        JSONArray items = new JSONArray();
        JSONObject jsonObj ;
        for(ConfigDirItem ci : configitems)
        {
            jsonObj = new JSONObject();
            jsonObj.put("name", ci.getName());
            jsonObj.put("source", ci.getSource());
            jsonObj.put("target", ci.getTarget());
            /*
            if (ci.getName().equals(selectedItem.getName()))
            {
                jsonObj.put("source", selectedItem.getSource());
                jsonObj.put("target", selectedItem.getTarget());
            }
             */
            items.add(jsonObj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConfigDirItem.cnstConfigDirItems, items);
        if (selectedItem == null && configitems.length == 1)
        {
            selectedItem = configitems[0];
        }
        jsonObject.put(ConfigDirItem.cnstselectedtem, (selectedItem != null ? selectedItem.getName() : ""));

        try {
            FileWriter file = new FileWriter(userConfig);
            file.write(jsonObject.toJSONString());
            file.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

}

package smartfilecopy;

import java.util.HashMap;

public class StartWithimportHashMap {
    private HashMap<String,FoundedHashFiles> hmMap = null;
    public StartWithimportHashMap(HashMap<String,FoundedHashFiles> p_hmMap)
    {
        hmMap = p_hmMap;
    }

    public FoundedHashFiles getStartWith(String key)
    {
        FoundedHashFiles ret = null;
        if (key != null && key.trim().length() > 0 && hmMap != null && hmMap.size() > 0)
        {
            for(String hmKey : hmMap.keySet())
            {
                if (hmKey.startsWith(key)) {
                    ret = hmMap.get(hmKey);
                    break;
                }
            }

            if (ret == null)
            {
                for(String hmKey : hmMap.keySet())
                {
                    if (key.startsWith(hmKey)) {
                        ret = hmMap.get(hmKey);
                        break;
                    }
                }
            }

        }

        return ret;
    }
}

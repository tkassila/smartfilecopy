package smartfilecopy;

import org.apache.commons.text.similarity.LongestCommonSubsequenceDistance;
// import org.apache.commons.lang3.StringUtils;

//Usage of Simmetrics
// import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class TestSimilarity {
    /*
    public double compareStrings(String stringA, String stringB) {
        JaroWinkler algorithm = new JaroWinkler();
        return algorithm.getSimilarity(stringA, stringB);
    }
    */

    public static void main(String[] args) {
        String str1 = "kissa kävelee";
        String str2 = "kk kissa kävelee";
        LongestCommonSubsequenceDistance lsd = new LongestCommonSubsequenceDistance();
        int iCount = lsd.apply(str1, str2);
        String left = str2; // "New York";
        String rigth = str1; // "New Hampshire";
        iCount = lsd.apply(left, rigth);
        System.out.println("iCount=" +iCount);
        System.out.println("left=" +left.length());
        System.out.println("rigth=" +rigth.length());
        System.out.println("iCount1=" +(iCount - left.length() - rigth.length()));
        // System.out.println("compareStrings=" +compareStrings(left, rigth));
        LevenshteinDistance l = new LevenshteinDistance();
        System.out.println("iCount=" +iCount);
        iCount = l.apply(left, rigth);
        System.out.println("2-- iCount=" +iCount);

        JaroWinklerSimilarity jws = new JaroWinklerSimilarity();
        double dCount = jws.apply(left, rigth);
        System.out.println("3-- dCount=" +dCount);
        double dCount2 = jws.apply(rigth, left);
        System.out.println("4-- dCount=" +dCount2);

        left = "New York";
        rigth = "New Hampshire";
        dCount = jws.apply(left, rigth);
        System.out.println("3.b-- dCount=" +dCount);
        dCount2 = jws.apply(rigth, left);
        System.out.println("4.b-- dCount=" +dCount2);
    }
}

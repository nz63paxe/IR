package robinhood;

public class Variables {
	public static final String DataPath = "../data/";
    public static final String argumentIndexPath = "indexes/arguments";
    public static final String concatenatedArgumentIndexPath = "indexes/concat_arguments";
    public static final String embeddedArgumentIndexPath = "indexes/embedded_arguments";
    public static final String evalOutputPath = "../evaluation/runs/";
    public static final String evalTopicsPath = "../evaluation/default_topics.xml";
    public static String[] jsonFiles = new String[] {
            DataPath + "parliamentary.json",
            DataPath + "debatepedia.json",
            DataPath + "debatewise.json",
            DataPath + "idebate.json",
            DataPath + "debateorg.json"
    };
}

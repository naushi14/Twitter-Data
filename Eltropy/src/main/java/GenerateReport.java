import javax.xml.crypto.Data;
import java.util.List;

public class GenerateReport {
    public static void processReportHeader(StringBuilder htmlContent) {
//        StringBuilder htmlContent=new StringBuilder();
        htmlContent.append("<table style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        htmlContent.append("<tr>");
        addColumnHeaderFor(htmlContent,"Twitter Handle");

        addColumnHeaderFor(htmlContent,"Twitter Handle URL");
        addColumnHeaderFor(htmlContent,"Snapshot of Landing Page");
        addColumnHeaderFor(htmlContent,"Top 10 tweet Id & text");
        addColumnHeaderFor(htmlContent,"Top 10 tweets images");
        addColumnHeaderFor(htmlContent,"Top 10 friends");

        htmlContent.append("</tr>");
    }

    public static void addColumnHeaderFor(StringBuilder htmlContent, String content) {
        htmlContent.append("<th style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        htmlContent.append(content);
        htmlContent.append("</th>");

    }

    public static void addColumnFor(StringBuilder htmlContent, String content) {
        htmlContent.append("<td style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        htmlContent.append(content);
        htmlContent.append("</td>");
    }
    public static void addImage(StringBuilder htmlContent,String filepath) {
        htmlContent.append("<td style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        htmlContent.append("<img src="+filepath+" style=width:200px;/>");
        htmlContent.append("</td>");
    }
    public static void addTweets(StringBuilder htmlContent, List<Base> tweets) {
        htmlContent.append("<td style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        for(Base tweet :tweets){
            htmlContent.append("<b>"+ tweet.getId_str()+"</b> - "+tweet.getText());
            htmlContent.append("<br>");
        }
        htmlContent.append("</td>");
    }
    public static void addFriends(StringBuilder htmlContent, List<User> friends) {
        htmlContent.append("<td style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
        int i=1;
        for(User friend :friends){
            htmlContent.append(i++ +"- "+friend.getScreen_name());
            htmlContent.append("<br>");
        }
        htmlContent.append("</td>");
    }
}

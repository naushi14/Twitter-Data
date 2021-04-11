import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static io.restassured.RestAssured.given;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Base {
    static String accessToken="Bearer AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8";

    private String created_at;
    private String text;
    private String id_str;
    private User user;
    private int retweet_count;
    private int favorite_count;

    public int getRetweet_count() { return retweet_count; }
    public int getFavorite_count() { return favorite_count; }
    public User getUser() { return user; }
    public String getId_str() { return id_str;}
    public String getCreated_at() { return created_at; }
    public String getText() { return text; }

    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public void setText(String text) { this.text = text; }
    public void setId_str(String id_str) {this.id_str = id_str; }
    public void setUser(User user) { this.user = user; }
    public void setRetweet_count(int retweet_count) { this.retweet_count = retweet_count; }
    public void setFavorite_count(int favorite_count) { this.favorite_count = favorite_count; }

    public static List<User> generateFriends(String name) throws IOException {
        String res=given().header("Authorization", accessToken)
                .queryParam("screen_name",name)
                .queryParam("count","100")
                .when().get("https://api.twitter.com/1.1/friends/list.json")
                .then().extract().response().body().asString();
//        System.out.println(res);
        ObjectMapper friend_obj = new ObjectMapper();
        Friend friend_parse=friend_obj.readValue(res,Friend.class);

//        System.out.println(friend_parse.getUsers().get(3).getScreen_name());
        Collections.sort(friend_parse.getUsers(), new Comparator<User>(){
            @Override
            public int compare(User o1, User o2) {
                return o2.getFollowers_count()-(o1.getFollowers_count());
            }
        });

        //data for top 10 friends based on highest no of followers
        List<User> friendsSubArray = new ArrayList<>();
        for(int i=0;i<10;i++){
            User temp=new User();
            temp.setScreen_name(friend_parse.getUsers().get(i).getScreen_name());
            temp.setFriends_count(friend_parse.getUsers().get(i).getFriends_count());
            temp.setFollowers_count(friend_parse.getUsers().get(i).getFollowers_count());
            friendsSubArray.add(temp);
        }
        return friendsSubArray;


    }
    public static List<Base> generateUserData(String name) throws IOException {
        String res=given().header("Authorization", accessToken)
                .queryParam("screen_name",name)
                .queryParam("count","100")
                .queryParam("include_rts","false")
                .when().get("https://api.twitter.com/1.1/statuses/user_timeline.json")
                .then().extract().response().body().asString();
//       System.out.println(res);
        ObjectMapper obj=new ObjectMapper();
        Base[] parseArray= obj.readValue(res, Base[].class);

        Arrays.sort(parseArray, new Comparator<Base>(){
            @Override
            public int compare(Base o1, Base o2) {
                return o2.getRetweet_count() - (o1.getRetweet_count());
            }
        });

        //data for top 10 tweets
        List<Base> subArray = new ArrayList<>();
        for(int i=0;i<10;i++){
            Base temp=new Base();
            temp.setCreated_at(parseArray[i].getCreated_at());
            temp.setId_str(parseArray[i].getId_str());
            temp.setText(parseArray[i].getText());
            temp.setRetweet_count(parseArray[i].getRetweet_count());
            temp.setFavorite_count(parseArray[i].getFavorite_count());
            User x=new User();
            x.setScreen_name(parseArray[i].getUser().getScreen_name());
            temp.setUser(x);
            subArray.add(temp);
        }
        return subArray;
    }


}

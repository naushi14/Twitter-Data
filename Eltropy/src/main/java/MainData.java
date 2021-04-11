import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainData {
    String name;
    List<Base> tweets;
    List<User> friends;


    public static List<MainData> readData() throws IOException {
        List<MainData> twitterData= new ArrayList<>();
        File myObj = new File("src/main/resources/Data.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            MainData temp = new MainData();
            String username = myReader.nextLine();
            temp.name=username;
            temp.tweets=Base.generateUserData(username);
            temp.friends=Base.generateFriends(username);
            twitterData.add(temp);
        }
//        for(int i=0;i<10;i++){
//            System.out.println(twitterData.get(0).friends.get(i).getFollowers_count());
//        }
        return twitterData;
    }

//    public static void main(String[] args) throws IOException {
//        readData();
//
//    }
}

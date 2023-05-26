import cinema.Seanse;
import cinema.Sessions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import person.Person;
import person.Phones;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

public class Parser {
    public static void main(String args[])
    {
        try {
            Gson g = new Gson();

            //Person person = g.fromJson(jsonString, Person.class);
            Type personTypeObj = Person.class;
            Person person = g.fromJson(new FileReader("src/main/resources/Person.json"), /*Person.class*/ personTypeObj);
            for (Person friend : person.friends) {
                System.out.print(friend.lastName);
                for (Phones phone : friend.phoneNumbers) {
                    System.out.println(" - phone type: " + phone.type + ", phone number : " + phone.number);
                }
            }
            System.out.println("=================================");

            Type type = new TypeToken<Map<String, Seanse>>(){}.getType();
            Map<String, Seanse> myMap = g.fromJson(new FileReader("src/main/resources/Cinema.json"), type);

            Iterator<String> iterator = myMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                //System.out.println(key + ":" + myMap.get(key));
                System.out.println("Seanse " + key + ":");
                Seanse currentSeanse = myMap.get(key);
                System.out.println(currentSeanse.name + ", " + currentSeanse.locate + ", " + currentSeanse.metro);
                System.out.println("Sessions information:");
                for(Sessions session : currentSeanse.sessions){
                    System.out.println("Session: " + session.time + ", " + session.price);
                }
            }
            System.out.println("=================================");
            System.out.println("Complete!");
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }
}

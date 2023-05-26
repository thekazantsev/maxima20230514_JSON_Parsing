import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import cinema.Sessions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import person.Person;
import person.Phones;
import cinema.Seance;

public class GsonParser {
    public static void main(String[] args) {
        String personJsonFile = "src/main/resources/Person.json";
        String cinemaJsonFile = "src/main/resources/Cinema.json";
        String charset = "UTF-8";

        Type personType = Person.class;

        Person deserializedPerson = (Person) deserializeToObjectFromFile(personJsonFile, charset, personType);
//        serializeToString(deserializedPerson);
        getPersonFriendsInfo(deserializedPerson);

        System.out.println("====================justadivider====================");

        // Приведение к типу с помощью TypeToken от Gson
//        Type type = new TypeToken<Map<String, Seance>>(){}.getType();
//        Map cinemaMap = deserializeToMapFromFile(cinemaJsonFile, charset, type);

        // передача типов в качестве параметра
        Map cinemaMap = deserializeToMap(cinemaJsonFile, charset, String.class, Seance.class);
        iterateOverACinemaMap(cinemaMap);
    }

    private static void serializeToString(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        System.out.println(json);
    }

    private static Object deserializeToObjectFromFile(String fileName, String charset, Type objType) {
        Object obj = null;

        try (Reader reader = new FileReader(fileName, Charset.forName(charset))) {
            Gson gson = new Gson();
            obj = gson.fromJson(reader, objType);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    private static void getPersonFriendsInfo(Person deserializedPerson) {
        for (Person friend : deserializedPerson.friends) {
            System.out.println(friend.lastName);
            for (Phones phone : friend.phoneNumbers) {
                System.out.println(" - phone type: " + phone.type + ", phone number : " + phone.number);
            }
            if (friend.friends != null) {
                for (Person friendOfFriend : friend.friends) {
                    System.out.println(" - friend: " + friendOfFriend.lastName);
                }
            }
        }
    }

    private static Map deserializeToMapFromFile(String fileName, String charset, Type type) {
        Map map = null;

        try (Reader reader = new FileReader(fileName, Charset.forName(charset))) {
            map = new Gson().fromJson(reader, type);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }


    private static <K, V> Map deserializeToMap(String fileName, String charset, Class<K> keyClass, Class<V> valueClass) {
        Map map = null;

        try (Reader reader = new FileReader(fileName, Charset.forName(charset))) {
            Type type = mapType(keyClass, valueClass);
            map = new Gson().fromJson(reader, type);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static ParameterizedType mapType(Class<?> keyClass, Class<?> valueClass) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{keyClass, valueClass};
            }

            @Override
            public Type getRawType() {
                return Map.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public static void iterateOverACinemaMap(Map<String, Seance> map) {
        Iterator<String> iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println("Seance " + key + ":");
            Seance currentSeance = map.get(key);
            System.out.println(currentSeance.name + ", " + currentSeance.locate + ", " + currentSeance.metro);
            System.out.println("Sessions information:");
            for(Sessions session : currentSeance.sessions){
                System.out.println("Session: " + session.time + ", " + session.price);
            }
        }
    }
}

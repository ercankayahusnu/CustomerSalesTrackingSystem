import core.Database;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {

        Connection connection1= Database.getInstance();
        Connection connection2= Database.getInstance();
    }
}

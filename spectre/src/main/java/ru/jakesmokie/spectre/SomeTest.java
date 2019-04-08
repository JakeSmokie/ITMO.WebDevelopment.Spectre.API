package ru.jakesmokie.spectre;

import lombok.SneakyThrows;
import lombok.val;

import java.sql.DriverManager;

public class SomeTest {
    @SneakyThrows
    public static void main(String[] args) {
        try (val connection = DriverManager.getConnection(
                "jdbc:postgresql://jakesmokie.ru:5432/postgres?currentSchema=spectre", "postgres",
                "iu8hhdshfiu3b1i")) {
            final val s = connection.createStatement().executeQuery("TABLE race;");

            while (s.next()) {
                System.out.println(s.getString("name"));
            }

            s.close();
        }

    }
}

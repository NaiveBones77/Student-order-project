package edu.studentorder.dao;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DBInit {
    private static final String FILENAME_1 = "student_project.sql";
    private static final String FILENAME_2 = "insert_data_db.sql";

    public static void resetDB () throws Exception
    {
        URL url1 = DictionaryDaoImplTest.class.getClassLoader()
                .getResource(FILENAME_1);
        URL url2 = DictionaryDaoImplTest.class.getClassLoader()
                .getResource(FILENAME_2);

        List<String> str1 = Files.readAllLines(Paths.get(url1.toURI()));
        List<String> str2 = Files.readAllLines(Paths.get(url2.toURI()));
        String sql1 = str1.stream().collect(Collectors.joining());
        String sql2 = str2.stream().collect(Collectors.joining());
        try (Connection con = ConnectionBuilder.getConnection();
             Statement stm = con.createStatement();
        ) {
            stm.executeUpdate(sql1);
            stm.executeUpdate(sql2);
        }
    }
}

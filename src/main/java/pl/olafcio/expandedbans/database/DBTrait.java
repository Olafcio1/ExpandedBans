package pl.olafcio.expandedbans.database;

import java.sql.Connection;
import java.sql.Statement;

public interface DBTrait {
    Connection connection();
    Statement statement();
}

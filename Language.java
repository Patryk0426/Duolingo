import java.sql.Connection;
import java.sql.SQLException;

abstract class Language {
    protected String languageName;
    protected static final String DB_URL = "jdbc:mysql://localhost:3306/duolingo";
    protected static final String DB_USER = "root";
    protected static final String DB_PASSWORD = "";

    public Language(String languageName) {
        this.languageName = languageName;
    }

    public abstract void learn();


    protected abstract boolean checkProgress(Connection connection) throws SQLException;

    // Abstrakcyjna metoda do aktualizacji postępu
    protected abstract void updateProgress(Connection connection) throws SQLException;

    @Override
    public String toString() {
        return languageName;
    }
}
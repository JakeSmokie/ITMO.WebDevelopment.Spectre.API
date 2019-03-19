import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;

public class Test {
    public static void main(String[] args) {
        val service = new AuthenticationService();
        val token = "PE1aOFcIf3rAHazNjYbV0chx_Zs.*AAJTSQACMDEAAlNLABxhMHIrQXY5VFFDODBHQXBTQnJNdDJFY2F0NFk9AAR0eXBlAANDVFMAAlMxAAA.*";

        System.out.println(service.isValidToken(token));
        System.out.println(service.getSessionProperties(token));
        System.out.println(service.isKeykeeper(token));
    }
}

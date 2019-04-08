import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;

public class Test {
    public static void main(String[] args) {
        val service = new AuthenticationService();

        System.out.println(service.getUserProperties("demo"));
        System.out.println(service.getUserProperties("jakesmokie"));
        System.out.println(service.getUserProperties("amAdmin"));
    }
}

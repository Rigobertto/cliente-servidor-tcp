package tcp;

public class P4 {
	public static void main(String[] args) {
        Client P4 = new Client("localhost", 5000, "P4");
        P4.start();
    }
}

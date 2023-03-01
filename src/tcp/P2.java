package tcp;

public class P2 {
	public static void main(String[] args) {
        Client P2 = new Client("localhost", 5000, "P2");
        P2.start();
    }
}

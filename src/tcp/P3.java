package tcp;

public class P3 {
	public static void main(String[] args) {
        Client P3 = new Client("localhost", 5000, "P3");
        P3.start();
    }
}

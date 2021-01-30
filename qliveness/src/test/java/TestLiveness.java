import blxt.qjava.qliveness.Register;


public class TestLiveness {

    public static void main(String[] args) throws Exception {
        Register register = new Register();
        register.setArl("http://127.0.0.1:8080/register/mqtt");
        register.register();
    }


}
